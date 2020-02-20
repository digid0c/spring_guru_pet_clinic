package guru.samples.petclinic.service.jpa;

import guru.samples.petclinic.model.Owner;
import guru.samples.petclinic.repository.OwnerRepository;
import guru.samples.petclinic.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Profile("jpa")
public class OwnerJpaService implements OwnerService {

    private final OwnerRepository ownerRepository;

    @Autowired
    public OwnerJpaService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Override
    public Owner findByLastName(String lastName) {
        return ownerRepository.findByLastName(lastName).orElse(null);
    }

    @Override
    public Set<Owner> findAll() {
        return StreamSupport.stream(ownerRepository.findAll().spliterator(), false)
                .collect(Collectors.toSet());
    }

    @Override
    public List<Owner> findAllByLastNameLike(String lastName) {
        return ownerRepository.findAllByLastNameLike("%" + lastName + "%");
    }

    @Override
    public Owner findById(Long id) {
        return ownerRepository.findById(id).orElse(null);
    }

    @Override
    public Owner save(Owner owner) {
        return ownerRepository.save(owner);
    }

    @Override
    public void delete(Owner owner) {
        ownerRepository.delete(owner);
    }

    @Override
    public void deleteById(Long id) {
        ownerRepository.deleteById(id);
    }
}
