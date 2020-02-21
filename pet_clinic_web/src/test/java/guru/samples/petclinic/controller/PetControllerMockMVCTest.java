package guru.samples.petclinic.controller;

import guru.samples.petclinic.model.Owner;
import guru.samples.petclinic.model.Pet;
import guru.samples.petclinic.model.PetType;
import guru.samples.petclinic.service.OwnerService;
import guru.samples.petclinic.service.PetService;
import guru.samples.petclinic.service.PetTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PetControllerMockMVCTest {

    private static final Long OWNER_ID = 1L;
    private static final Long PET_ID = 1L;
    private static final String PET_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";

    @Mock
    private OwnerService ownerService;

    @Mock
    private PetService petService;

    @Mock
    private PetTypeService petTypeService;

    @InjectMocks
    private PetController tested;

    private MockMvc mockMvc;
    private Owner owner;
    private Pet pet;
    private Set<PetType> petTypes;

    @BeforeEach
    public void setUp() {
        owner = Owner.builder().id(OWNER_ID).build();
        pet = Pet.builder().id(PET_ID).build();

        petTypes = Stream.of(PetType.builder().id(1L).name("Dog").build(), PetType.builder().id(2L).name("Cat").build())
                .collect(toSet());
        mockMvc = MockMvcBuilders.standaloneSetup(tested).build();

        when(ownerService.findById(OWNER_ID)).thenReturn(owner);
        when(petTypeService.findAll()).thenReturn(petTypes);
    }

    @Test
    public void shouldGetCreatePetForm() throws Exception {
        mockMvc.perform(get("/owners/1/pets/new"))
                .andExpect(status().isOk())
                .andExpect(view().name(PET_CREATE_OR_UPDATE_FORM))
                .andExpect(model().attributeExists("pet"));

        verifyNoInteractions(petService);
    }

    @Test
    public void shouldCreatePet() throws Exception {
        when(petService.save(any())).thenReturn(pet);

        mockMvc.perform(post("/owners/1/pets/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"))
                .andExpect(header().string("Location", "/owners/1"));

        verify(petService).save(any());
    }

    @Test
    public void shouldGetUpdatePetForm() throws Exception {
        when(petService.findById(PET_ID)).thenReturn(pet);

        mockMvc.perform(get("/owners/1/pets/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name(PET_CREATE_OR_UPDATE_FORM))
                .andExpect(model().attributeExists("pet"))
                .andExpect(model().attribute("pet", hasProperty("id", is(PET_ID))));

        verify(petService).findById(PET_ID);
    }

    @Test
    public void shouldUpdatePet() throws Exception {
        when(petService.save(any())).thenReturn(pet);

        mockMvc.perform(post("/owners/1/pets/1/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"))
                .andExpect(header().string("Location", "/owners/1"));

        verify(petService).save(any());
    }
}
