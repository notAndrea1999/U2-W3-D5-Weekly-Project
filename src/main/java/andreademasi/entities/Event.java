package andreademasi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "event_id")
    private long eventId;

    private String title;

    private String description;

    private LocalDate date;

    private String place;

    private long seats;

    private String img;

    @ManyToMany(mappedBy = "events")
    @JsonIgnore
    private Set<User> participants;


}
