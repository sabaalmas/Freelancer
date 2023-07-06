package services;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import actors.ProjectSearchSchedulerActor;
import actors.ProjectSearchSchedulerActor.RefreshAll;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Scheduler;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

@Singleton
public class SchedulingService {

    /**
     * Akka's Actor System
     */
    private final ActorSystem actorSystem;


    @Inject
    public SchedulingService(ActorSystem actorSystem) {
        this.actorSystem = actorSystem;
    }

    /**
     * Implementation of startScheduler method
     * <p>
     * @param scheduler 				Scheduler object
     * @param schedulerActorRef 		scheduler actor
     */

    public void startScheduler(Scheduler scheduler, ActorRef schedulerActorRef) {
        FiniteDuration initialDelay = Duration.create(0, TimeUnit.SECONDS);
        FiniteDuration interval = Duration.create(10, TimeUnit.SECONDS);
        RefreshAll message = new RefreshAll();
        //SkillSearchSchedulerActor.RefreshAll messageForSkill = new SkillSearchSchedulerActor.RefreshAll();
        ExecutionContext executor = actorSystem.dispatcher();

            System.out.println("Control Reached Here for scheduling Project Refresh");
            scheduler.schedule(initialDelay, interval, schedulerActorRef, message, executor, ActorRef.noSender());

        /*else {
            System.out.println("Control Reached Here for scheduling Skill Refresh");
            scheduler.schedule(initialDelay, interval, schedulerActorRef, messageForSkill, executor, ActorRef.noSender());
        }*/

    }


}
