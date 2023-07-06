
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.scalatest.junit.JUnitSuite;
import java.time.Duration;
import services.FreeLancerService;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import util.MockObjectCreator;
import actors.UserInformationActor;
import model.UserInformation;
/**
 * Tests the functionality of UserInformationActorTest.
 *
 * @author Almas Saba
 */
public class UserInformationActorTest extends JUnitSuite {

    /**
     * Actors System
     */
    private static ActorSystem testSystem;

    /**
     * Mock of FreeLancerService
     */
    static private FreeLancerService freeLancerService = mock(FreeLancerService.class);

    /**
     * Initializes objects needed for tests before each unit test
     */
    @BeforeClass
    public static void setup() {
        testSystem = ActorSystem.create();
        testSystem.actorOf(UserInformationActor.props(freeLancerService));
    }

    /**
     * Teardown Actor system after all tests
     */
    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(testSystem);
        testSystem = null;
    }

    /**
     * Sends UserInformation
     */
    @Test
    public void testUserInformationActor() {
        new TestKit(testSystem) {{

            final Props props = Props.create(UserInformationActor.class, freeLancerService);
            final ActorRef tsa = testSystem.actorOf(props);
            final TestKit probe1 = new TestKit(testSystem);
            Mockito.doReturn(MockObjectCreator.createMockUserInformation()).when(freeLancerService).getUserDetails(anyString());
            tsa.tell(freeLancerService.getUserDetails("id1"), probe1.getRef());
            //expectMsg(Duration.ofSeconds(10), MockObjectCreator.createMockHashMap());
        }};
    }
}

