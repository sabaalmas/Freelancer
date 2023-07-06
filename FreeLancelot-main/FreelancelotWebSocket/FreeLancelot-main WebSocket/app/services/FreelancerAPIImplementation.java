package services;

import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * Implementation of the FreelancerAPI interface for the Freelancer API requests
 * @author Aditi Aditi
 */

public class FreelancerAPIImplementation  implements services.FreelancerAPI {
    private String baseUrl = "https://www.freelancer.com/api/projects/0.1/projects/all/";
    private WSClient ws;

    /**
     * Constructor
     * @param ws WSClient provided by Guice
     * @author Aditi Aditi
     */
    @Inject
    public FreelancerAPIImplementation(WSClient ws) {
        this.ws = ws;
    }

    /**
     * Setter for the baseUrl
     * This is useful for tests, and used in the mock implementation which, ironically,
     * uses part of the live implementation without ever querying Freelancer
     * @param baseUrl baseUrl. Default is Freelancer live URL as defined above.
     * @author Aditi Aditi
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    /**
     * Search for Projects given a keyword
     * @param keyword keyword to search for
     * @param limitValue to limit the total projects to be returned by API
     * @param offsetValue for allowing pagination of results if required
     * @return CompletionStage of a WSResponse. We do not apply any treatment to the response yet.
     * @author Aditi Aditi
     */
    public CompletionStage<WSResponse> search(String keyword, String limitValue, String offsetValue) {
//        System.out.println("hi aditi bye " + baseUrl);
        return ws.url(baseUrl)
                .addQueryParameter("query", keyword)
                .addQueryParameter("limit",limitValue)
                .addQueryParameter("offset", offsetValue)
                .addQueryParameter("job_details","true")
                .addQueryParameter("sort_field","time_updated")
                .addQueryParameter("project_statuses[]","active")
                .get(); // THIS IS NOT BLOCKING! It returns a promise to the response. It comes from WSRequest.
    }

    /**
     * Search for Projects given a Skill ID
     * @param id is the Skill ID to search for
     * @return CompletionStage of a WSResponse. We do not apply any treatment to the response yet.
     * @author Aditi Aditi
     */
    public CompletionStage<WSResponse> searchSkill(String id) {
        return ws.url(baseUrl)
                .addQueryParameter("jobs[]", id)
                .addQueryParameter("limit", "10")
                .addQueryParameter("job_details","true")
                .addQueryParameter("sort_field","time_updated")
                .addQueryParameter("project_statuses[]","active")
                .get(); // THIS IS NOT BLOCKING! It returns a promise to the response. It comes from WSRequest.
    }
}
