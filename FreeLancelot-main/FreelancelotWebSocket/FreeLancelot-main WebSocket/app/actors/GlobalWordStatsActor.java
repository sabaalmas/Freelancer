package actors;
import akka.actor.AbstractActor;
import akka.actor.Props;
import services.FreeLancerService;
import javax.inject.Inject;
import java.time.LocalTime;
import java.util.LinkedHashMap;

/**
 * GlobalWordStats
 * Implements the Freelancelot app using GlobalWordStatsActor
 * @author Shreyas Patel
 */
public class GlobalWordStatsActor extends AbstractActor {

    private FreeLancerService freeLancerService;

    /**
     * Configures props to create GlobalWordStatsActor
     * @param freeLancerService
     * @return Newly created props
     * @author Shreyas Patel
     */
    static public Props props(FreeLancerService freeLancerService) {
        return Props.create(GlobalWordStatsActor.class, freeLancerService);
    }

    /**
     * Logging during actor creation
     * @author Shreyas Patel
     */
    @Override
    public void preStart() {
        System.out.println("in PreStart");
        System.out.println("GlobalWordStats started at " + LocalTime.now());
    }


//    /**
//     * Logging during actor stopping
//     * @author Shreyas Patel
//     */
//    @Override
//    public void postStop() {
//        System.out.println("in postStop");
//        System.out.println("GlobalWordStats stopped at "+ LocalTime.now());
//    }


    /**
     * Creates a new GlobalWordStatsActor
     * @param freeLancerService         Project search service
     * @author Shreyas Patel
     */
    @Inject
    public GlobalWordStatsActor(FreeLancerService freeLancerService){
        this.freeLancerService = freeLancerService;
    }

    /**
     * Handles Global WordStats
     * keyword - searched keyword
     * @author Shreyas Patel
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, searchKeyword ->{
                    LinkedHashMap<String,Integer> wordStats = freeLancerService.globalWordStatistics(searchKeyword);
                    sender().tell(wordStats,self());
                })
                .build();
    }
}
