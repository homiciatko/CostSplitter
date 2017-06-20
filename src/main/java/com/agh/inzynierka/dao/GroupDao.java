package com.agh.inzynierka.dao;


import com.agh.inzynierka.model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupDao extends MongoRepository<Group, String> {
}
