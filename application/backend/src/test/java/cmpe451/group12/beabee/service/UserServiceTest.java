package cmpe451.group12.beabee.service;

import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.dto.UserDTO;
import cmpe451.group12.beabee.mapper.UserMapper;
import cmpe451.group12.beabee.model.Users;
import cmpe451.group12.beabee.repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Autowired
    @InjectMocks
    private UserService userService;

    @Mock
    private UserMapper userMapper;

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