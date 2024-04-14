package org.dmitriy.Restaurant.repositories;

import org.dmitriy.Restaurant.models.Reservation;
import org.dmitriy.Restaurant.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByTableId(int tableId);

    List<Reservation> findByDateOfReservBefore(LocalDateTime localDateTime);
}
