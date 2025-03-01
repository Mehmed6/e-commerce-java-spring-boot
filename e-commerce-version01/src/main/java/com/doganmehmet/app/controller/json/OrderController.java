package com.doganmehmet.app.controller.json;


import com.doganmehmet.app.bean.json.JSONBeanName;
import com.doganmehmet.app.dto.order.OrderDTO;
import com.doganmehmet.app.dto.order.OrderDTOS;
import com.doganmehmet.app.enums.Status;
import com.doganmehmet.app.mapper.IOrderMapper;
import com.doganmehmet.app.services.json.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController(JSONBeanName.JSON_ORDER_CONTROLLER)
@RequestMapping("json/api/order")
public class OrderController {
    private final OrderService m_orderService;
    private final IOrderMapper m_orderMapper;

    public OrderController(OrderService orderService, IOrderMapper orderMapper)
    {
        m_orderService = orderService;
        m_orderMapper = orderMapper;
    }

    @PostMapping("/create")
    public OrderDTO createOrder(@RequestParam String username,@RequestParam String password)
    {
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
        return m_orderMapper.toOrderDTOS(m_orderService.findOrderByUsername(username));
    }

    @DeleteMapping("delete/id")
    public OrderDTOS deleteOrderById(@RequestParam String username, @RequestParam Long orderId)
    {
        return m_orderService.deleteOrderById(username, orderId);
    }

    @DeleteMapping("delete/status")
    public OrderDTOS deleteOrderIfCancel(@RequestParam String username, @RequestParam Status status)
    {
        return m_orderService.deleteOrderIfCancel(username, status);
    }

    @DeleteMapping("delete/all")
    public void deleteAll(@RequestParam String username)
    {
        m_orderService.deleteAll(username);
    }
}
