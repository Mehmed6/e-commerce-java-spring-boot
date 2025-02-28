package com.doganmehmet.app.controller.json;

import com.doganmehmet.app.dto.cardinfo.CardInfoDTO;
import com.doganmehmet.app.request.CardInfoRequest;
import com.doganmehmet.app.services.json.CardInfoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/json/api/card")
public class CardInfoController {
    private final CardInfoService m_cardInfoService;

    public CardInfoController(CardInfoService cardInfoService)
    {
        m_cardInfoService = cardInfoService;
    }

    @PostMapping("/save")
    public CardInfoDTO saveCard(@RequestBody CardInfoRequest cardInfoRequest)
    {
        return m_cardInfoService.saveCard(cardInfoRequest);
    }

    @GetMapping("/find")
    public List<CardInfoDTO> findCard(@RequestParam String username)
    {
        return m_cardInfoService.findCardByUsername(username);
    }

    @DeleteMapping("/delete")
    public String deleteCard(@RequestParam String username, @RequestParam String cardNumber)
    {
        return m_cardInfoService.deleteCardByUsername(username, cardNumber);
    }
}
