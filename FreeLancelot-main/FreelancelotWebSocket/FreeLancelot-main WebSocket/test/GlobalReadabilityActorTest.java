import actors.IndividualReadabilityActor;
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
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import actors.GlobalReadabilityActor;
import scala.compat.java8.FutureConverters;
import services.FreeLancerService;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import util.MockObjectCreator;

import javax.inject.Inject;

import static akka.pattern.Patterns.ask;

/**
 * Tests the functionality of GlobalReadabilityActorTest.
 *
 * @author Anurag Shekhar
 */
public class GlobalReadabilityActorTest extends JUnitSuite {

    /**
     * Actors System
     */
    private static ActorSystem testSystem;

    /**
     * Mock of FreeLancerService
     */

    //private FreeLancerService freeLancerService;
    @Inject
    static private FreeLancerService freeLancerService = mock(FreeLancerService.class);

    /**
     * Initializes objects needed for tests before each unit test
     */
    @BeforeClass
    public static void setup() {
        testSystem = ActorSystem.create();
        //testSystem.actorOf(GlobalReadabilityActor.props(freeLancerService));
    }

    /**
     * Teardown Actor system after all tests
     */
    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(testSystem);
    }

    /**
     * Sends global Readability
     */
    @Test
    public void testGlobalReadabilityActor() {
        final TestKit testProbe = new TestKit(testSystem);
        final ActorRef actorTest1 = testSystem.actorOf(GlobalReadabilityActor.props(freeLancerService));

        String keyword = "Java";
        List<ProjectInfo> projectInfoList = new ArrayList<>();
        projectInfoList.add(MockObjectCreator.createMockProjectInfo("23515703","Short video recorder and editor code like tiktok in Java","I need short video recorder and editor like tiktok in java, features are below Camera features \\nMu"));
        projectInfoList.add(MockObjectCreator.createMockProjectInfo("9039243","Need a Java Expert having Good Knowledge of RMI","Need done ASAP: Search and sort data related to song objects. The song data will be obtained from database."));

        //System.out.println(projectInfoList.toString());
        HashMap<String, String> expectedAverageReadabilityResult = new HashMap<String, String>();
        expectedAverageReadabilityResult.put("Flesch_Index","55.6317750257998" );
        expectedAverageReadabilityResult.put("FKGL", "11.701486068111455");


        CompletableFuture<Object> result =  FutureConverters.toJava(ask(actorTest1, "Python", Integer.MAX_VALUE)).toCompletableFuture();
        HashMap<String,String> actualResult = new HashMap<String,String>();
        try {
            actualResult = (HashMap<String,String>) result.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(actualResult.isEmpty());


//        new TestKit(testSystem) {{
//
//            final Props props = Props.create(GlobalReadabilityActor.class, freeLancerService);
//            final ActorRef tsa = testSystem.actorOf(props);
//            final TestKit probe1 = new TestKit(testSystem);
//            tsa.tell(MockObjectCreator.createMockHashMap(), probe1.getRef());
//            //expectMsg(Duration.ofSeconds(10), MockObjectCreator.createMockHashMap());
//        }};
    }
}