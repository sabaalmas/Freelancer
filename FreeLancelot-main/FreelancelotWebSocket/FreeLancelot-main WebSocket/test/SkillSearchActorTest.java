import actors.ProjectSearchActor;
import actors.ProjectSearchSchedulerActor;
import actors.SkillSearchActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import model.ProjectInfo;
import model.SearchResult;
import model.UserInformation;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.scalatest.junit.JUnitSuite;
import services.FreeLancerService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests the functionality of SkillSearchActor.
 *
 * @author Aditi Aditi
 */

public class SkillSearchActorTest extends JUnitSuite {

    /**
     * Actors System
     * @author Aditi Aditi
     */
    static ActorSystem system;

    /**
     * Mock of tFreeLancerService
     * @author Aditi Aditi
     */
    static private FreeLancerService freeLancerService = mock(FreeLancerService.class);

    /**
     * A list of test projects
     * @author Aditi Aditi
     */
    static private List<ProjectInfo> projects;

    /**
     * Initializes objects needed for tests before each unit test
     * @author Aditi Aditi
     */
    @BeforeClass
    public static void setup() {
        List<SearchResult> searchResultList = new ArrayList<>();
        SearchResult searchResult = new SearchResult();
        searchResult.setKeyword("someKeyword");
        searchResult.setProjectInfo(projects);
        searchResultList.add(searchResult);
        when(freeLancerService.projectsForSkill(any(String.class))).
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
        ActorRef schedulerActorRef = system.actorOf(ProjectSearchSchedulerActor.props(), "Scheduler");
        system.actorOf(SkillSearchActor.props(null,schedulerActorRef, freeLancerService));
    }

    /**
     * Teardown Actor system after all tests
     * @Aditi Aditi
     */
    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    /**
     * Sends Register message to SchedulerActor
     * @Aditi Aditi
     */
    @Test
    public void testActorRegister() {
        new TestKit(system) {{
            final Props props = Props.create(SkillSearchActor.class, getRef(), getRef(), freeLancerService);
            system.actorOf(props);

            expectMsgClass(duration("5 second"), ProjectSearchSchedulerActor.Register.class);
        }};
    }

    /**
     * Sends Search message to SkillSearchActor
     * @Aditi Aditi
     */
    @Test
    public void testActorSearch() {
        new TestKit(system) {{
            SkillSearchActor.Search search = new SkillSearchActor.Search();
            search.setSearchKey("someKeyword");
            final TestKit probe1 = new TestKit(system);
            final TestKit probe2 = new TestKit(system);
            final Props props = Props.create(SkillSearchActor.class, probe1.getRef(), probe2.getRef(), freeLancerService);
            final ActorRef tsa = system.actorOf(props);
            probe2.expectMsgClass(duration("5 second"), ProjectSearchSchedulerActor.Register.class);
            tsa.tell(search, probe1.getRef());
            //JsonNode searchResult = Json.parse("{'keyword':'someKeyword'}");
            //probe1.expectMsg(duration("5 second"), searchResult);
        }};
    }

    /**
     * Sends Refresh message to SkillSearchActor
     * @Aditi Aditi
     */
    @Test
    public void testActorRefresh() {
       new TestKit(system) {{
            SkillSearchActor.Search search = new SkillSearchActor.Search();
            search.setSearchKey("someKeyword");
            search.getSearchKey();
            final TestKit probe1 = new TestKit(system);
            final TestKit probe2 = new TestKit(system);
            final Props props = Props.create(SkillSearchActor.class, probe1.getRef(), probe2.getRef(), freeLancerService);
            final ActorRef tsa = system.actorOf(props);
            probe2.expectMsgClass(duration("5 second"), ProjectSearchSchedulerActor.Register.class);
            //tsa.tell(search, probe1.getRef());
           //probe2.expectMsgClass(duration("5 second"), ProjectSearchSchedulerActor.Register.class);
            tsa.tell(new SkillSearchActor.Refresh(), probe1.getRef());

        }};
    }
}