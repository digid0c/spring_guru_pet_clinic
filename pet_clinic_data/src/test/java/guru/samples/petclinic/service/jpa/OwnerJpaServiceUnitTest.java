package guru.samples.petclinic.service.jpa;

import guru.samples.petclinic.model.Owner;
import guru.samples.petclinic.repository.OwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OwnerJpaServiceUnitTest {

    private static final Long FIRST_OWNER_ID = 1L;
    private static final String FIRST_OWNER_LAST_NAME = "Gerrard";
    private static final Long SECOND_OWNER_ID = 2L;
    private static final String SECOND_OWNER_LAST_NAME = "Rooney";

    private Owner firstOwner;
    private Owner secondOwner;

    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private OwnerJpaService tested;

    @BeforeEach
    public void setUp() {
        firstOwner = Owner.builder()
                .id(FIRST_OWNER_ID)
                .lastName(FIRST_OWNER_LAST_NAME)
                .build();
        secondOwner = Owner.builder()
                .id(SECOND_OWNER_ID)
                .lastName(SECOND_OWNER_LAST_NAME)
                .build();
    }

    @Test
    public void shouldFindAllOwners() {
        when(ownerRepository.findAll()).thenReturn(Stream.of(firstOwner, secondOwner).collect(Collectors.toSet()));

        Set<Owner> owners = tested.findAll();

        verify(ownerRepository).findAll();
        assertThat(owners.size(), is(equalTo(2)));
    }

    @Test
    public void shouldFindOwnerById() {
        when(ownerRepository.findById(FIRST_OWNER_ID)).thenReturn(Optional.of(firstOwner));

        Owner owner = tested.findById(FIRST_OWNER_ID);

        verify(ownerRepository).findById(eq(FIRST_OWNER_ID));
        assertThat(owner, is(notNullValue()));
        assertThat(owner.getId(), is(equalTo(FIRST_OWNER_ID)));
    }

    @Test
    public void shouldNotFindOwnerById() {
        when(ownerRepository.findById(SECOND_OWNER_ID)).thenReturn(Optional.empty());

        Owner owner = tested.findById(SECOND_OWNER_ID);

        verify(ownerRepository).findById(eq(SECOND_OWNER_ID));
        assertThat(owner, is(nullValue()));
    }

    @Test
    public void shouldSaveOwner() {
        when(ownerRepository.save(firstOwner)).thenReturn(firstOwner);

        Owner owner = tested.save(firstOwner);

        verify(ownerRepository).save(eq(firstOwner));
        assertThat(owner, is(notNullValue()));
        assertThat(owner.getId(), is(equalTo(FIRST_OWNER_ID)));
    }

    @Test
    public void shouldDeleteOwner() {
        tested.delete(firstOwner);

        verify(ownerRepository).delete(eq(firstOwner));
    }

    @Test
    public void shouldDeleteOwnerById() {
        tested.deleteById(FIRST_OWNER_ID);

        verify(ownerRepository).deleteById(eq(FIRST_OWNER_ID));
    }

    @Test
    public void shouldFindOwnerByLastName() {
        when(ownerRepository.findByLastName(FIRST_OWNER_LAST_NAME)).thenReturn(Optional.of(firstOwner));

        Owner owner = tested.findByLastName(FIRST_OWNER_LAST_NAME);

        verify(ownerRepository).findByLastName(eq(FIRST_OWNER_LAST_NAME));
        assertThat(owner, is(notNullValue()));
        assertThat(owner.getId(), is(equalTo(FIRST_OWNER_ID)));
    }

    @Test
    public void shouldNotFindOwnerByLastName() {
        when(ownerRepository.findByLastName(SECOND_OWNER_LAST_NAME)).thenReturn(Optional.empty());

        Owner owner = tested.findByLastName(SECOND_OWNER_LAST_NAME);

        verify(ownerRepository).findByLastName(eq(SECOND_OWNER_LAST_NAME));
        assertThat(owner, is(nullValue()));
    }
}