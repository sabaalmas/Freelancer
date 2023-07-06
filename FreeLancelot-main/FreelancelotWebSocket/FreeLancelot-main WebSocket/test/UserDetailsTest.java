import UserDetailsHelper.UserDetails;
import model.UserInformation;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import util.MockObjectCreator;

import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsTest {

    @Mock
    private static UserDetails userDetails;

    @Spy
    private UserDetails userDetailsSpy;


    /**
     * Test the userDetails.getUserDetails for userInformation
     *
     * @author Almas Saba
     */

    @Test
    public void testGetUserDetails() {
        UserDetails userDetailsSpy = Mockito.spy(new UserDetails());
        when(userDetailsSpy.getInformationFromConnection("https://www.freelancer.com/api/users/0.1/users/61316717?avatar=true&display_info=true&country_details=true&jobs=true&portfolio_details=true&preferred_details=true&profile_description=true&qualification_details=true&recommendations=true&responsiveness=true&status=true&&operating_areas=true&compact=true")).thenReturn(MockObjectCreator.createMockUserJSONObject());
        when(userDetailsSpy.getInformationFromConnection("https://www.freelancer.com/api/projects/0.1/projects/?owners[]=61316717&limit=10&job_details=true&compact=true&full_description=true")).thenReturn(MockObjectCreator.createMockProjectJSONOBject());

        UserInformation userinfo = userDetailsSpy.getUserDetails("61316717");
        UserInformation expected = new UserInformation();
        expected.setId("61316717");
        expected.setUsername("UmerAkhter");
        expected.setRegistrationDate("20012002");
        expected.setLimitedAccount("true");
        expected.setRole("employer");
        expected.setEmailVerified("verified");
        expected.setCountry("India");
        expected.setCompany("Test-company");
        Assert.assertEquals(expected.getUsername(), userinfo.getUsername());
    }
}
