package guru.samples.petclinic.service.map;

import guru.samples.petclinic.model.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class OwnerMapServiceUnitTest {

    private static final Long SAVED_OWNER_ID = 1L;
    private static final String SAVED_OWNER_LAST_NAME = "Gerrard";
    private static final Long NEW_OWNER_ID = 2L;

    private OwnerMapService tested;

    @BeforeEach
    public void setUp() {
        tested = new OwnerMapService(new PetMapService(), new PetTypeMapService());
        tested.save(Owner.builder().lastName(SAVED_OWNER_LAST_NAME).build());
    }

    @Test
    public void shouldFindAllOwners() {
        Set<Owner> owners = tested.findAll();

        assertThat(owners.size(), is(equalTo(1)));
    }

    @Test
    public void shouldFindOwnerById() {
        Owner owner = tested.findById(SAVED_OWNER_ID);

        assertThat(owner, is(notNullValue()));
        assertThat(owner.getId(), is(equalTo(SAVED_OWNER_ID)));
    }

    @Test
    public void shouldSaveOwnerWithExistingId() {
        Owner newOwner = Owner.builder().id(NEW_OWNER_ID).build();
        Owner saved = tested.save(newOwner);

        assertThat(saved, is(notNullValue()));
        assertThat(saved.getId(), is(equalTo(NEW_OWNER_ID)));
    }

    @Test
    public void shouldSaveOwnerWithoutId() {
        Owner newOwner = Owner.builder().build();
        Owner saved = tested.save(newOwner);

        assertThat(saved, is(notNullValue()));
        assertThat(saved.getId(), is(equalTo(NEW_OWNER_ID)));
    }

    @Test
    public void shouldDeleteOwner() {
        Owner owner = tested.findById(SAVED_OWNER_ID);
        tested.delete(owner);

        assertThat(tested.findAll().size(), is(equalTo(0)));
    }

    @Test
    public void shouldDeleteOwnerById() {
        tested.deleteById(SAVED_OWNER_ID);

        assertThat(tested.findAll().size(), is(equalTo(0)));
    }

    @Test
    public void shouldFindOwnerByLastName() {
        Owner owner = tested.findByLastName(SAVED_OWNER_LAST_NAME);

        assertThat(owner, is(notNullValue()));
        assertThat(owner.getId(), is(equalTo(SAVED_OWNER_ID)));
    }

    @Test
    public void shouldNotFindOwnerByLastName() {
        Owner owner = tested.findByLastName("Rooney");

        assertThat(owner, is(nullValue()));
    }
}