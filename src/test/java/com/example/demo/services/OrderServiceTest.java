package com.example.demo.services;

import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.repository.MedicineRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.IdentityHolder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(OrderService.class)
class OrderServiceTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    MedicineRepository medicineRepository;
    @Autowired
    OrderService service;
    @MockBean
    IdentityHolder identityHolder;

    @Test
    void testGetAllOrders() {
        List<Order> orders = service.getAllOrders(null, null);
        assertEquals(2, orders.size());
        assertEquals(Order.Status.PENDING, orders.get(0).getStatus());
        assertEquals(2, orders.get(1).getOrderItems().size());
        assertEquals(5, orders.get(0).getOrderItems().get(0).getQuantity());
    }

    @Test
    void testGetOrderById_Success() {
        Mockito.when(identityHolder.getIdentity())
                .thenReturn(userRepository.findById(2L).get());

        Order order = service.getOrderById(1L);
        assertEquals(Order.Status.PENDING, order.getStatus());
        assertEquals(2, order.getOrderItems().size());
        assertEquals("Ibuprofen", order.getOrderItems().get(0).getId().getMedicine().getName());
    }

    @Test
    void testGetOrderById_SecurityException(){
        Mockito.when(identityHolder.getIdentity())
                .thenReturn(userRepository.findById(1L).get());
        assertThrows(SecurityException.class, () -> service.getOrderById(2L));
    }

    @Test
    void testGetOrderById_NoSuchElementException(){
        assertThrows(NoSuchElementException.class, () -> service.getOrderById(3L));
    }


    @Test
    void testChangeOrderStatus() {
        assertEquals(Order.Status.PENDING, orderRepository.findById(1L).get().getStatus());
        service.changeOrderStatus(1L);
        assertEquals(Order.Status.DELIVERED, orderRepository.findById(1L).get().getStatus());
    }

    @Test
    void testCreateOrderFromItems_Success() {
        Mockito.when(identityHolder.getIdentity())
                .thenReturn(userRepository.findById(1L).get());
        List<OrderItem.Projection> projections = List.of(
                new OrderItem.Projection("Ibuprofen", 3),
                new OrderItem.Projection("Nurofen", 2)
        );

        service.createOrderFromItems(projections);
        Optional<Order> newOrder = orderRepository.findById(3L);
        assertTrue(newOrder.isPresent());
        assertEquals(1L, newOrder.get().getUser().getId());
        assertEquals(2, newOrder.get().getOrderItems().size());
        assertEquals(7, medicineRepository.findByName("Ibuprofen").get().getQuantity());
    }

    @Test
    void testCreateOrderFromItems_Fail(){
        Mockito.when(identityHolder.getIdentity())
                .thenReturn(userRepository.findById(1L).get());
        List<OrderItem.Projection> projections = List.of(
                new OrderItem.Projection("Ibuprofen", 15)
        );

        assertThrows(IllegalArgumentException.class, () -> service.createOrderFromItems(projections));
    }
}