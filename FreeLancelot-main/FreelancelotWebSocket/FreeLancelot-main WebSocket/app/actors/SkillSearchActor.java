package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import model.SearchResult;
import play.libs.Json;
import services.FreeLancerService;

import java.time.LocalTime;
import java.util.List;

/**
 * SkillSearchActor
 * Implements the Freelancelot app using Skill Search Actor
 * @author Aditi Aditi
 */
public class SkillSearchActor extends AbstractActor {


    private final ActorRef out;

    private final ActorRef scheduler;

    private FreeLancerService freeLancerService;

    private String keyWord ; // Need to check


    /**
     * Creates a new Search Actor
     * @param out                        Actor to send project search service results
     * @param scheduler                  Scheduler actor
     * @param freeLancerService         Project search service
     * @author Aditi Aditi
     */
    public SkillSearchActor(ActorRef out, ActorRef scheduler, FreeLancerService freeLancerService) {
        this.out = out;
        this.scheduler = scheduler;
        this.freeLancerService = freeLancerService;
        System.out.println("scheduler Skill= "+ scheduler);
        this.scheduler.tell(new ProjectSearchSchedulerActor.Register(self()), self());
    }

    /**
     * Configures props to create Search Actor
     * @param out                        Actor to send tweets search service results to
     * @param scheduler                  Scheduler actor
     * @param freeLancerService
     * @return Newly created props
     * @author Aditi Aditi
     */
    public static Props props(ActorRef out, ActorRef scheduler, FreeLancerService freeLancerService) {
        return Props.create(SkillSearchActor.class, out, scheduler, freeLancerService);
    }

    /**
     * Logging during actor creation
     * @author Aditi Aditi
     */
    @Override
    public void preStart() {
        System.out.println("in PreStart");
        System.out.println("Project SkillSearchActor started at " + LocalTime.now());
    }


    /**
     * Logging during actor stopping
     * @author Aditi Aditi
     */
    @Override
    public void postStop() {
        System.out.println("in postStop");
        System.out.println("Project SKillSearchActor stopped at "+ LocalTime.now());
    }

    /**
     * A message to carry search phrase
     * @author Aditi Aditi
     */
    public static class Search {

        public String searchKey;

        public Search() {
            //System.out.println("Inside Search Constructor");
        }

        /**
         * Retrieves the search phrase
         * @return search phrase
         * @author Aditi Aditi
         */
        public String getSearchKey() {
            return searchKey;
        }

        /**
         * Sets the search phrase
         * @param searchKey search phrase
         * @author Aditi  Aditi
         */
        public void setSearchKey(String searchKey) {
            this.searchKey = searchKey;
        }
    }

    /**


     /**
     * Handles refresh and search messages
     * Search message - adds a search phrase to a list the Actor would query
     * Refresh message - triggers querying project search service
     * @author Aditi Aditi
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Refresh.class, newRefresh -> {
                    System.out.println("Skill actor refreshed:"+keyWord);
                    System.out.println("keyword for skill Search is: "+ keyWord);
                    List<SearchResult> reply = freeLancerService.projectsForSkill(keyWord);
                    out.tell(Json.toJson(reply), self());
                })
                .match(Search.class, newSearch -> {
                    keyWord= newSearch.getSearchKey();
                    System.out.println("Skill match Search.class keyWords = " + keyWord);
                    List<SearchResult> reply = freeLancerService.projectsForSkill(keyWord);
                    out.tell(Json.toJson(reply), self());
                })
                .build();
    }

    /**
     * A message to trigger refresh
     * @author Aditi Aditi
     */
    public static class Refresh {

        public Refresh() {
            //System.out.println("Inside Refresh Constructor");
        }
    }

}
