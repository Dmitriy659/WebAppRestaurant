package org.dmitriy.Restaurant.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int tableId;
    private LocalDateTime dateOfReserv;

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private User user;
}

