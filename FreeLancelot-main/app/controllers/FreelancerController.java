package controllers;


import model.ProjectInfo;
import model.SearchResult;


import play.i18n.MessagesApi;
import play.data.Form;
import play.data.FormFactory;

import play.mvc.Controller;

import play.mvc.Result;

import javax.inject.Inject;

import java.util.List;
import java.util.concurrent.CompletableFuture;


import services.FreeLancerService;
import java.util.concurrent.CompletionStage;

/**
 * FreelancerController
 * Implements the Freelancelot app using Freelancer's API
 * @author Aditi Aditi
 */
public class FreelancerController extends Controller {

    private FreeLancerService freeLancerService;
    private Form form;

    @Inject
    FormFactory formFactory;

    /**
     * Constructor for initializing FreelanceController
     * @param freeLancerService injected parameter
     * @param messagesApi injected parameter messages
     * @author Aditi Aditi
     */
    @Inject
    public FreelancerController(FreeLancerService freeLancerService, MessagesApi messagesApi) {

        this.freeLancerService = freeLancerService;

    }

    /**
     * Method to fetch the projects from API by delegating to FreelancerService
     * @param keyword for search
     * @return HTTP OK response as CompletableFuture Result
     * @author Aditi Aditi
     */
    public CompletableFuture<Result> fetch(String keyword) {
        System.out.println("Call is coming to backend fetch");
        System.out.println("Keyword for Project Search is "+ keyword);
        List<SearchResult> filledProjects =null;
        filledProjects= freeLancerService.getOrQuery(keyword);
        System.out.println("Filled Project count :"+ filledProjects.size());
        return CompletableFuture.completedFuture(ok());

    }

    /**
     * Renders the initial search page at the beginning
     * @return CompletableFuture Result containing index html
     * @author Aditi Aditi
     */
    public CompletableFuture<Result> fetchempty() {
        System.out.println("Empty Call");
        freeLancerService.clearResults();
        return CompletableFuture.completedFuture(ok(views.html.index.render(null )));

    }

    /**
     * Renders the display of all searches till now based on the listOfSearches
     * @return CompletableFuture Result containing listResults html
     * @author Aditi Aditi
     */
    public CompletableFuture<Result> listAll() {
        System.out.println("Came to List All");
        return CompletableFuture.completedFuture(ok(views.html.listResults.render(freeLancerService.listOfSearch)));

    }

    /**
     * Redirects to the page for display of projects with Projects fetched for the Skill
     * @param id skill Id
     * @param skillname Name of the skill or Job
     * @return CompletableFuture Result containing listResults html
     * @author Aditi Aditi
     */

    public CompletableFuture<Result> fetchProjectsForSkill(String skillname,String id){
        System.out.println("Call is coming to Skill backend fetch");
        System.out.println("Keyword for Skill Search is "+ id);
        List<SearchResult> filledProjects =null;

        filledProjects= freeLancerService.projectsForSkill(id);
        System.out.println("Filled Skills Project count :"+ filledProjects.size());
        //return CompletableFuture.completedFuture(ok(views.html.index.render(filledProjects )));
        return CompletableFuture.completedFuture(redirect(routes.FreelancerController.listSkillProjects(skillname)));
    }

    /**
     * Renders the display of project (maximum 10)  for the skill
     * @param skillname Name of the skill clicked on
     * @return CompletableFuture Result containing listResultsForSkill html
     * @author Aditi Aditi
     */
    public CompletableFuture<Result> listSkillProjects(String skillname) {
        System.out.println("Came to List Skill All");
        return CompletableFuture.completedFuture(ok(views.html.listResultsForSkill.render(skillname,freeLancerService.listOfSearchSkills )));

    }

    /**
     * Redirects to the page for display of word statistics of all preview descriptions returned in a project
     * @param searchedKeywords Search Keyword
     * @return CompletableFuture Result containing html for word statistics of all preview descriptions
     * @author Shreyas Patel
     */
    public CompletableFuture<Result> globalWordStatistics(String searchedKeywords) {
        System.out.println("Came to globalWordStatistics: "+searchedKeywords);
        return CompletableFuture.completedFuture(ok(views.html.wordStatistics.render(freeLancerService.globalWordStatistics(searchedKeywords),searchedKeywords,"",true)));

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
        return CompletableFuture.completedFuture(ok(views.html.wordStatistics.render(freeLancerService.individualWordStatistics(previewDescription),searchedKeywords,previewDescription,false)));
    }

    /**
     * Redirects to the page for display of average flesch readability index of preview descriptions returned in search results
     * @param keyword Search Keyword
     * @return CompletableFuture Result containing html for calculated values of averages of indexes
     * @author Anurag Shekhar
     */
    public CompletableFuture<Result> averageFleschIndex(String keyword) {
        System.out.println("Came to averageScoreCalculate: "+keyword);
       return CompletableFuture.completedFuture(ok(views.html.globalReadabilityScore.render(freeLancerService.averageScoreCalculate(keyword),keyword)));

    }

    /**
     * Redirects to the page for display of individual flesch readability index of preview description returned in search results
     * @param previewDescription preview description
     * @return CompletableFuture Result containing html for calculated values of flesch readability index
     * @author Anurag Shekhar
     */
    public CompletableFuture<Result> singleReadabilityScore(String previewDescription) {
        System.out.println("Came to Readability Score : "+" : "+previewDescription);
        return CompletableFuture.completedFuture(ok(views.html.readabilityScore.render(freeLancerService.readbilityScoreCalculate(previewDescription),previewDescription)));

    }

    /**
     * Redirects to the page for display of owner information and projects
     * @param ownerId Owner ID
     * @return CompletableFuture Result containing html for owner information and projects
     * @author Almas Saba
     */
    public CompletionStage<Result> userInformation(String ownerId)
    {
        return CompletableFuture.completedFuture(ok(views.html.userDetailsProjects.render(freeLancerService.getUserDetails(ownerId))));
    }
}
