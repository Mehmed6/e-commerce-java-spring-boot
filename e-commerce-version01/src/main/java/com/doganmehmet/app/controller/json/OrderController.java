package com.doganmehmet.app.controller.json;


import com.doganmehmet.app.bean.json.JSONBeanName;
import com.doganmehmet.app.dto.order.OrderDTO;
import com.doganmehmet.app.dto.order.OrderDTOS;
import com.doganmehmet.app.enums.Status;
import com.doganmehmet.app.mapper.IOrderMapper;
import com.doganmehmet.app.service.SecurityControl;
import com.doganmehmet.app.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController(JSONBeanName.JSON_ORDER_CONTROLLER)
@RequestMapping("json/api/order")
public class OrderController {
    private final OrderService m_orderService;
    private final IOrderMapper m_orderMapper;
    private final SecurityControl m_securityControl;

    public OrderController(OrderService orderService, IOrderMapper orderMapper, SecurityControl securityControl)
    {
        m_orderService = orderService;
        m_orderMapper = orderMapper;
        m_securityControl = securityControl;
    }

    @PostMapping("/create")
    public OrderDTO createOrder(@RequestParam String username,@RequestParam String password)
    {
        m_securityControl.checkTokenUserMatch(username);
        return m_orderService.creatOrder(username, password);
    }

    @PostMapping("/complete")
    @PreAuthorize("hasRole('ADMIN')")
    public OrderDTO completeOrder(@RequestParam Long orderId)
    {
        return m_orderService.completeOrder(orderId);
    }

    @GetMapping("/find")
    public OrderDTOS findOrderByUsername(@RequestParam String username)
    {
        m_securityControl.checkTokenUserMatch(username);
        return m_orderMapper.toOrderDTOS(m_orderService.findOrderByUsername(username));
    }

    @DeleteMapping("delete/id")
    public OrderDTOS deleteOrderById(@RequestParam String username, @RequestParam Long orderId)
    {
        m_securityControl.checkTokenUserMatch(username);
        return m_orderService.deleteOrderById(username, orderId);
    }

    @DeleteMapping("delete/status")
    public OrderDTOS deleteOrderIfCancel(@RequestParam String username, @RequestParam Status status)
    {
        m_securityControl.checkTokenUserMatch(username);
        return m_orderService.deleteOrderByStatus(username, status);
    }

    @DeleteMapping("delete/all")
    public void deleteAll(@RequestParam String username)
    {
        m_securityControl.checkTokenUserMatch(username);
        m_orderService.deleteAll();
    }
}
