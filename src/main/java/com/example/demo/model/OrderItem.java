package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    @Embeddable
    @Getter
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
