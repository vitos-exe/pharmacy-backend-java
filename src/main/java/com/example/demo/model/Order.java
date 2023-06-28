package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "\"order\"")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime created = LocalDateTime.now();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @OneToMany(mappedBy = "id.order", fetch = FetchType.EAGER)
    private List<OrderItem> orderItems;

    @ManyToOne
    private User user;

    public enum Status{
        PENDING,
        DELIVERED;

        public Status opposite(){
            return this.equals(PENDING) ? DELIVERED : PENDING;
        }
    }
}
