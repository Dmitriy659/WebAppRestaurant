package org.dmitriy.Restaurant.services;

import jakarta.persistence.PrePersist;
import lombok.RequiredArgsConstructor;
import org.dmitriy.Restaurant.models.Reservation;
import org.dmitriy.Restaurant.models.User;
import org.dmitriy.Restaurant.repositories.ReservationRepository;
import org.dmitriy.Restaurant.repositories.UserRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    public void addReservation(Long userId, int tableId, LocalDateTime localDateTime) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            Reservation reservation = new Reservation();
            reservation.setTableId(tableId);
            reservation.setDateOfReserv(localDateTime);
            user.addReservation(reservation);
            userRepository.save(user);
        }
    }

    public List<LocalDateTime> freeTime(int tableId) {
        List<Reservation> reservations = reservationRepository.findAllByTableId(tableId);
        List<LocalDateTime> times = new ArrayList<>();

        for(Reservation i: reservations) {
            times.add(i.getDateOfReserv());
        }

        List<LocalDateTime> freeTime = new ArrayList<>();

        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime nextHour = currentDateTime.plusHours(1).withMinute(0).withSecond(0).withNano(0);
        while (nextHour.getHour() < 23) {
            if (!times.contains(nextHour)) {
                freeTime.add(nextHour);
            }
            nextHour = nextHour.plusHours(1);
        }

        LocalDate nextDay = LocalDate.now().plusDays(1);
        LocalDateTime nextDayStart = nextDay.atTime(10, 0);
        while (nextDayStart.getHour() < 23) {
            if (!times.contains(nextDayStart)) {
                freeTime.add(nextDayStart);
            }
            nextDayStart = nextDayStart.plusHours(1);
        }

        return freeTime;
    }

    public void deleteReservation(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null && user.getReservation() != null) {
            System.out.println("удаление");
            Reservation reservation = user.getReservation();
            user.setReservation(null);
            userRepository.save(user);
            reservationRepository.delete(reservation);
        }
    }

    @Scheduled(fixedRate = 3600000)
    public void deleteOldReservations() {
        List<Reservation> all = reservationRepository.findAll();
        for (Reservation i: all) {
            Long userId = i.getUser().getId();
            if (i.getDateOfReserv().isBefore(LocalDateTime.now())) {
                deleteReservation(userId);
            }
        }
    }

}
