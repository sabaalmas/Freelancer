import model.ProjectInfo;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.routing.RoutingDsl;
import play.server.Server;
import services.FreeLancerService;
import services.FreelancerAPI;
import services.FreelancerAPIImplementation;

import java.util.List;
import java.util.concurrent.CompletionStage;
import static play.mvc.Results.ok;

/**
 * Mock implementation of the FreelancerAPI interface for Negative or Wrong Results
 * @author Aditi Aditi
 */
public class FreelancerTestNegativeAPIImplentation implements  FreelancerAPI{

    private WSClient ws;

    private Server server;

    private String baseUrl;

    private FreelancerAPIImplementation freelancerAPIImplementation;

    /**
     * Constructor
     * First, we setup a server that will return our static files for a search of projects
     * Then, we get a test instance of the WSClient, existing in Play
     * Then, we inject this instance in the real implementation: this way, the mock server
     * will respond instead of Freelancer, giving us the static files
     * Finally, we override the base URL to query the local server which responds on /projects/all
     * @author Aditi Aditi
     */

    public FreelancerTestNegativeAPIImplentation() {
        // Mock the Freelancer's API response
        server = Server.forRouter((components) -> RoutingDsl.fromComponents(components)
                .GET("/projects/all/").routingTo((request) -> ok().sendResource("searchBadJson.json")).build());

        System.out.println("Control was here");

        // Get test instance of WSClient
        ws = play.test.WSTestClient.newClient(server.httpPort());

        // Here we will use the "real" implementation but with the mock server created above
        // Therefore, we will achieve code coverage but not call the live Freelancer API!
        freelancerAPIImplementation = new FreelancerAPIImplementation(ws);
        freelancerAPIImplementation.setBaseUrl("/projects/all/");

    }

    /**
     * Test the search implementation
     * @param keyword keyword to search
     * @return CompletionStage of a WSResponse
     * @author Aditi Aditi
     */

    @Override
    public CompletionStage<WSResponse> search(String keyword, String limitValue, String offsetValue) {
        return freelancerAPIImplementation.search(keyword,limitValue,offsetValue);
    }

    /**
     * Test the searchSkill implementation
     * @param id Skill Id to search
     * @return CompletionStage of a WSResponse
     * @author Aditi Aditi
     */

    @Override
    public CompletionStage<WSResponse> searchSkill(String id) {
        return freelancerAPIImplementation.searchSkill(id);
    }
}