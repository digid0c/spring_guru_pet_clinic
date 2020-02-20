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

import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
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

    private List<Owner> owners;
    private Owner owner;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        owner = Owner.builder().id(FIRST_OWNER_ID).build();
        Owner anotherOwner = Owner.builder().id(SECOND_OWNER_ID).build();
        owners = Stream.of(owner, anotherOwner).collect(toList());

        mockMvc = MockMvcBuilders.standaloneSetup(tested).build();
    }

    @Test
    public void shouldFindOwners() throws Exception {
        mockMvc.perform(get("/owners/find"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"))
                .andExpect(model().attributeExists("owner"));

        verifyNoInteractions(ownerService);
    }

    @Test
    public void shouldGetOwner() throws Exception {
        when(ownerService.findById(FIRST_OWNER_ID)).thenReturn(owner);

        mockMvc.perform(get("/owners/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownerDetails"))
                .andExpect(model().attributeExists("owner"))
                .andExpect(model().attribute("owner", hasProperty("id", is(FIRST_OWNER_ID))));

        verify(ownerService).findById(FIRST_OWNER_ID);
    }

    @Test
    public void shouldListManyOwners() throws Exception {
        when(ownerService.findAllByLastNameLike(anyString())).thenReturn(owners);

        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownersList"))
                .andExpect(model().attribute("selections", hasSize(2)));

        verify(ownerService).findAllByLastNameLike(anyString());
    }

    @Test
    public void shouldListOneOwner() throws Exception {
        when(ownerService.findAllByLastNameLike(anyString())).thenReturn(singletonList(owner));

        mockMvc.perform(get("/owners"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"))
                .andExpect(header().string("Location", "/owners/1"));

        verify(ownerService).findAllByLastNameLike(anyString());
    }

    @Test
    public void shouldNotFindAnyOwners() throws Exception {
        when(ownerService.findAllByLastNameLike(anyString())).thenReturn(emptyList());

        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"));

        verify(ownerService).findAllByLastNameLike(anyString());
    }
}