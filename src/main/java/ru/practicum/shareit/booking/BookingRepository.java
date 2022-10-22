package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingRequestsState;

import java.util.List;
import java.util.Optional;

@Repository
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
    List<Booking> findByBookerIdAndState(Long bookerId, BookingRequestsState state,
                                         BookingRequestsState all,
                                         BookingRequestsState past,
                                         BookingRequestsState future,
                                         BookingRequestsState current,
                                         BookingRequestsState waiting,
                                         BookingRequestsState rejected);

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
    List<Booking> findByItemOwnerIdAndState(Long bookerId, BookingRequestsState state,
                                            BookingRequestsState all,
                                            BookingRequestsState past,
                                            BookingRequestsState future,
                                            BookingRequestsState current,
                                            BookingRequestsState waiting,
                                            BookingRequestsState rejected);

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
