package controllers;

import actors.*;
import model.SearchResult;
import model.UserInformation;
import play.mvc.*;

import javax.inject.Singleton;
import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import play.libs.streams.ActorFlow;
import akka.stream.javadsl.Flow;
import play.libs.F.Either;
import play.mvc.Controller;
import scala.compat.java8.FutureConverters;
import services.FreeLancerService;
import services.SchedulingService;

import javax.inject.Inject;
import java.util.*;
//import java.util.HashMap;
//import java.util.List;
//import java.util.LinkedHashMap;
//import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.regex.Pattern;
import static akka.pattern.Patterns.ask;

/**
* WebSocketController
*Implements the FreelanceIot app using Freelancer's API
* @author Shreyas Patel, Anurag Shekhar
*/

@Singleton
public class WebSocketController extends Controller {

    private final ActorSystem actorSystem;
//    private final ActorRef averageFleschIndexActor;
    private final Materializer materializer;
    private FreeLancerService freeLancerService;
    private SchedulingService schedulingService;
    private ActorRef schedulerActorRef;
    private ActorRef schedulerActorRefSkill;
//    private AsyncCacheApi cache;

    /**
     * Creates a new Responsive Application Controller
     *
     * @param freeLancerService          Freelancer search service
     * @param actorSystem                Actors System
     * @param materializer               Materializer
     * @param schedulingService          Scheduling service
     * @author Shreyas Patel, Anurag Shekhar
     */
    @Inject
    public WebSocketController(
            FreeLancerService freeLancerService,
            ActorSystem actorSystem,
            Materializer materializer,
            SchedulingService schedulingService
            ) {

        this.freeLancerService = freeLancerService;
        this.actorSystem = actorSystem;
        this.materializer = materializer;
        this.schedulingService = schedulingService;

        schedulerActorRef = actorSystem.actorOf(ProjectSearchSchedulerActor.props(), "Scheduler"); // Scheduler Part.
        //schedulerActorRefSkill= actorSystem.actorOf(SkillSearchSchedulerActor.props(),"SkillScheduler");
        schedulingService.startScheduler(actorSystem.scheduler(), schedulerActorRef);
        //schedulingService.startScheduler(actorSystem.scheduler(), schedulerActorRefSkill, true);
       // this.averageFleschIndexActor = actorSystem.actorOf(GlobalReadabilityActor.props(this.freeLancerService));
    }


    public CompletableFuture<Result> fetch(String keyword) {
        List<String> keywords =new ArrayList<String>();
        keywords.add(keyword);
        List<SearchResult> filledProjects =null;
        filledProjects= freeLancerService.getOrQuery(keywords);
        System.out.println("Filled Project count :"+ filledProjects.size());
        return CompletableFuture.completedFuture(ok());

    }

    /**
     * Renders the initial search page at the beginning
     * @return CompletableFuture Result containing index html
     * @author Aditi Aditi
     */
    public CompletableFuture<Result> fetchempty(Http.Request request) {  // changed from CompleteableFuture
        System.out.println("Empty Call");
        String url = routes.WebSocketController.websocket().webSocketURL(request);
        ProjectSearchActor.keyWords.clear();
        freeLancerService.clearResults();
        return CompletableFuture.completedFuture(ok(views.html.index.render(null)));
    }

    /**
     * Creates WebSocket
     * @return a connection upgraded to a websocket
     */
    public WebSocket websocket() {

        System.out.println("Inside WebSocket function");
        return WebSocket.json(ProjectSearchActor.Search.class).acceptOrResult(request -> {
                final CompletionStage<Either<Result, Flow<ProjectSearchActor.Search, Object, ?>>> stage =
                        CompletableFuture.supplyAsync(() -> {

                            Object flowAsObject = ActorFlow.actorRef(out ->
                                            ProjectSearchActor.props(out, schedulerActorRef, freeLancerService),
                                    actorSystem, materializer);

                            @SuppressWarnings("unchecked")
                            Flow<ProjectSearchActor.Search, Object, NotUsed> flow =
                                    (Flow<ProjectSearchActor.Search, Object, NotUsed>) flowAsObject;

                            final Either<Result, Flow<ProjectSearchActor.Search, Object, ?>> right = Either.Right(flow);
                            return right;
                        });
                return stage;

        });
    }

    /**
     * Creates WebSocket for Skill Search
     * @return a connection upgraded to a websocket
     * @author Aditi Aditi
     */
    public WebSocket websocketSkill() {

        System.out.println("Inside WebSocket Skill function");
        return WebSocket.json(SkillSearchActor.Search.class).acceptOrResult(request -> {
            final CompletionStage<Either<Result, Flow<SkillSearchActor.Search, Object, ?>>> stage =
                    CompletableFuture.supplyAsync(() -> {

                        Object flowAsObject = ActorFlow.actorRef(out ->
                                        SkillSearchActor.props(out, schedulerActorRef, freeLancerService),
                                actorSystem, materializer);

                        @SuppressWarnings("unchecked")
                        Flow<SkillSearchActor.Search, Object, NotUsed> flow =
                                (Flow<SkillSearchActor.Search, Object, NotUsed>) flowAsObject;

                        final Either<Result, Flow<SkillSearchActor.Search, Object, ?>> right = Either.Right(flow);
                        return right;
                    });
            return stage;

        });
    }


    /**
     * Redirects to the page for display of projects with Projects fetched for the Skill
     * @param request Http request
     * @param id skill Id
     * @param skillname Name of the skill or Job
     * @return CompletableFuture Result containing listResults html
     * @author Aditi Aditi
     */

    public CompletableFuture<Result> fetchProjectsForSkill(Http.Request request, String skillname,String id){
        System.out.println("Call is coming to Skill backend fetch");
        System.out.println("Keyword for Skill Search is "+ id);
        String url = routes.WebSocketController.websocketSkill().webSocketURL(request);

        return CompletableFuture.completedFuture(redirect(routes.WebSocketController.listSkillProjects(skillname,id)));
    }

    /**
     * Renders the display of project (maximum 10)  for the skill
     * @param skillId  Id of skill being searched
     * @param skillname Name of the skill clicked on
     * @return CompletableFuture Result containing listResultsForSkill html
     * @author Aditi Aditi
     */
    public CompletableFuture<Result> listSkillProjects(String skillname,String skillId) {
        System.out.println("Came to List Skill All");
        System.out.println("Skill Name is " + skillname);
        String skillNameReplaced= skillname;
        if(!skillname.contains("C+++")) {
            skillNameReplaced=skillname.replaceAll(Pattern.quote("+"), " ");
            System.out.println(skillname + "After Replace");
        }
        else
            skillNameReplaced=skillname.replace("C+++","C++ ");
        return CompletableFuture.completedFuture(ok(views.html.listResultsForSkillWebSockets.render(skillNameReplaced,skillId)));

    }

    /**
     * Redirects to the page for display of word statistics of all preview descriptions returned in a project
     * @param searchedKeywords Search Keyword
     * @return CompletableFuture Result containing html for word statistics of all preview descriptions
     * @author Shreyas Patel
     */
    public CompletableFuture<Result> globalWordStatistics(String searchedKeywords) {
        System.out.println("Came to globalWordStatistics: "+searchedKeywords);
       // return CompletableFuture.completedFuture(ok(views.html.wordStatistics.render(freeLancerService.globalWordStatistics(searchedKeywords),searchedKeywords,"",true)));
        ActorRef wordStats =  actorSystem.actorOf(GlobalWordStatsActor.props(freeLancerService));
        return FutureConverters.toJava(ask(wordStats, searchedKeywords, Integer. MAX_VALUE))
                .thenApply(response -> ok(views.html.wordStatistics.render((LinkedHashMap<String,Integer>)response,searchedKeywords,"",true))).toCompletableFuture();
    }

    /**
     * Redirects to the page for display of word stats of individual preview description returned in a project
     * @param searchedKeywords Search Keyword
     * @param previewDescription Preview Description
     * @return CompletableFuture Result containing html for word stats of individual preview description
     * @author Shreyas Patel
     */
    public CompletableFuture<Result> individualWordStatistics(String searchedKeywords,String previewDescription) {
        System.out.println("Came to individualWordStatistics: "+searchedKeywords+" : "+previewDescription);
        //return CompletableFuture.completedFuture(ok(views.html.wordStatistics.render(freeLancerService.individualWordStatistics(previewDescription),searchedKeywords,previewDescription,false)));
        ActorRef wordStats =  actorSystem.actorOf(IndividualWordStatsActor.props(freeLancerService));
        return FutureConverters.toJava(ask(wordStats, previewDescription, Integer. MAX_VALUE))
                .thenApply(response -> ok(views.html.wordStatistics.render((LinkedHashMap<String,Integer>)response,searchedKeywords,previewDescription,false))).toCompletableFuture();
    }

    /**
     * Redirects to the page for display of average flesch readability index of preview descriptions returned in search results
     * @param keyword Search Keyword
     * @return CompletableFuture Result containing html for calculated values of averages of indexes
     * @author Anurag Shekhar
     */
    @SuppressWarnings("unchecked")
    public CompletableFuture<Result> averageFleschIndex(String keyword) {
        System.out.println("Came to averageScoreCalculate: "+keyword);
       // return CompletableFuture.completedFuture(ok(views.html.globalReadabilityScore.render(freeLancerService.averageScoreCalculate(keyword),keyword)));
        ActorRef averageFleschIndexActor =  actorSystem.actorOf(GlobalReadabilityActor.props(freeLancerService));
        return FutureConverters.toJava(ask(averageFleschIndexActor, keyword, Integer. MAX_VALUE))
                .thenApply(response -> ok(views.html.globalReadabilityScore.render((HashMap<String,String>)response,keyword))).toCompletableFuture();
    }

    /**
     * Redirects to the page for display of individual flesch readability index of preview description returned in search results
     * @param previewDescription preview description
     * @return CompletableFuture Result containing html for calculated values of flesch readability index
     * @author Anurag Shekhar
     */
    @SuppressWarnings("unchecked")
    public CompletableFuture<Result> singleReadabilityScore(String previewDescription) {
        System.out.println("Came to Readability Score : "+" : "+previewDescription);
        //return CompletableFuture.completedFuture(ok(views.html.readabilityScore.render(freeLancerService.readbilityScoreCalculate(previewDescription),previewDescription)));
        ActorRef indFleschIndexActor =  actorSystem.actorOf(IndividualReadabilityActor.props(freeLancerService));
        return FutureConverters.toJava(ask(indFleschIndexActor, previewDescription, Integer. MAX_VALUE))
                .thenApply(response -> ok(views.html.readabilityScore.render((HashMap<String,String>)response,previewDescription))).toCompletableFuture();
    }

    /**
     * Redirects to the page for display of owner information and projects
     * @param ownerId Owner ID
     * @return CompletableFuture Result containing html for owner information and projects
     * @author Almas Saba
     */
    public CompletableFuture<Result> userInformation(String ownerId)
    {
        //return CompletableFuture.completedFuture(ok(views.html.userDetailsProjects.render(freeLancerService.getUserDetails(ownerId))));
        ActorRef userInformationActor =  actorSystem.actorOf(UserInformationActor.props(freeLancerService));
        return FutureConverters.toJava(ask(userInformationActor, ownerId, 10000))
                .thenApply(response -> ok(views.html.userDetailsProjects.render((UserInformation) response))).toCompletableFuture();
    }

}
