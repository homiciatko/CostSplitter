package com.agh.inzynierka.dao;

import com.agh.inzynierka.model.Debt;
import com.agh.inzynierka.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;


@Repository
public interface DebtDao extends MongoRepository<Debt, String> {

    List<Debt> findAllByUser(User user);
    List<Debt> findByUserId(String userId);


}
