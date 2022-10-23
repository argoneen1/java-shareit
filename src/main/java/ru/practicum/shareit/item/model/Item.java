package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items", schema = "public")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    @Enumerated
    private Status status;

    @Column(name = "request_id")
    private Long request;

    @OneToMany(mappedBy = "item", cascade = CascadeType.PERSIST)
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.PERSIST)
    private List<Comment> comments = new ArrayList<>();

    public Item(
            Long id,
            String name,
            User owner,
            String description,
            Status status
    ) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id.equals(item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
