package com.doganmehmet.app.controller.json;

import com.doganmehmet.app.bean.json.JSONBeanName;
import com.doganmehmet.app.request.PaymentRequest;
import com.doganmehmet.app.services.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;


@RestController(JSONBeanName.JSON_PAYMENT_CONTROLLER)
@RequestMapping("json/api/payment")
public class PaymentController {
    private final PaymentService m_paymentService;

    public PaymentController(PaymentService paymentService)
    {
        m_paymentService = paymentService;
    }

    @PostMapping("/pay")
    public String payment(@RequestBody PaymentRequest paymentRequest, HttpServletRequest request)
    {
        return m_paymentService.makePayment(paymentRequest.getAddressId(),
                paymentRequest.getCardId(),
                paymentRequest.getOrderId(),
                request);
    }
}
