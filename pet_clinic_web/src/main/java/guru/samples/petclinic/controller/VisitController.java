package guru.samples.petclinic.controller;

import guru.samples.petclinic.model.Pet;
import guru.samples.petclinic.model.Visit;
import guru.samples.petclinic.service.PetService;
import guru.samples.petclinic.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class VisitController {

    private static final String VISIT_CREATE_OR_UPDATE_FORM = "visits/createOrUpdateVisitForm";

    private final VisitService visitService;
    private final PetService petService;

    @Autowired
    public VisitController(VisitService visitService, PetService petService) {
        this.visitService = visitService;
        this.petService = petService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @ModelAttribute("visit")
    public Visit loadPetWithVisit(@PathVariable Long petId, Model model) {
        Pet pet = petService.findById(petId);
        model.addAttribute("pet", pet);

        Visit visit = new Visit();
        pet.addVisit(visit);
        return visit;
    }

    @GetMapping("/owners/*/pets/{petId}/visits/new")
    public String getCreateVisitForm(@PathVariable Long petId, Model model) {
        return VISIT_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/owners/{ownerId}/pets/{petId}/visits/new")
    public String createVisit(@Valid Visit visit, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return VISIT_CREATE_OR_UPDATE_FORM;
        } else {
            visitService.save(visit);
            return "redirect:/owners/{ownerId}";
        }
    }
}
