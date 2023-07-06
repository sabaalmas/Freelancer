package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import model.SearchResult;
import play.libs.Json;
import services.FreeLancerService;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
/**
 * ProjectSearchActor
 *Implements the Freelancelot app using ProjectSearchActor
 * @author Shreyas Patel, Anurag Shekhar
 */
public class ProjectSearchActor extends AbstractActor {


    private final ActorRef out;

    private final ActorRef scheduler;

    private FreeLancerService freeLancerService;

    public static ArrayList<String> keyWords = new ArrayList<>(); // Need to check


    /**
     * Creates a new Search Actor
     * @param out                        Actor to send project search service results
     * @param scheduler                  Scheduler actor
     * @param freeLancerService         Project search service
     */
    public ProjectSearchActor(ActorRef out, ActorRef scheduler, FreeLancerService freeLancerService) {
        this.out = out;
        this.scheduler = scheduler;
        this.freeLancerService = freeLancerService;
        System.out.println("scheduler = "+ scheduler);
        this.scheduler.tell(new ProjectSearchSchedulerActor.Register(self()), self());
    }

    /**
     * Configures props to create Search Actor
     * @param out                        Actor to send project search service results to
     * @param scheduler                  Scheduler actor
     * @param freeLancerService
     * @return Newly created props
     */
    public static Props props(ActorRef out, ActorRef scheduler, FreeLancerService freeLancerService) {
        return Props.create(ProjectSearchActor.class, out, scheduler, freeLancerService);
    }

    @Override
    public void preStart() {
        System.out.println("in PreStart");
        System.out.println("Project SearchSchedulerActor started at " + LocalTime.now());
    }


    @Override
    public void postStop() {
        System.out.println("in postStop");
        System.out.println("Project SearchSchedulerActor stopped at "+ LocalTime.now());
    }

    /**
     * A message to carry search phrase
     * @author Shreyas Patel, Anurag Shekhar
     */
    public static class Search {

        public String searchKey;

        public Search() {
            //System.out.println("Inside Search Constructor");
        }

        /**
         * Retrieves the search phrase
         * @return search phrase
         */
        public String getSearchKey() {
            return searchKey;
        }

        /**
         * Sets the search phrase
         * @param searchKey search phrase
         */
        public void setSearchKey(String searchKey) {
            this.searchKey = searchKey;
        }
    }

    /**
     * A message to trigger refresh
     * @author Shreyas Patel, Anurag Shekhar
     */
    public static class Refresh {

        public Refresh() {
            //System.out.println("Inside Refresh Constructor");
        }
    }

    /**
     * Handles refresh and search messages
     * Search message - adds a search phrase to a list the Actor would query
     * Refresh message - triggers querying project search service
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Refresh.class, newRefresh -> {
                    System.out.println("search actor refreshed:"+keyWords.toString());
                    List<SearchResult> reply = freeLancerService.getOrQuery(keyWords);
                    out.tell(Json.toJson(reply), self());
                })
                .match(Search.class, newSearch -> {
                    if(!keyWords.contains(newSearch.searchKey)){
                        keyWords.add(newSearch.searchKey);
                        System.out.println("match Search.class keyWords = " + keyWords.toString());
                        List<SearchResult> reply = freeLancerService.getOrQuery(keyWords);
                        out.tell(Json.toJson(reply), self());
                    }
                })
                .build();
    }

}
