package actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import services.FreeLancerService;

import javax.inject.Inject;
import java.time.LocalTime;
import java.util.LinkedHashMap;

/**
 * IndividualWordStatsActor
 * Implements the Freelancelot app using IndividualWordStatsActor
 * @author Shreyas Patel
 */
public class IndividualWordStatsActor extends AbstractActor {

    private FreeLancerService freeLancerService;

    static public Props props(FreeLancerService freeLancerService) {
        return Props.create(IndividualWordStatsActor.class, freeLancerService);
    }

    /**
     * Logging during actor creation
     * @author Shreyas Patel
     */
    @Override
    public void preStart() {
        System.out.println("in PreStart");
        System.out.println("IndividualWordStats started at " + LocalTime.now());
    }

//    /**
//     * Logging during actor stopping
//     * @author Shreyas Patel
//     */
//    @Override
//    public void postStop() {
//        System.out.println("in postStop");
//        System.out.println("IndividualWordStats stopped at "+ LocalTime.now());
//    }

    /**
     * Creates a new IndividualWordStatsActor
     * @param freeLancerService  Project search service
     * @author Shreyas Patel
     */
    @Inject
    public IndividualWordStatsActor(FreeLancerService freeLancerService){
        this.freeLancerService = freeLancerService;
    }


    /**
     * Handles Individual Word Stats
     * keyword - searched keyword
     * previewDescription - previewDescription
     * @author Shreyas Patel
     */
    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(String.class, desc ->{
                    LinkedHashMap<String,Integer> wordStats = freeLancerService.individualWordStatistics(desc);
                    sender().tell(wordStats,self());
                })
                .build();
    }
}
