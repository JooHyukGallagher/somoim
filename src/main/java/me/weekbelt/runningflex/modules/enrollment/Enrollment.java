package me.weekbelt.runningflex.modules.enrollment;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.event.Event;

import javax.persistence.*;
import java.time.LocalDateTime;

@NamedEntityGraph(
        name = "Enrollment.withEventAndSociety",
        attributeNodes = {
                @NamedAttributeNode(value = "event", subgraph = "society")
        },
        subgraphs = @NamedSubgraph(name = "society", attributeNodes = @NamedAttributeNode("society"))
)
@NoArgsConstructor
@Getter @EqualsAndHashCode(of = "id")
@Entity
public class Enrollment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Event event;

    @ManyToOne
    private Account account;

    private LocalDateTime enrolledAt;

    private boolean accepted;

    private boolean attended;

    @Builder
    public Enrollment(Event event, Account account, LocalDateTime enrolledAt,
                      boolean accepted, boolean attended) {
        this.event = event;
        this.account = account;
        this.enrolledAt = enrolledAt;
        this.accepted = accepted;
        this.attended = attended;
    }

    public void addEvent(Event event) {
        this.event = event;
    }

    public void accepted() {
        this.accepted = true;
    }

    public void notAccepted() {
        this.accepted = false;
    }

    public void attended() {
        this.attended = true;
    }

    public void notAttended() {
        this.attended = false;
    }
}
