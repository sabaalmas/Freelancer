import actors.GlobalReadabilityActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.scalatest.junit.JUnitSuite;

import actors.IndividualWordStatsActor;
import scala.compat.java8.FutureConverters;
import services.FreeLancerService;

import static akka.pattern.Patterns.ask;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import util.MockObjectCreator;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Tests the functionality of IndividualWordStatsActor.
 *
 * @author Shreyas Patel
 */
public class IndividualWordStatsActorTest extends JUnitSuite {

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
        testSystem.actorOf(IndividualWordStatsActor.props(freeLancerService));
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
     * Sends individual word stat
     */
    @Test
    public void testIndividualWordStatsActor() {
        //LinkedHashMap<String, Integer> result = freeLancerService.individualWordStatistics
        // ("Using Eclipse, java script, tomcat, MY SQL, ant. I have coding and schema. Need help displaying webp");
        final TestKit testProbe = new TestKit(testSystem);
        final ActorRef actorTest1 = testSystem.actorOf(IndividualWordStatsActor.props(freeLancerService));

        String description = "";
        CompletableFuture<Object> res =  FutureConverters.toJava(ask(actorTest1, description, Integer. MAX_VALUE)).toCompletableFuture();
        LinkedHashMap<String, Integer> result = null;
        try {
            result = (LinkedHashMap<String, Integer>) res.get();
            System.out.println(result.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(result.isEmpty());

        //        new TestKit(testSystem) {{
//
//            final Props props = Props.create(IndividualWordStatsActor.class, freeLancerService);
//            final ActorRef tsa = testSystem.actorOf(props);
//            final TestKit probe1 = new TestKit(testSystem);
//            tsa.tell(MockObjectCreator.createMockLinkedHashMap(), probe1.getRef());
//        }};
   }
}
