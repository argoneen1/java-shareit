package ru.practicum.shareit.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    Page<ItemRequest> findAllByRequesterId(Long requesterId, Pageable pageable);

    Page<ItemRequest> findAllByRequesterIdIsNot(Long requesterId, Pageable pageable);

}
