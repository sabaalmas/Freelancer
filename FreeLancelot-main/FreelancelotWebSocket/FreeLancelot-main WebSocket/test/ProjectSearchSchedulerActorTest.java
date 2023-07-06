
import actors.ProjectSearchSchedulerActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.scalatest.junit.JUnitSuite;
import actors.ProjectSearchSchedulerActor;
import actors.ProjectSearchActor;
/**
 * Tests the functionality of ProjectSearchSchedulerActor.
 *
 * @author Shreyas Patel, Anurag Shekhar
 */
public class ProjectSearchSchedulerActorTest extends JUnitSuite {

    /**
     * Actors System
     */
    private static ActorSystem testSystem;

    /**
     * Initializes objects needed for tests before each unit test
     */
    @BeforeClass
    public static void setup() {
        testSystem = ActorSystem.create();
        testSystem.actorOf(ProjectSearchSchedulerActor.props());
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
     * Sends Register message to scheduler actor for registering a twitter search mock actor.
     * Then sends RefreshAll message from a mocked scheduling mechanism to the scheduler actor.
     * Then expects to receive Refresh message on the registered mock twitter actor sent from the scheduler actor.
     */
    @Test
    public void testProjectSearchSchedulerActorRegisterThenRefreshAll() {
        new TestKit(testSystem) {{

            final TestKit searchActorMock = new TestKit(testSystem);
            final TestKit schedulerMechanismMock = new TestKit(testSystem);

            final Props prop = Props.create(ProjectSearchSchedulerActor.class);

            ActorRef projectSearchSchedulerActorRef = testSystem.actorOf(prop);

            projectSearchSchedulerActorRef.tell(
                    new ProjectSearchSchedulerActor.Register(searchActorMock.getRef()),
                    searchActorMock.getRef());

            projectSearchSchedulerActorRef.tell(
                    new ProjectSearchSchedulerActor.RefreshAll(),
                    schedulerMechanismMock.getRef());

            searchActorMock.expectMsgClass(duration("5 second"), ProjectSearchActor.Refresh.class);
        }};
    }
}