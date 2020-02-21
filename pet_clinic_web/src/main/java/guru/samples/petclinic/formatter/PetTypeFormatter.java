package guru.samples.petclinic.formatter;

import guru.samples.petclinic.model.PetType;
import guru.samples.petclinic.service.PetTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class PetTypeFormatter implements Formatter<PetType> {

    private final PetTypeService petTypeService;

    @Autowired
    public PetTypeFormatter(PetTypeService petTypeService) {
        this.petTypeService = petTypeService;
    }

    @Override
    public PetType parse(String text, Locale locale) throws ParseException {
        return petTypeService.findAll().stream()
                .filter(petType -> petType.getName().equals(text))
                .findFirst()
                .orElseThrow(() -> new ParseException("type not found: " + text, 0));
    }

    @Override
    public String print(PetType petType, Locale locale) {
        return petType.getName();
    }
}
