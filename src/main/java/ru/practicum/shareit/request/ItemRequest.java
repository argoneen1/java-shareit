package ru.practicum.shareit.request;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * TODO Sprint add-item-requests.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "item_requests")
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requester_id", referencedColumnName = "id", nullable = false)
    private User requester;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "created", nullable = false, updatable = false)
    private LocalDateTime created;

    @OneToMany(mappedBy = "request", cascade = CascadeType.PERSIST)
    private List<Item> items = new ArrayList<>();

    public ItemRequest(Long id, User requester, String description, LocalDateTime created) {
        this.id = id;
        this.requester = requester;
        this.description = description;
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemRequest that = (ItemRequest) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
