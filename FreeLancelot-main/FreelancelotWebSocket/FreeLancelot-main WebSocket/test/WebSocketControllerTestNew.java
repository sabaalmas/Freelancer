import actors.ProjectSearchSchedulerActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import controllers.WebSocketController;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import play.mvc.Http;
import play.mvc.Result;
import play.shaded.ahc.org.asynchttpclient.AsyncHttpClient;
import play.shaded.ahc.org.asynchttpclient.AsyncHttpClientConfig;
import play.shaded.ahc.org.asynchttpclient.DefaultAsyncHttpClient;
import play.shaded.ahc.org.asynchttpclient.DefaultAsyncHttpClientConfig;
import play.shaded.ahc.org.asynchttpclient.netty.ws.NettyWebSocket;

import static play.mvc.Http.HttpVerbs.GET;
import static play.mvc.Http.Status.OK;
import org.junit.Test;
import play.test.Helpers;
import play.test.TestServer;
import services.FreeLancerService;
import services.SchedulingService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;
import static org.awaitility.Awaitility.*;

/**
 * Class for testing Web Socket Controller
 * @Aditi Aditi
 */
@RunWith(MockitoJUnitRunner.class)
public class WebSocketControllerTestNew {


    @Mock
    static FreeLancerService freeLancerService;
    @Mock
    static SchedulingService schedulingService;
    @Mock
    static Materializer materializer;


    static ActorSystem actorSystem;


    static ActorRef scheduleRef;


    static WebSocketController ws;

    @BeforeClass
    public static void setUp(){
        actorSystem=ActorSystem.create();
        scheduleRef= actorSystem.actorOf(ProjectSearchSchedulerActor.props(), "SchedulerTest");
        freeLancerService= new FreeLancerService();
        schedulingService= new SchedulingService(actorSystem);

        ws= new WebSocketController(freeLancerService,actorSystem,materializer,schedulingService);


    }



    /**
     * Test for the accept WebSocket
     * @author Aditi Aditi
     */
    @Test
    public void testAcceptWebSocket() {
        TestServer server = testServer(19001);
        running(server, () -> {
            try {
                AsyncHttpClientConfig config = new DefaultAsyncHttpClientConfig.Builder().setMaxRequestRetry(0).build();
                AsyncHttpClient client = new DefaultAsyncHttpClient(config);
                WebSocketClient webSocketClient = new WebSocketClient(client);

                try {
                    String serverURL = "ws://localhost:19001/websocket";
                    WebSocketClient.LoggingListener listener = new WebSocketClient.LoggingListener(message -> {});
                    CompletableFuture<NettyWebSocket> completionStage = webSocketClient.call(serverURL, listener);
                    await().until(completionStage::isDone);
                    assertThat(completionStage)
                            .hasNotFailed();
                } finally {
                    client.close();
                }
            } catch (Exception e) {
                fail("Unexpected exception", e);
            }
        });
    }

    /**
     * Test for the accept WebSocketSkill
     * @author Aditi Aditi
     */
    @Test
    public void testAcceptWebSocketSkill() {
        TestServer server = testServer(3127);
        running(server, () -> {
            try {
                AsyncHttpClientConfig config = new DefaultAsyncHttpClientConfig.Builder().setMaxRequestRetry(0).build();
                AsyncHttpClient client = new DefaultAsyncHttpClient(config);
                WebSocketClient webSocketClient = new WebSocketClient(client);

                try {
                    String serverURL = "ws://localhost:3127/websocketSkill";
                    WebSocketClient.LoggingListener listener = new WebSocketClient.LoggingListener(message -> {});
                    CompletableFuture<NettyWebSocket> completionStage = webSocketClient.call(serverURL, listener);
                    await().until(completionStage::isDone);
                    assertThat(completionStage)
                            .hasNotFailed();
                } finally {
                    client.close();
                }
            } catch (Exception e) {
                fail("Unexpected exception", e);
            }
        });
    }

    /**
     * Test for the accept ListSkillProjects
     * @author Aditi Aditi
     */
    @Test
    public void testListSkillProjects() throws ExecutionException, InterruptedException {
        CompletionStage<Result> result= ws.listSkillProjects("Java","7");
        assertEquals(OK, result.toCompletableFuture().get().status());


    }

    /**
     * Test for the accept ListSkillProjects for C++
     * @author Aditi Aditi
     */
    @Test
    public void testListSkillProjectsForAnotherSkill() throws ExecutionException, InterruptedException {
        CompletionStage<Result> result= ws.listSkillProjects("C+++","7");
        assertEquals(OK, result.toCompletableFuture().get().status());


    }

    /**
     * Test for the accept FetchSkillProjects
     * @author Aditi Aditi
     */
    @Test
    public void testFetchSkillProjects() throws ExecutionException, InterruptedException {
        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(GET)
                .uri("/");
        CompletionStage<Result> result= ws.fetchProjectsForSkill( request.build(),"Java","7");

        assertEquals(SEE_OTHER, result.toCompletableFuture().get().status());


    }


}
