package services;

import play.libs.ws.WSResponse;

import java.util.concurrent.CompletionStage;

/**
 * Interface for the Freelancer API requests
 * @author Aditi Aditi
 */
public interface FreelancerAPI {

    /**
     * Search for Projects given a keyword
     * @param keyword keyword to search for
     * @param limitValue to limit the total projects to be returned by API
     * @param offsetValue for allowing pagination of results if required
     * @return CompletionStage of a WSResponse. We do not apply any treatment to the response yet.
     * @author Aditi Aditi
     */
    public CompletionStage<WSResponse> search(String keyword, String limitValue, String offsetValue);

    /**
     * Search for Projects given a Skill ID
     * @param id is the Skill ID to search for
     * @return CompletionStage of a WSResponse. We do not apply any treatment to the response yet.
     * @author Aditi Aditi
     */
    public CompletionStage<WSResponse> searchSkill(String id);
}
