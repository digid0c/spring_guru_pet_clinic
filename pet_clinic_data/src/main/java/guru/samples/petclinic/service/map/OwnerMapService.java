package guru.samples.petclinic.service.map;

import guru.samples.petclinic.model.Owner;
import guru.samples.petclinic.service.OwnerService;
import guru.samples.petclinic.service.PetService;
import guru.samples.petclinic.service.PetTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Profile({"map", "default"})
public class OwnerMapService extends AbstractMapService<Owner, Long> implements OwnerService {

    private final PetService petService;
    private final PetTypeService petTypeService;

    @Autowired
    public OwnerMapService(PetService petService, PetTypeService petTypeService) {
        this.petService = petService;
        this.petTypeService = petTypeService;
    }

    @Override
    public Set<Owner> findAll() {
        return super.findAll();
    }

    @Override
    public Owner findById(Long id) {
        return super.findById(id);
    }

    @Override
    public Owner save(Owner owner) {
        if (owner == null) {
            return null;
        }

        owner.getPets().forEach(pet -> {
            if (pet.getPetType() == null) {
                throw new RuntimeException("Pet type is required.");
            } else if (pet.getPetType().isNew()) {
                petTypeService.save(pet.getPetType());
            }

            if (pet.isNew()) {
                petService.save(pet);
            }
        });

        return super.save(owner);
    }

    @Override
    public void delete(Owner owner) {
        super.delete(owner);
    }

    @Override
    public void deleteById(Long id) {
        super.deleteById(id);
    }

    @Override
    public Owner findByLastName(String lastName) {
        return null;
    }
}
