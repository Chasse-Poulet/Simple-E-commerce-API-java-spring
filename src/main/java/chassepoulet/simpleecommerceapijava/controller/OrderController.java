package chassepoulet.simpleecommerceapijava.controller;

import chassepoulet.simpleecommerceapijava.model.Order;
import chassepoulet.simpleecommerceapijava.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/orders")
@SecurityRequirement(name = "Bearer Authentication")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    @PreAuthorize("#userId == principal.id")
    public List<Order> getAllOrders(@PathVariable String userId) {
        return orderService.getAllOrdersByUserId(userId);
    }
}
