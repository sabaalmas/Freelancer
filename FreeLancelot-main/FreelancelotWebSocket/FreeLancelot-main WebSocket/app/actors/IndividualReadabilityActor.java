package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import services.FreeLancerService;
import javax.inject.Inject;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * IndividualReadabilityActor
 * Implements the Freelancelot app using IndividualReadabilityActor
 * @author Anurag Shekhar
 */
public class IndividualReadabilityActor extends AbstractActor {

    private FreeLancerService freeLancerService;

    static public Props props(FreeLancerService freeLancerService) {
        return Props.create(IndividualReadabilityActor.class, freeLancerService);
    }

    /**
     * Logging during actor creation
     * @author Anurag Shekhar
     */
    @Override
    public void preStart() {
        System.out.println("in PreStart");
        System.out.println("IndividualReadabilityActor started at " + LocalTime.now());
    }

//    /**
//     * Logging during actor stopping
//     * @author Anurag Shekhar
//     */
//    @Override
//    public void postStop() {
//        System.out.println("in postStop");
//        System.out.println("IndividualReadabilityActor stopped at "+ LocalTime.now());
//    }
    /**
     * Creates a new IndividualReadabilityActor
     * @param freeLancerService         Project search service
     * @author Anurag Shekhar
     */
    @Inject
    public IndividualReadabilityActor(FreeLancerService freeLancerService){
        this.freeLancerService = freeLancerService;
    }


    /**
     * Handles Individual Readability
     * previewDescription - previewDescription
     * @author Anurag Shekhar
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, description ->{
                    HashMap<String,String> readabilityScore = freeLancerService.readbilityScoreCalculate(description);
                    sender().tell(readabilityScore,self());
                })
                .build();
    }
}
