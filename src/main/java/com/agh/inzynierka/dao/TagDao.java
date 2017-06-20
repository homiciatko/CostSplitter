package com.agh.inzynierka.dao;


import com.agh.inzynierka.model.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagDao extends MongoRepository<Tag, String>{
}
