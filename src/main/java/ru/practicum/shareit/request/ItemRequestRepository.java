package ru.practicum.shareit.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByRequesterId(Long requesterId);

    @Query("select r from ItemRequest r where r.id <> :requesterId")
    Page<ItemRequest> findAllByNotRequesterIdWithPageable(Long requesterId, Pageable pageable);
}
