import actors.ProjectSearchSchedulerActor;
import akka.actor.ActorRef;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.WebSocketController;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;

import org.junit.Assert;
import services.SchedulingService;
import model.ProjectInfo;
import model.SearchResult;
import org.apache.http.HttpStatus;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import play.Application;

import play.inject.guice.GuiceApplicationBuilder;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;
import services.FreeLancerService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;
import play.test.WithServer;
import play.shaded.ahc.org.asynchttpclient.AsyncHttpClient;
import play.shaded.ahc.org.asynchttpclient.DefaultAsyncHttpClient;
import play.shaded.ahc.org.asynchttpclient.ws.WebSocket;
import org.webjars.play.WebJarsUtil;
import util.MockObjectCreator;
import util.WebSocketClientTest;

import static org.mockito.ArgumentMatchers.anyString;


/**
 * Test class for WebSocketController
 * @author Shreyas Patel, Anurag Shekhar
 */
@RunWith(MockitoJUnitRunner.class)
public class WebSocketControllerTest extends WithServer {


    @Mock
    private static ActorSystem actorSystem;

    //private static ActorRef schedulerActorRef = system.actorOf(ProjectSearchSchedulerActor.props(), "Scheduler");
    @Mock
    private static ActorRef schedulerActorRef;
    @Mock
    private static Materializer materializer;

    @Mock
    private AsyncHttpClient asyncHttpClient;

    @Mock
    private static FreeLancerService freeLancerService;

    @Mock
    private static SchedulingService schedulingService;

    /**
     * Execution context that encapsulates inside a Fork/Join pool.
     * This is a real object and not a mock because it is used to run async operations
     */
    private HttpExecutionContext ec = new HttpExecutionContext(ForkJoinPool.commonPool());
    //WebSocketController webSocketController1 = new WebSocketController(freeLancerService,actorSystem,materializer,schedulingService);
    @InjectMocks
    private static WebSocketController webSocketController;
         //   = new WebSocketController(freeLancerService,actorSystem,materializer,schedulingService);
    /**
     * Initializes objects needed for tests before each unit test
     */
    @Before
    public void setUpHttpContext() {
        asyncHttpClient = new DefaultAsyncHttpClient();
        actorSystem = ActorSystem.create();
    }

    /**
     * Clears HttpContext to clear the session data
     *
     * @throws IOException TO handle failed I/O Exception
     */
    @After
    public void clearHttpContext() throws IOException {
        asyncHttpClient.close();
    }


    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    /** Unit test for testing the index endpoint for GET
     * @author Shreyas Patel, Anurag Shekhar
     */
    @Test
    public void indexTest() {
        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(Helpers.GET)
                .uri("/");

        Result result = route(app, request);
        // System.out.println(result.con);
        assertEquals(OK, result.status());
    }

//    /**
//     * Calls WebSocket using WebSocketTestClient with correct origin
//     * expects http request to be upgraded and WebSocket to be opened
//     *
//     * @throws Exception
//     */
//    @Test
//    public void websocket_success() throws Exception {
////        String serverURL = "ws://localhost:9000/websocket";
////        String origin = serverURL;
////
////        WebSocketClientTest webSocketTestClient = new WebSocketClientTest(asyncHttpClient);
////        WebSocket webSocket = webSocketTestClient.call(serverURL, origin).get();
//        WebSocket webSocket1 = (WebSocket) webSocketController.websocket();
//
//        Assert.assertTrue(webSocket1.isOpen());
//    }

    /**
     * Unit Test the webSocketController.fetch method
     * @author Shreyas Patel, Anurag Shekhar
     */
    @Test
    public void testFetchProjects(){
        running(provideApplication(),()->{
            List<String> keywords =new ArrayList<String>();
            keywords.add("java");
            when(freeLancerService.getOrQuery(keywords)).thenReturn(MockObjectCreator.createMockListSearchResult1(90));

            CompletionStage<Result> searchResults = webSocketController.fetch("java");
            try {
                Result result= searchResults.toCompletableFuture().get();
                assertEquals(HttpStatus.SC_OK,result.status());

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }



    /**
     * mock object for providing SearchResults
     * @author Shreyas Patel, Anurag Shekhar
     * @return List<SearchResult> represents the list of searches
     */
    private List<SearchResult> getProjectList() throws IOException {
        // InputStream inJson = ProjectInfo.class.getResourceAsStream("/resources/project.json");
        String projectJsonFile = getJsonFileAsString(File.separator + "test" + File.separator + "resources" +
                File.separator + "project.json");
        ProjectInfo projectInfo = new ObjectMapper().readValue(projectJsonFile, ProjectInfo.class);
        ProjectInfo projectWith1[]= new ProjectInfo[1];
        projectWith1[0]=projectInfo;
        SearchResult searchResult= new SearchResult();
        searchResult.setKeyword("Java");
        searchResult.setProjectInfo(Arrays.asList(projectWith1));
        List<SearchResult> listOfSearches = new ArrayList<>();
        listOfSearches.add(searchResult);
        /*Results.ok().sendResource("search.json");*/
        return  listOfSearches;
    }

    /**
     * Convert Json to String
     * @author Shreyas Patel, Anurag Shekhar
     * @param path of json file
     * @return Json as String
     */
    private String getJsonFileAsString(String path) throws IOException {
        String filePath = new File("").getAbsolutePath();
        byte[] encoded = Files.readAllBytes(Paths.get(filePath.concat(path)));
        return new String(encoded, "UTF-8");
    }

    /**
     * Unit Test the freelancerController.singleReadabilityScore
     * @author Anurag Shekhar
     */

    @Test
    public void testSingleReadabilityScore() throws ExecutionException, InterruptedException {

        //CompletionStage<Result> readabilityScore = webSocketController.singleReadabilityScore("Test ME!!");
        //Result result = readabilityScore.toCompletableFuture().get();
        Result result = new WebSocketController(freeLancerService,actorSystem,materializer,schedulingService).singleReadabilityScore("Test ME!!").toCompletableFuture().get();
        assertEquals(OK, result.status());
    }

    /**
     * Unit Test the freelancerController.averageFleschIndex
     * @author Anurag Shekhar
     */
    @Test
    public void testAverageFleschIndex() throws ExecutionException, InterruptedException {

//        CompletionStage<Result> avgReadabilityScore = webSocketController.averageFleschIndex("Test Keyword");
//        Result result = avgReadabilityScore.toCompletableFuture().get();
        Result result = new WebSocketController(freeLancerService,actorSystem,materializer,schedulingService).averageFleschIndex("Test ME!!").toCompletableFuture().get();
        assertEquals(OK, result.status());
    }

    /**
     * Unit Test the freelancerController.globalWordStatistics
     * @author Shreyas Patel
     */

    @Test
    public void testGlobalWordStatistics() throws ExecutionException, InterruptedException {
        //WebSocketController wsc = new WebSocketController(freeLancerService,actorSystem,materializer,schedulingService);
        Result result = new WebSocketController(freeLancerService,actorSystem,materializer,schedulingService).globalWordStatistics("").toCompletableFuture().get();
        assertEquals(OK, result.status());
    }



    /**
     * Unit Test the freelancerController.individualWordStatistics
     * @author Shreyas Patel
     */

    @Test
    public void testIndividualWordStatistics() throws ExecutionException, InterruptedException {

        Result result = new WebSocketController(freeLancerService,actorSystem,materializer,schedulingService).individualWordStatistics("Java","Using Eclipse, java script").toCompletableFuture().get();
        assertEquals(OK, result.status());
    }

    /**
     * Unit Test the freelancerController.userInformation
     * @author Almas Saba
     */

    @Test
    public void testUserInformation() throws ExecutionException, InterruptedException {
    Exception exception = null;
    Result result = null;
    try {
        result = new WebSocketController(freeLancerService, actorSystem, materializer, schedulingService).userInformation("61047855").toCompletableFuture().get();
    }catch(Exception e){
        exception = e;
    }
    assertTrue(exception.getMessage().contains("AskTimeoutException"));

    }
}
