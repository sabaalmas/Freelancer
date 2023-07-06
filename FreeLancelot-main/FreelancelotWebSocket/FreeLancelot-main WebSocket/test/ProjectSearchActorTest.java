
import actors.ProjectSearchSchedulerActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.ProjectInfo;
import model.SearchResult;
import model.UserInformation;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.scalatest.junit.JUnitSuite;
import play.libs.Json;
import services.FreeLancerService;
import actors.ProjectSearchActor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests the functionality of ProjectSearchActor.
 *
 * @author Shreyas Patel, Anurag Shekhar
 */

public class ProjectSearchActorTest extends JUnitSuite {

    /**
     * Actors System
     */
    static ActorSystem system;

    /**
     * Mock of tFreeLancerService
     */
    static private FreeLancerService freeLancerService = mock(FreeLancerService.class);

    /**
     * A list of test projects
     */
    static private List<ProjectInfo> projects;

    /**
     * Initializes objects needed for tests before each unit test
     */
    @BeforeClass
    public static void setup() {
        List<SearchResult> searchResultList = new ArrayList<>();
        SearchResult searchResult = new SearchResult();
        searchResult.setKeyword("someKeyword");
        searchResult.setProjectInfo(projects);
        searchResultList.add(searchResult);
        when(freeLancerService.getOrQuery(any(List.class))).
                thenReturn(searchResultList);
        UserInformation user1 = new UserInformation();
        user1.setUsername("username1");
        user1.setId("userId1");
        UserInformation user2 = new UserInformation();
        user2.setUsername("username2");
        user2.setId("userId2");

        ProjectInfo projectInfo1 = new ProjectInfo();
        projectInfo1.setType("fixed");
        projectInfo1.setPreviewDescription("previewdescription1");
        projectInfo1.setTitle("title1");
        projectInfo1.setOwnerId("userId1");

        ProjectInfo projectInfo2 = new ProjectInfo();
        projectInfo2.setType("fixed");
        projectInfo2.setPreviewDescription("previewdescription2");
        projectInfo2.setTitle("title2");
        projectInfo2.setOwnerId("userId2");

        projects = new ArrayList<>();
        projects.add(projectInfo1);
        projects.add(projectInfo2);

        system = ActorSystem.create();
        ActorRef schedulerActorRef = system.actorOf(ProjectSearchSchedulerActor.props(), "Scheduler2");
        system.actorOf(ProjectSearchActor.props(null,null, freeLancerService));
    }

    /**
     * Teardown Actor system after all tests
     */
    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    /**
     * Sends Register message to ProjectSearchActor
     */
    @Test
    public void testActorRegister() {
        new TestKit(system) {{
            final Props props = Props.create(ProjectSearchActor.class, getRef(), getRef(), freeLancerService);
            system.actorOf(props);

            expectMsgClass(duration("5 second"), actors.ProjectSearchSchedulerActor.Register.class);
        }};
    }

    /**
     * Sends Search message to ProjectSearchActor
     */
    @Test
    public void testActorSearch() {
        new TestKit(system) {{
            ProjectSearchActor.Search search = new ProjectSearchActor.Search();
            search.setSearchKey("someKeyword");
            final TestKit probe1 = new TestKit(system);
            final TestKit probe2 = new TestKit(system);
            final Props props = Props.create(ProjectSearchActor.class, probe1.getRef(), probe2.getRef(), freeLancerService);
            final ActorRef tsa = system.actorOf(props);
            probe2.expectMsgClass(duration("5 second"), actors.ProjectSearchSchedulerActor.Register.class);
            tsa.tell(search, probe1.getRef());
            //JsonNode searchResult = Json.parse("{'keyword':'someKeyword'}");
            //probe1.expectMsg(duration("5 second"), searchResult);
        }};
    }

    /**
     * Sends Refresh message to ProjectSearchActor
     */
    @Test
    public void testActorRefresh() {
        new TestKit(system) {{
            ProjectSearchActor.Search search = new ProjectSearchActor.Search();
            search.setSearchKey("someKeyword");
            search.getSearchKey();
            final TestKit probe1 = new TestKit(system);
            final TestKit probe2 = new TestKit(system);
            final Props props = Props.create(ProjectSearchActor.class, probe1.getRef(), probe2.getRef(), freeLancerService);
            final ActorRef tsa = system.actorOf(props);
            //tsa.tell(search, probe1.getRef());
            tsa.tell(new ProjectSearchActor.Refresh(), probe1.getRef());
//            JsonNode searchResult = Json.parse("{\"Java Developer\":true}");
//            probe1.expectMsg(duration("3 second"), searchResult);
        }};
    }
}