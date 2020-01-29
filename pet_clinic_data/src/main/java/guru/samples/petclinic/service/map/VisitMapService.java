package guru.samples.petclinic.service.map;

import guru.samples.petclinic.model.Visit;
import guru.samples.petclinic.service.VisitService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Profile({"map", "default"})
public class VisitMapService extends AbstractMapService<Visit, Long> implements VisitService {

    @Override
    public Set<Visit> findAll() {
        return super.findAll();
    }

    @Override
    public Visit findById(Long id) {
        return super.findById(id);
    }

    @Override
    public Visit save(Visit visit) {
        if (visit == null) {
            return null;
        } else if (visit.getPet() == null || visit.getPet().isNew()
                || visit.getPet().getOwner() == null || visit.getPet().getOwner().isNew()) {
            throw new RuntimeException("Pet and pet owner are required.");
        }
        return super.save(visit);
    }

    @Override
    public void delete(Visit visit) {
        super.delete(visit);
    }

    @Override
    public void deleteById(Long id) {
        super.deleteById(id);
    }
}
