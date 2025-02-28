package com.doganmehmet.app.services.json;

import com.doganmehmet.app.dto.cardinfo.CardInfoDTO;
import com.doganmehmet.app.entity.CardInfo;
import com.doganmehmet.app.exception.ApiException;
import com.doganmehmet.app.exception.MyError;
import com.doganmehmet.app.mapper.ICardInfoMapper;
import com.doganmehmet.app.repositories.ICardInfoRepository;
import com.doganmehmet.app.repositories.IUserRepository;
import com.doganmehmet.app.request.CardInfoRequest;
import com.doganmehmet.app.services.SecurityControl;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CardInfoService {
    private final ICardInfoRepository m_cardInfoRepository;
    private final ICardInfoMapper m_cardInfoMapper;
    private final SecurityControl m_securityControl;
    private final IUserRepository m_userRepository;

    public CardInfoService(ICardInfoRepository cardInfoRepository, ICardInfoMapper cardInfoMapper, SecurityControl securityControl, IUserRepository userRepository)
    {
        m_cardInfoRepository = cardInfoRepository;
        m_cardInfoMapper = cardInfoMapper;
        m_securityControl = securityControl;
        m_userRepository = userRepository;
    }

    private Optional<CardInfo> isExistingCard(CardInfoRequest cardInfoRequest)
    {
        var matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnorePaths("username", "cardInfoId", "user");

        var example = Example.of(m_cardInfoMapper.toCardInfo(cardInfoRequest), matcher);
        return m_cardInfoRepository.findOne(example);
    }

    public CardInfoDTO saveCard(CardInfoRequest cardInfoRequest)
    {
        m_securityControl.checkTokenUserMatch(cardInfoRequest.getUsername());

        var year = Integer.parseInt(cardInfoRequest.getExpiryYear());
        var month = Integer.parseInt(cardInfoRequest.getExpiryMonth());
        var expiryDate = LocalDate.of(year, month, 1);

        if (expiryDate.isBefore(LocalDate.now()))
            throw new ApiException(MyError.USER_CARD_EXPIRED);

        if (isExistingCard(cardInfoRequest).isPresent())
            throw new ApiException(MyError.CARD_ALREADY_EXISTS);

        if (m_cardInfoRepository.existsByCardNumber((cardInfoRequest.getCardNumber())))
            throw new ApiException(MyError.CARD_NUMBER_ALREADY_IN_USE);
        
        var savedCard = m_cardInfoMapper.toCardInfo(cardInfoRequest);

        savedCard.setUser(m_userRepository.findByUsername(cardInfoRequest.getUsername())
                .orElseThrow(() -> new ApiException(MyError.USER_NOT_FOUND)));

        return m_cardInfoMapper.toCardInfoDTO(m_cardInfoRepository.save(savedCard));
    }

    public List<CardInfoDTO> findCardByUsername(String username)
    {
        m_securityControl.checkTokenUserMatch(username);

        var user = m_userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(MyError.USER_NOT_FOUND));

        return m_cardInfoMapper.toCardInfoDTOList(m_cardInfoRepository.findByUser(user));
    }

    public String deleteCardByUsername(String username, String cardNumber)
    {
        m_securityControl.checkTokenUserMatch(username);
        var user = m_userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(MyError.USER_NOT_FOUND));

        var card = m_cardInfoRepository.findByUserAndCardNumber(user, cardNumber);

        if (card != null) {
            m_cardInfoRepository.delete(card);
            return "Card deleted successfully";
        }
        return "Card not found";

    }
}
