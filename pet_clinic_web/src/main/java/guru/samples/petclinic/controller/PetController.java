package guru.samples.petclinic.controller;

import guru.samples.petclinic.model.Owner;
import guru.samples.petclinic.model.Pet;
import guru.samples.petclinic.model.PetType;
import guru.samples.petclinic.service.OwnerService;
import guru.samples.petclinic.service.PetService;
import guru.samples.petclinic.service.PetTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

import static java.lang.String.format;
import static org.springframework.util.StringUtils.hasLength;

@Controller
@RequestMapping("/owners/{ownerId}")
public class PetController {

    private static final String PET_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";

    private final OwnerService ownerService;
    private final PetService petService;
    private final PetTypeService petTypeService;

    @Autowired
    public PetController(OwnerService ownerService, PetService petService, PetTypeService petTypeService) {
        this.ownerService = ownerService;
        this.petService = petService;
        this.petTypeService = petTypeService;
    }

    @InitBinder("owner")
    public void initOwnerBinder(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @ModelAttribute("owner")
    public Owner findOwner(@PathVariable Long ownerId) {
        return ownerService.findById(ownerId);
    }

    @ModelAttribute("types")
    public Collection<PetType> populatePetTypes() {
        return petTypeService.findAll();
    }

    @GetMapping("/pets/new")
    public String getCreatePetForm(Owner owner, Model model) {
        Pet pet = new Pet();
        owner.addPet(pet);
        model.addAttribute("pet", pet);
        return PET_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/pets/new")
    public String createPet(Owner owner, @Valid Pet pet, BindingResult bindingResult, Model model) {
        if (hasLength(pet.getName()) && pet.isNew() && owner.getPet(pet.getName()).isPresent()){
            bindingResult.rejectValue("name", "duplicate", "already exists");
        }

        owner.addPet(pet);

        if (bindingResult.hasErrors()) {
            model.addAttribute("pet", pet);
            return PET_CREATE_OR_UPDATE_FORM;
        } else {
            petService.save(pet);
            return format("redirect:/owners/%d", owner.getId());
        }
    }

    @GetMapping("/pets/{petId}/edit")
    public String getUpdatePetForm(@PathVariable Long petId, Model model) {
        model.addAttribute("pet", petService.findById(petId));
        return PET_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/pets/{petId}/edit")
    public String updatePet(@Valid Pet pet, BindingResult bindingResult, Owner owner, Model model) {
        if (bindingResult.hasErrors()) {
            pet.setOwner(owner);
            model.addAttribute("pet", pet);
            return PET_CREATE_OR_UPDATE_FORM;
        } else {
            owner.addPet(pet);
            petService.save(pet);
            return format("redirect:/owners/%d", owner.getId());
        }
    }
}
