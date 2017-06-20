package com.agh.inzynierka.dao;

import com.agh.inzynierka.model.Tag;
import com.agh.inzynierka.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface UserDao extends MongoRepository<User, String> {

    User findById(String id);

    User findByEmail(String email);

    List<User> findByFirstNameLike(String firstName);

    List<User> findByLastNameLike(String lastName);

    User findByTags(Tag tag);

    @Query("{  ?0 : ?1 }")
    List<User> findByFieldAndValue(@Param("field") String field, @Param("value") String value);

    default List<User> findBySelectInExternalId(@Param("context") String externalContext, @Param("id") String externalId) {
        return findByFieldAndValue("externalIds." + externalContext, externalId);
    }
}
