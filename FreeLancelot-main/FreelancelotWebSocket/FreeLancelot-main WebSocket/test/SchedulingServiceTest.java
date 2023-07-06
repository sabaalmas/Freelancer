import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;
import com.miguno.akka.testing.VirtualTime;

import com.typesafe.config.ConfigFactory;
import com.typesafe.config.Config;

import services.SchedulingService;

/**
 * Tests the functionality of Scheduling Service.
 * @author Shreyas Patel, Anurag Shekhar
 */
public class SchedulingServiceTest {

    static ActorSystem system;

    /**
     * Initializes objects needed for tests before each unit test
     */

    @BeforeClass
    public static void setup() {
        Config customConf = ConfigFactory.parseString("akka.actor.default-dispatcher { type=\"akka.testkit.CallingThreadDispatcherConfigurator\"}");
        system = ActorSystem.create("ActorSystem", ConfigFactory.load(customConf));
    }

    /**
     * Teardown function to allow the test case to do a preparation
     * and post clean up process for each of the test method call
     */

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    /**
     * Tests Scheduling Service start Scheduler method
     * Here we set the scheduler function to the virtual time
     * by advancing the duration
     * Scheduler Virtual time result is asserted using expectMsgClass
     */

    @Test
    public void whenStartingSchedulerThenSchedulerActorStartsReceivingRefreshAllMessages() {

        VirtualTime virtualTime = new VirtualTime();

        new TestKit(system) {{

            final TestKit probe = new TestKit(system);
            SchedulingService schedulingService = new SchedulingService(system);
            schedulingService.startScheduler(virtualTime.scheduler(), probe.getRef());
            virtualTime.advance(Duration.create(3, TimeUnit.SECONDS));

            probe.expectMsgClass(duration("3 second"), actors.ProjectSearchSchedulerActor.RefreshAll.class);
        }};
    }
}
