package cmpe451.group12.beabee.service;

import cmpe451.group12.beabee.login.mapper.UserCredentialsMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

@ExtendWith(MockitoExtension.class)
public class UserCredentialsServiceTest {


    @Mock
    private UserCredentialsMapper userCredentialsMapper;

    @BeforeEach
    public void setUp() {

    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    void givenUserToAddShouldReturnProduct() {
        /*
        Users user = new Users();
        user.setUsername("valid_username");
        user.setPassword("valid_pass");
        user.setEmail("valid@mail.address");
        MessageResponse result = userService.addUser(user);
        Assert.assertEquals(new MessageResponse("User has been added successfully!", MessageType.SUCCESS), result );
*/
    }

    @Test
    void givenProductToAddShouldReturnAddedProduct() {
        /*
        MessageResponse result = userService.addUser(null);
        Assert.assertEquals(new MessageResponse("Couldn't add user!", MessageType.ERROR), result );
    */
    }


}