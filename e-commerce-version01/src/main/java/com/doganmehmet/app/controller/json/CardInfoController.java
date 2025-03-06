package com.doganmehmet.app.controller.json;

import com.doganmehmet.app.bean.json.JSONBeanName;
import com.doganmehmet.app.dto.cardinfo.CardInfoDTO;
import com.doganmehmet.app.request.CardInfoRequest;
import com.doganmehmet.app.service.SecurityControl;
import com.doganmehmet.app.service.CardInfoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController(JSONBeanName.JSON_CARD_INFO_CONTROLLER)
@RequestMapping("/json/api/card")
public class CardInfoController {
    private final CardInfoService m_cardInfoService;
    private final SecurityControl m_securityControl;

    public CardInfoController(CardInfoService cardInfoService, SecurityControl securityControl)
    {
        m_cardInfoService = cardInfoService;
        m_securityControl = securityControl;
    }

    @PostMapping("/save")
    public CardInfoDTO saveCard(@RequestBody CardInfoRequest cardInfoRequest)
    {
        m_securityControl.checkTokenUserMatch(cardInfoRequest.getUsername());
        return m_cardInfoService.saveCard(cardInfoRequest);
    }

    @GetMapping("/find")
    public List<CardInfoDTO> findCard(@RequestParam String username)
    {
        m_securityControl.checkTokenUserMatch(username);
        return m_cardInfoService.findCardByUsername(username);
    }

    @DeleteMapping("/delete")
    public String deleteCard(@RequestParam String username, @RequestParam String cardNumber)
    {
        m_securityControl.checkTokenUserMatch(username);
        return m_cardInfoService.deleteCardByUsername(username, cardNumber);
    }
}
