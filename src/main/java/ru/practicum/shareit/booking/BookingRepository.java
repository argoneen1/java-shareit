package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingRequestsState;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select b " +
            "from Booking b " +
            "where b.booker.id = :bookerId and " +
            "(:state = :all or " +
            "(:state = :past and b.end < CURRENT_TIMESTAMP ) or " +
            "(:state = :future and b.start > CURRENT_TIMESTAMP) or " +
            "(:state = :current and b.start < CURRENT_TIMESTAMP and b.end > CURRENT_TIMESTAMP) or " +
            "(:state = :waiting and b.status = ru.practicum.shareit.booking.model.Status.WAITING) or " +
            "(:state = :rejected and b.status = ru.practicum.shareit.booking.model.Status.REJECTED)" +
            ") order by b.end desc ")
    Page<Booking> findByBookerIdAndState(Long bookerId, BookingRequestsState state,
                                         BookingRequestsState all,
                                         BookingRequestsState past,
                                         BookingRequestsState future,
                                         BookingRequestsState current,
                                         BookingRequestsState waiting,
                                         BookingRequestsState rejected,
                                         Pageable pageable);

    @Query("select b " +
            "from Booking b " +
            "where b.item.owner.id = :bookerId and " +
            "(:state = :all or " +
            "(:state = :past and b.end < CURRENT_TIMESTAMP ) or " +
            "(:state = :future and b.start > CURRENT_TIMESTAMP) or " +
            "(:state = :current and b.start < CURRENT_TIMESTAMP and b.end > CURRENT_TIMESTAMP) or " +
            "(:state = :waiting and b.status = ru.practicum.shareit.booking.model.Status.WAITING) or " +
            "(:state = :rejected and b.status = ru.practicum.shareit.booking.model.Status.REJECTED)" +
            ") " +
            "order by b.end desc ")
    Page<Booking> findByItemOwnerIdAndState(Long bookerId, BookingRequestsState state,
                                            BookingRequestsState all,
                                            BookingRequestsState past,
                                            BookingRequestsState future,
                                            BookingRequestsState current,
                                            BookingRequestsState waiting,
                                            BookingRequestsState rejected,
                                            Pageable pageable);

    @Query("select b " +
            "from Booking b " +
            "where b.item.id = :itemId and " +
            "b.end < current_timestamp " +
            "order by b.end desc")
    Optional<Booking> findLast(Long itemId);

    @Query("select b " +
            "from Booking b " +
            "where b.item.id = :itemId and " +
            "b.start > current_timestamp " +
            "order by b.start asc")
    Optional<Booking> findNext(Long itemId);
}
