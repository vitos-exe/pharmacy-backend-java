package com.example.demo.services;

import com.example.demo.model.Medicine;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.repository.MedicineRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.security.IdentityHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderService {
    OrderRepository orderRepository;
    MedicineRepository medicineRepository;
    IdentityHolder identityHolder;

    public OrderService(OrderRepository orderRepository, MedicineRepository medicineRepository, IdentityHolder identityHolder) {
        this.orderRepository = orderRepository;
        this.medicineRepository = medicineRepository;
        this.identityHolder = identityHolder;
    }

    public List<Order> getAllOrders(LocalDate startDate, LocalDate endDate) {
        if (startDate == null && endDate == null) {
            return orderRepository.findAll();
        } else if (startDate == null) {
            return orderRepository.findByCreatedLessThan(endDate);
        } else if (endDate == null) {
            return orderRepository.findByCreatedGreaterThan(startDate);
        } else {
            return orderRepository.findByCreatedBetween(startDate, endDate);
        }
    }

    public Order getOrderById(Long id){
        Order order = orderRepository.findById(id).orElseThrow();
        if (order.getUser().getId().equals(identityHolder.getIdentity().getId())){
            throw new SecurityException();
        }
        return order;
    }

    @Transactional
    public void changeOrderStatus(Long id){
        orderRepository.findById(id).ifPresentOrElse(
                order -> {
                    order.setStatus(order.getStatus().opposite());
                    orderRepository.save(order);
                },
                () -> {throw new NoSuchElementException();}
        );
    }

    @Transactional
    public Order createOrderFromItems(List<OrderItem.Projection> orderItemsProjections){
        Order newOrder = new Order();
        List<OrderItem> orderItems = orderItemsProjections.stream().map(item -> {
            Medicine medicine = medicineRepository.findByName(item.medicineName()).orElseThrow();
            if (medicine.getQuantity() < item.quantity()){
                throw new IllegalArgumentException();
            }
            medicine.setQuantity(medicine.getQuantity() - item.quantity());
            return new OrderItem(
                    new OrderItem.OrderItemPrimaryKey(newOrder, medicine),
                    item.quantity()
            );
        }).toList();
        newOrder.setUser(identityHolder.getIdentity());
        newOrder.setOrderItems(orderItems);
        return orderRepository.save(newOrder);
    }
}
