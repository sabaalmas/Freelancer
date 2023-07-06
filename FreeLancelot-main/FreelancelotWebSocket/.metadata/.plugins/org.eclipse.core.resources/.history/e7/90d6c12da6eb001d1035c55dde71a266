package actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import services.FreeLancerService;

import javax.inject.Inject;

/**
 * UserInformationActor
 * Implements the Freelancelot app using UserInformationActor
 * @author Almas Saba
 */

public class UserInformationActor extends AbstractActor {

    private FreeLancerService freeLancerService;

    /**
     * Configures props to create UserInformationActor
     * @param freeLancerService
     * @return Newly created props
     * @author Almas Saba
     */
    static public Props props(FreeLancerService freeLancerService) {
        return Props.create(UserInformationActor.class, freeLancerService);
    }

    /**
     * Logging during actor creation
     * @author Alams Saba
     */
    @Override
    public void preStart() {
        System.out.println("New UserInformation Actor");
    }


    /**
     * Logging during actor stopping
     * @author Almas Saba
     */
    @Override
    public void postStop() {
        System.out.println("UserInformation actor Stopped ");
    }

    /**
     * Creates a new UserInformationActor
     * @param freeLancerService         Project search service
     * @author Almas Saba
     */
    @Inject
    public UserInformationActor(FreeLancerService freeLancerService){
        this.freeLancerService = freeLancerService;
    }

    /**
     * Handles User Information
     * ownerId - ownerId
     * @author Almas Saba
     */
    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(String.class, ownerID ->{
                    sender().tell(freeLancerService.getUserDetails(ownerID),self());
                })
                .build();
    }
}