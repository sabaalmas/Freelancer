package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import services.FreeLancerService;
import javax.inject.Inject;
import java.time.LocalTime;
import java.util.HashMap;

/**
 * GlobalReadabilityActor
 * Implements the Freelancelot app using GlobalReadabilityActor
 * @author Anurag Shekhar
 */

public class GlobalReadabilityActor extends AbstractActor {

    private FreeLancerService freeLancerService;

    /**
     * Configures props to create GlobalReadabilityActor
     * @param freeLancerService
     * @return Newly created props
     * @author Anurag Shekhar
     */
    static public Props props(FreeLancerService freeLancerService) {
        return Props.create(GlobalReadabilityActor.class, freeLancerService);
    }

    /**
     * Logging during actor creation
     * @author Anurag Shekhar
     */
    @Override
    public void preStart() {
        System.out.println("in PreStart");
        System.out.println("GlobalReadabilityActor started at " + LocalTime.now());
    }

//    /**
//     * Logging during actor stopping
//     * @author Anurag Shekhar
//     */
//    @Override
//    public void postStop() {
//        System.out.println("in postStop");
//        System.out.println("GlobalReadabilityActor stopped at "+ LocalTime.now());
//    }

    /**
     * Creates a new GlobalReadabilityActor
     * @param freeLancerService         Project search service
     * @author Anurag Shekhar
     */
    @Inject
    public GlobalReadabilityActor(FreeLancerService freeLancerService){
        this.freeLancerService = freeLancerService;
    }

    /**
     * Handles Global Readability
     * keyword - searched keyword
     * @author Anurag Shekhar
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, keyword ->{
                    HashMap<String,String> readabilityResult = freeLancerService.averageScoreCalculate(keyword);
                    sender().tell(readabilityResult,self());
                })
                .build();
    }
}
