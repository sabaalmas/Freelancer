import actors.IndividualWordStatsActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.scalatest.junit.JUnitSuite;

import actors.GlobalWordStatsActor;
import scala.compat.java8.FutureConverters;
import services.FreeLancerService;

import static akka.pattern.Patterns.ask;
import static org.mockito.Mockito.mock;
import util.MockObjectCreator;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Tests the functionality of GlobalWordStatsActorTest.
 *
 * @author Shreyas Patel
 */
public class GlobalWordStatsActorTest extends JUnitSuite {

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
        testSystem.actorOf(GlobalWordStatsActor.props(freeLancerService));
    }

    /**
     * Teardown Actor system after all tests
     */
    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(testSystem);
    }

    /**
     * Sends global word stats
     */
    @Test
    public void testGlobalWordStatsActor() {
        final TestKit testProbe = new TestKit(testSystem);
        final ActorRef actorTest1 = testSystem.actorOf(IndividualWordStatsActor.props(freeLancerService));

        String keyword = "";
        CompletableFuture<Object> res =  FutureConverters.toJava(ask(actorTest1, keyword, Integer.MAX_VALUE)).toCompletableFuture();
        LinkedHashMap<String, Integer> result = null;
        try {
            result = (LinkedHashMap<String, Integer>) res.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(result.isEmpty());

//        new TestKit(testSystem) {{
//
//            final Props props = Props.create(GlobalWordStatsActor.class, freeLancerService);
//            final ActorRef testActor = testSystem.actorOf(props);
//            final TestKit probe1 = new TestKit(testSystem);
//            testActor.tell(probe1.getRef(),getRef());
//            expectMsg(Duration.ofSeconds(1),"done");
//            //tsa.tell(MockObjectCreator.createMockLinkedHashMap(), probe1.getRef());
//
//        }};
    }
}

