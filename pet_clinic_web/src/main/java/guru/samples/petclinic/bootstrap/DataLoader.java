package guru.samples.petclinic.bootstrap;

import guru.samples.petclinic.model.*;
import guru.samples.petclinic.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static java.time.LocalDate.now;

@Slf4j
@Component
public class DataLoader implements CommandLineRunner {

    private final OwnerService ownerService;
    private final VetService vetService;
    private final PetTypeService petTypeService;
    private final SpecialityService specialityService;
    private final VisitService visitService;

    @Autowired
    public DataLoader(OwnerService ownerService, VetService vetService, PetTypeService petTypeService,
                      SpecialityService specialityService, VisitService visitService) {
        this.ownerService = ownerService;
        this.vetService = vetService;
        this.petTypeService = petTypeService;
        this.specialityService = specialityService;
        this.visitService = visitService;
    }

    @Override
    public void run(String... args) throws Exception {
        int count = petTypeService.findAll().size();
        if (count == 0) {
            loadData();
        }
    }

    private void loadData() {
        PetType dog = PetType.builder()
                .name("Dog")
                .build();
        PetType savedDogPetType = petTypeService.save(dog);

        PetType cat = PetType.builder()
                .name("Cat")
                .build();
        PetType savedCatPetType = petTypeService.save(cat);

        log.info("Loaded Pet Types...");

        Speciality radiology = Speciality.builder()
                .description("Radiology")
                .build();
        Speciality savedRadiology = specialityService.save(radiology);

        Speciality surgery = Speciality.builder()
                .description("Surgery")
                .build();
        Speciality savedSurgery = specialityService.save(surgery);

        Speciality dentistry = Speciality.builder()
                .description("Dentistry")
                .build();
        Speciality savedDentistry = specialityService.save(dentistry);

        log.info("Loaded Specialties...");

        Pet mikesDog = Pet.builder()
                .petType(savedDogPetType)
                .name("Rosco")
                .birthDate(now())
                .build();

        Owner owner1 = Owner.builder()
                .firstName("Michael")
                .lastName("Weston")
                .address("123 Brickerel")
                .city("Miami")
                .telephone("123456")
                .build()
                .addPet(mikesDog);

        ownerService.save(owner1);

        Pet fionasCat = Pet.builder()
                .petType(savedCatPetType)
                .name("Masya")
                .birthDate(now())
                .build();

        Owner owner2 = Owner.builder()
                .firstName("Fiona")
                .lastName("Glenanne")
                .address("456 Brickerel")
                .city("Los-Angeles")
                .telephone("654321")
                .build()
                .addPet(fionasCat);

        ownerService.save(owner2);

        log.info("Loaded Owners with Pets...");

        Visit catVisit = Visit.builder()
                .pet(fionasCat)
                .date(now())
                .description("Sneezy kitty")
                .build();

        visitService.save(catVisit);

        Visit dogVisit = Visit.builder()
                .pet(mikesDog)
                .date(now())
                .description("Poor dog")
                .build();

        visitService.save(dogVisit);

        log.info("Loaded Visits...");

        Vet vet1 = Vet.builder()
                .firstName("Sam")
                .lastName("Axe")
                .build()
                .addSpeciality(savedRadiology);

        vetService.save(vet1);

        Vet vet2 = Vet.builder()
                .firstName("Jessie")
                .lastName("Porter")
                .build()
                .addSpeciality(savedSurgery);

        vetService.save(vet2);

        log.info("Loaded Vets...");
    }
}
