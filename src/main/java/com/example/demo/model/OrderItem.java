package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    @Embeddable
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItemPrimaryKey implements Serializable {
        @ManyToOne
        private Order order;

        @ManyToOne
        private Medicine medicine;
    }

    @EmbeddedId
    private OrderItemPrimaryKey id;

    @Column(nullable = false)
    private Integer quantity;

    public record Projection(
            String medicineName,
            Integer quantity
    ){}
}
