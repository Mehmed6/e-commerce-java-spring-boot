package com.doganmehmet.app.repository;

import com.doganmehmet.app.entity.CardInfo;
import com.doganmehmet.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICardInfoRepository extends JpaRepository<CardInfo, Long> {
    List<CardInfo> findByUser(User user);

    CardInfo findByUserAndCardNumber(User user, String cardNumber);

    boolean existsByCardNumber(String cardNumber);

    CardInfo findByCardNumber(String cardNumber);
}
