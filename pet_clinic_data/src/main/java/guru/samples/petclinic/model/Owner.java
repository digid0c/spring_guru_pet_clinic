package guru.samples.petclinic.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "owner")
public class Owner extends Person {

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "telephone")
    private String telephone;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private Set<Pet> pets = new HashSet<>();

    @Builder
    public Owner(Long id, String firstName, String lastName, String address, String city, String telephone) {
        super(id, firstName, lastName);
        this.address = address;
        this.city = city;
        this.telephone = telephone;
    }

    public Owner addPet(Pet pet) {
        pet.setOwner(this);
        this.pets.add(pet);
        return this;
    }

    public Optional<Pet> getPet(String petName) {
        return this.pets.stream()
                .filter(pet -> !pet.isNew())
                .filter(pet -> pet.getName().equalsIgnoreCase(petName))
                .findFirst();
    }
}
