package com.doganmehmet.app.mapper;

import com.doganmehmet.app.dto.cardinfo.CardInfoDTO;
import com.doganmehmet.app.entity.CardInfo;
import com.doganmehmet.app.request.CardInfoRequest;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(implementationName = "CardInfoMapperImpl", componentModel = "spring")
public interface ICardInfoMapper {
    CardInfo toCardInfo(CardInfoRequest cardInfoRequest);

    CardInfoDTO toCardInfoDTO(CardInfo cardInfo);

    List<CardInfoDTO> toCardInfoDTOList(List<CardInfo> cardInfoList);
}
