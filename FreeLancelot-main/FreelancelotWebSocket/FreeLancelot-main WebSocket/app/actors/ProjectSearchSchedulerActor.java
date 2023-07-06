package actors;

import actors.ProjectSearchActor.Refresh;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

/**
 * ProjectSearchSchedulerActor
 *Implements the Freelancelot app using ProjectSearchSchedulerActor
 * @author Shreyas Patel, Anurag Shekhar
 */
public class ProjectSearchSchedulerActor extends AbstractActor {


    private final Set<ActorRef> projectSearchActors;

    public ProjectSearchSchedulerActor() {
        projectSearchActors = new HashSet<>();
    }

    /**
     * Configure props to create Search SchedulerActor
     */
    public static Props props() {
        return Props.create(ProjectSearchSchedulerActor.class);
    }

    /**
     * Logs the start time
     */
    @Override
    public void preStart() {
        System.out.println("In scheduler actor preStart");
        System.out.println("Project SearchSchedulerActor started at " + LocalTime.now());
    }

    /**
     * Logs the stop time of the
     */
    @Override
    public void postStop() {
        System.out.println("In scheduler actor postStop");
        System.out.println("Project SearchSchedulerActor stopped at "+ LocalTime.now());
    }

    /**
     * A message caries an Actor to register in ProjectSearchActor
     * @author Shreyas Patel, Anurag Shekhar
     */
    public static class Register {
        /**
         * Reference to the actor
         */
        public final ActorRef actorRef;

        /**
         * Creates a message
         * @param actorRef the reference to the Actor
         */
        public Register(ActorRef actorRef) {
            System.out.println("Inside Register Constructor");
            this.actorRef = actorRef;
        }
    }

    /**
     * A message to trigger refresh of all Actors
     * @author Shreyas Patel, Anurag Shekhar
     */
    public static class RefreshAll {
        /**
         * Creates a message
         */
        public RefreshAll() {
            //System.out.println("Inside RefreshAll Constructor");
        }
    }

    /**
     * Handles refresh and search messages
     * Register message - register the Actor in the list Actors to notify
     * RefreshAll message - sends Refresh message to all Actors in a list
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(RefreshAll.class, p -> {

                    for (ActorRef actorRef : projectSearchActors) {

                        actorRef.tell(new Refresh(), getSelf());
                        actorRef.tell(new SkillSearchActor.Refresh(),getSelf());

                    }
                })
                .match(Register.class, p -> {
                    projectSearchActors.add(p.actorRef);
                    System.out.println("new registerd Actor = " + p.actorRef);
                    System.out.println("actors = " + projectSearchActors);
                })
                .build();
    }

}
