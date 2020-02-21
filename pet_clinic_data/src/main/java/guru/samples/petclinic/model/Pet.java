package guru.samples.petclinic.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pet")
public class Pet extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "birth_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private PetType petType;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pet")
    private Set<Visit> visits = new HashSet<>();

    @Builder
    public Pet(Long id, String name, LocalDate birthDate, PetType petType, Owner owner) {
        super(id);
        this.name = name;
        this.birthDate = birthDate;
        this.petType = petType;
        this.owner = owner;
    }

    public Pet addVisit(Visit visit) {
        visit.setPet(this);
        this.visits.add(visit);
        return this;
    }
}
