package org.springframework.samples.petclinic.owner;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.validation.constraints.Max;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerController;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for {@link OwnerController}
 *
 * @author Colin But
 */
@RunWith(SpringRunner.class)
@WebMvcTest(OwnerController.class)
public class OwnerControllerTests {

    private static final int TEST_OWNER_ID = 1;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OwnerRepository owners;

    private Owner george;

    @Before
    public void setup() {
        george = new Owner();
        george.setId(TEST_OWNER_ID);
        george.setFirstName("George");
        george.setLastName("Franklin");
        george.setAddress("110 W. Liberty St.");
        george.setCity("Madison");
        george.setTelephone("6085551023");
        given(this.owners.findById(TEST_OWNER_ID)).willReturn(george);
    }

    @Test
    public void testInitCreationForm() throws Exception {
        mockMvc.perform(get("/owners/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("owner"))
            .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    @Test
    public void testProcessCreationFormSuccess() throws Exception {
        mockMvc.perform(post("/owners/new")
            .param("firstName", "Joe")
            .param("lastName", "Bloggs")
            .param("address", "123 Caramel Street")
            .param("city", "London")
            .param("telephone", "01316761638")
            .param("email", "22@email.com")
            .param("password", "1212")
        )
            .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testProcessCreationFormHasErrors() throws Exception {
        mockMvc.perform(post("/owners/new")
            .param("firstName", "Joe")
            .param("lastName", "Bloggs")
            .param("city", "London")
        )
            .andExpect(status().isOk())
            .andExpect(model().attributeHasErrors("owner"))
            .andExpect(model().attributeHasFieldErrors("owner", "address"))
            .andExpect(model().attributeHasFieldErrors("owner", "telephone"))
            .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    @Test
    public void testInitFindForm() throws Exception {
        mockMvc.perform(get("/owners/find"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("owner"))
            .andExpect(view().name("owners/findOwners"));
    }

    @Test
    public void testProcessFindFormSuccess() throws Exception {
        given(this.owners.findByLastName("")).willReturn(Lists.newArrayList(george, new Owner()));
        mockMvc.perform(get("/owners"))
            .andExpect(status().isOk())
            .andExpect(view().name("owners/ownersList"));
    }

    @Test
    public void testProcessFindFormByLastName() throws Exception {
        given(this.owners.findByLastName(george.getLastName())).willReturn(Lists.newArrayList(george));
        mockMvc.perform(get("/owners")
            .param("lastName", "Franklin")
        )
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/owners/" + TEST_OWNER_ID));
    }

    @Test
    public void testProcessFindFormNoOwnersFound() throws Exception {
        mockMvc.perform(get("/owners")
            .param("lastName", "Unknown Surname")
        )
            .andExpect(status().isOk())
            .andExpect(model().attributeHasFieldErrors("owner", "lastName"))
            .andExpect(model().attributeHasFieldErrorCode("owner", "lastName", "notFound"))
            .andExpect(view().name("owners/findOwners"));
    }

    @Test
    public void testInitUpdateOwnerForm() throws Exception {
        mockMvc.perform(get("/owners/{ownerId}/edit", TEST_OWNER_ID))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("owner"))
            .andExpect(model().attribute("owner", hasProperty("lastName", is("Franklin"))))
            .andExpect(model().attribute("owner", hasProperty("firstName", is("George"))))
            .andExpect(model().attribute("owner", hasProperty("address", is("110 W. Liberty St."))))
            .andExpect(model().attribute("owner", hasProperty("city", is("Madison"))))
            .andExpect(model().attribute("owner", hasProperty("telephone", is("6085551023"))))
            .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    @Test
    public void testProcessUpdateOwnerFormSuccess() throws Exception {
        mockMvc.perform(post("/owners/{ownerId}/edit", TEST_OWNER_ID)
            .param("firstName", "Joe")
            .param("lastName", "Bloggs")
            .param("address", "123 Caramel Street")
            .param("city", "London")
            .param("telephone", "01616291589")
            .param("email", "22@email.com")
            .param("password", "1212")
        )
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/owners/{ownerId}"));
    }

    @Test
    public void testProcessUpdateOwnerFormHasErrors() throws Exception {
        mockMvc.perform(post("/owners/{ownerId}/edit", TEST_OWNER_ID)
            .param("firstName", "Joe")
            .param("lastName", "Bloggs")
            .param("city", "London")
        )
            .andExpect(status().isOk())
            .andExpect(model().attributeHasErrors("owner"))
            .andExpect(model().attributeHasFieldErrors("owner", "address"))
            .andExpect(model().attributeHasFieldErrors("owner", "telephone"))
            .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    @Test
    public void testShowOwner() throws Exception {
        mockMvc.perform(get("/owners/{ownerId}", TEST_OWNER_ID))
            .andExpect(status().isOk())
            .andExpect(model().attribute("owner", hasProperty("lastName", is("Franklin"))))
            .andExpect(model().attribute("owner", hasProperty("firstName", is("George"))))
            .andExpect(model().attribute("owner", hasProperty("address", is("110 W. Liberty St."))))
            .andExpect(model().attribute("owner", hasProperty("city", is("Madison"))))
            .andExpect(model().attribute("owner", hasProperty("telephone", is("6085551023"))))
            .andExpect(view().name("owners/ownerDetails"));
    }
    
    @Test
    public void 日本語入力テストエラー版() throws Exception{
    	mockMvc.perform(post("/owners/new")
//                .param("firstName", "Joe")
//                .param("lastName", "Bloggs")
//                .param("city", "London")
            )
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("owner"))
                .andExpect(model().attributeHasFieldErrors("owner", "firstName"))
                .andExpect(model().attributeHasFieldErrors("owner", "lastName"))
                .andExpect(model().attributeHasFieldErrors("owner", "city"))
                .andExpect(model().attributeHasFieldErrors("owner", "address"))
                .andExpect(model().attributeHasFieldErrors("owner", "telephone"))
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
        }
    
    @Test
    public void 日本語入力テスト() throws Exception{
        mockMvc.perform(post("/owners/new")
                .param("firstName", "太地")
                .param("lastName", "金子")
                .param("address", "千代田区神田須田町2-3-1 NBF神田須田町ビル6階")
                .param("city", "東京都")
                .param("telephone", "038876009")
                .param("email", "22@email.com")
                .param("password", "1212")
            )
                .andExpect(status().is3xxRedirection());
        }
    
    @Test
    public void Loginチェック() throws Exception{
    	mockMvc.perform(post("/login")
    			.param("email", "Franklin")
    			.param("password", "1234")
    			)
        .andExpect(model().attributeHasFieldErrors("owner", "email"))
        .andExpect(model().attributeHasFieldErrorCode("owner", "email", "loginerr"))
        .andExpect(view().name("/login"));
    }
    
    
    
    
}
