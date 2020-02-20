package guru.samples.petclinic.controller;

import guru.samples.petclinic.model.Owner;
import guru.samples.petclinic.service.OwnerService;
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
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class OwnerControllerMockMVCTest {

    private static final Long FIRST_OWNER_ID = 1L;
    private static final Long SECOND_OWNER_ID = 2L;

    @Mock
    private OwnerService ownerService;

    @InjectMocks
    private OwnerController tested;

    private Set<Owner> owners;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        Owner owner1 = Owner.builder().id(FIRST_OWNER_ID).build();
        Owner owner2 = Owner.builder().id(SECOND_OWNER_ID).build();
        owners = Stream.of(owner1, owner2).collect(toSet());

        mockMvc = MockMvcBuilders.standaloneSetup(tested).build();
    }

    @Test
    public void shouldListOwners() throws Exception {
        when(ownerService.findAll()).thenReturn(owners);

        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/index"))
                .andExpect(model().attribute("owners", hasSize(2)));

        verify(ownerService).findAll();
    }

    @Test
    public void shouldFindOwners() throws Exception {
        mockMvc.perform(get("/owners/find"))
                .andExpect(status().isOk())
                .andExpect(view().name("not-implemented"));

        verifyNoInteractions(ownerService);
    }

    @Test
    public void shouldGetOwner() throws Exception {
        Owner owner = Owner.builder()
                .id(FIRST_OWNER_ID)
                .build();
        when(ownerService.findById(FIRST_OWNER_ID)).thenReturn(owner);

        mockMvc.perform(get("/owners/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownerDetails"))
                .andExpect(model().attributeExists("owner"))
                .andExpect(model().attribute("owner", hasProperty("id", is(FIRST_OWNER_ID))));

        verify(ownerService).findById(FIRST_OWNER_ID);
    }
}