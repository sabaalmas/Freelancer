import actors.GlobalReadabilityActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import model.ProjectInfo;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.scalatest.junit.JUnitSuite;

import actors.IndividualReadabilityActor;
import scala.compat.java8.FutureConverters;
import services.FreeLancerService;

import static akka.pattern.Patterns.ask;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import util.MockObjectCreator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Tests the functionality of IndividualReadabilityActor.
 *
 * @author Anurag Shekhar
 */
public class IndividualReadabilityActorTest extends JUnitSuite {

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
        testSystem.actorOf(IndividualReadabilityActor.props(freeLancerService));
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
     * Sends individual Readability
     */
    @Test
    public void testIndividualReadabilityActor() {
        //final TestKit testProbe = new TestKit(testSystem);
        final ActorRef actorTest = testSystem.actorOf(IndividualReadabilityActor.props(freeLancerService));

        String previewDescription = "Looking for someone with expertise in optimizing searches in large SQL databases to provide consulta";

        HashMap<String, String> actualReadabilityResult = new HashMap<String, String>();
        //actualReadabilityResult = freeLancerService.readbilityScoreCalculate(previewDescription);

        HashMap<String, String> expectedReadabilityResult = new HashMap<String, String>();
        expectedReadabilityResult.put("Flesch_Index", "30.30000000000001");
        expectedReadabilityResult.put("FKGL", "17.006666666666664");
        expectedReadabilityResult.put("Level", "College");


        CompletableFuture<Object> result =  FutureConverters.toJava(ask(actorTest, previewDescription, Integer. MAX_VALUE)).toCompletableFuture();

        try {
            actualReadabilityResult = (HashMap<String,String>) result.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(actualReadabilityResult.isEmpty());

//        new TestKit(testSystem) {{
//
//            final Props props = Props.create(IndividualReadabilityActor.class, freeLancerService);
//            final ActorRef tsa = testSystem.actorOf(props);
//            final TestKit probe1 = new TestKit(testSystem);
//            tsa.tell(MockObjectCreator.createMockHashMap(), probe1.getRef());
//        }};
    }
}
