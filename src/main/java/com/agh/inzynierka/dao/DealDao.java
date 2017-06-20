package com.agh.inzynierka.dao;

import com.agh.inzynierka.model.Deal;
import com.agh.inzynierka.model.Debt;
import com.agh.inzynierka.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DealDao extends MongoRepository<Deal, String> {

    List<Deal> findByDescriptionLikeOrderByCreateDateTimeDesc(String description);

    List<Deal> findByCreateDateTimeAfterOrderByCreateDateTimeDesc(LocalDateTime localDateTime); //TODO: do przetestowania nazwa zmiennej

    List<Deal> findByCreateDateTimeBeforeOrderByCreateDateTimeDesc(LocalDateTime localDateTime);

    List<Deal> findByCreateDateTimeAfterAndCreateDateTimeBeforeOrderByCreateDateTimeDesc(LocalDateTime dateStart, LocalDateTime dateEnd);

    List<Deal> findByParticipantsId(String id);

    List<Deal> findByParticipants(User user);

    List<Deal> findByDebtsUser(User user);

    Deal findById(String id);

    List<Deal> findByDebtsIn(List<Debt> debts);

//    Deal findByDebt(Debt debt);

//    @Query("'debts.$id' : {'$oid': ?0}")
//    @Query("{'debts': {'$ref':'debts','$id':{'$oid':?0}}}")
//    @Query("{ 'debt': {'$ref': 'debts', '$id': { '$oid': ?0 } } }")
//    @Query("{ 'debt': {'$ref': 'debts', '$id': ?0 } }")
    Deal findByDebtsId(String debtId);

//    List<Deal> findByUserId(String userId);
//    List<Deal> findAllByDebtsUserId(String userId);
//    List<Deal> findByDebtsInUserId(String userId);
    List<Deal> findByDebtsUserId(String userId);
    List<Deal> findAllByDebtsUser(User user);

    @Query("'debts.debt._id' : '?0'}")
    Deal findByDebt(String id);





}
