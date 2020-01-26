package guru.samples.petclinic.service.map;

import guru.samples.petclinic.model.Vet;
import guru.samples.petclinic.service.SpecialityService;
import guru.samples.petclinic.service.VetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class VetMapService extends AbstractMapService<Vet, Long> implements VetService {

    private final SpecialityService specialityService;

    @Autowired
    public VetMapService(SpecialityService specialityService) {
        this.specialityService = specialityService;
    }

    @Override
    public Set<Vet> findAll() {
        return super.findAll();
    }

    @Override
    public Vet findById(Long id) {
        return super.findById(id);
    }

    @Override
    public Vet save(Vet vet) {
        if (vet == null) {
            return null;
        }

        vet.getSpecialities().forEach(speciality -> {
            if (speciality.isNew()) {
                specialityService.save(speciality);
            }
        });

        return super.save(vet);
    }

    @Override
    public void delete(Vet vet) {
        super.delete(vet);
    }

    @Override
    public void deleteById(Long id) {
        super.deleteById(id);
    }
}
