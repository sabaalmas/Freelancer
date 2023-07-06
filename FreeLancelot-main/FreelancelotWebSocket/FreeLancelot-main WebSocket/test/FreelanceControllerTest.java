//import com.fasterxml.jackson.databind.ObjectMapper;
//import controllers.FreelancerController;
//
//import model.ProjectInfo;
//import model.SearchResult;
//import org.apache.http.HttpStatus;
//
//import org.junit.Test;
//import static org.junit.Assert.*;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import play.Application;
//
//import play.inject.guice.GuiceApplicationBuilder;
//
//import play.mvc.Http;
//import play.mvc.Result;
//import play.test.Helpers;
//import play.test.WithApplication;
//import UserDetailsHelper.services.FreeLancerService;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.concurrent.CompletionStage;
//import java.util.concurrent.ExecutionException;
//
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//import static play.mvc.Http.Status.OK;
//import static play.test.Helpers.*;
//
//import util.MockObjectCreator;
//
//import static org.mockito.ArgumentMatchers.anyString;
//
//
///**
// * Test class for FreelancerController using mockito
// * @author Aditi Aditi
// */
//@RunWith(MockitoJUnitRunner.class)
//public class FreelanceControllerTest extends WithApplication {
//
//    @Mock
//    private static FreeLancerService freelancerService;
//    @InjectMocks
//    private static FreelancerController freelancerController;
//
//    @Override
//    protected Application provideApplication() {
//        return new GuiceApplicationBuilder().build();
//    }
//
//    /** Unit test for testing the index endpoint for GET
//     * @author Aditi Aditi
//     */
//    @Test
//    public void indexTest() {
//        Http.RequestBuilder request = Helpers.fakeRequest()
//                .method(Helpers.GET)
//                .uri("/");
//
//        Result result = route(app, request);
//       // System.out.println(result.con);
//        assertEquals(OK, result.status());
//    }
//
//    /**
//     * Unit Test the freelancerController.fetch method
//     * @author Aditi Aditi
//     */
//    @Test
//    public void testFetchProjects(){
//        running(provideApplication(),()->{
//            try {
//                when(freelancerService.getOrQuery("Java")).thenReturn(getProjectList());
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            CompletionStage<Result> searchResults = freelancerController.fetch("Java");
//            try {
//                Result result= searchResults.toCompletableFuture().get();
//                assertEquals(HttpStatus.SC_OK,result.status());
//
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            } catch (ExecutionException e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }
//
//
//    /**
//     * Unit Test the freelancerController.listAll()
//     * @author Aditi Aditi
//     */
//    @Test
//    public void testListAllProjects(){
//        running(provideApplication(),()->{
//            try {
//                freelancerService.listOfSearch=getProjectList();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            CompletionStage<Result> searchResults = freelancerController.listAll();
//            try {
//                Result result= searchResults.toCompletableFuture().get();
//                assertEquals(HttpStatus.SC_OK,result.status());
//                assertEquals("text/html",result.contentType().get());
//                assertTrue(contentAsString(result).contains("Search terms :  Java"));
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            } catch (ExecutionException e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }
//
//    /**
//     * mock object for providing SearchResults
//     * @author Aditi Aditi
//     * @return List<SearchResult> represents the list of searches
//     */
//    private List<SearchResult> getProjectList() throws IOException {
//       // InputStream inJson = ProjectInfo.class.getResourceAsStream("/resources/project.json");
//        String projectJsonFile = getJsonFileAsString(File.separator + "test" + File.separator + "resources" +
//                File.separator + "project.json");
//        ProjectInfo projectInfo = new ObjectMapper().readValue(projectJsonFile, ProjectInfo.class);
//        ProjectInfo projectWith1[]= new ProjectInfo[1];
//        projectWith1[0]=projectInfo;
//        SearchResult searchResult= new SearchResult();
//        searchResult.setKeyword("Java");
//        searchResult.setProjectInfo(Arrays.asList(projectWith1));
//        List<SearchResult> listOfSearches = new ArrayList<>();
//        listOfSearches.add(searchResult);
//        /*Results.ok().sendResource("search.json");*/
//        return  listOfSearches;
//    }
//
//    /**
//     * Convert Json to String
//     * @author Aditi Aditi
//     * @param path of json file
//     * @return Json as String
//     */
//    private String getJsonFileAsString(String path) throws IOException {
//        String filePath = new File("").getAbsolutePath();
//        byte[] encoded = Files.readAllBytes(Paths.get(filePath.concat(path)));
//        return new String(encoded, "UTF-8");
//    }
//
//
//    /**
//     * Unit Test the freelancerController.listSkillProjects
//     * @author Aditi Aditi
//     */
//    @Test
//    public void testListSkillProjects(){
//        running(provideApplication(),()->{
//            try {
//                freelancerService.listOfSearchSkills=getProjectList();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            CompletionStage<Result> searchResults = freelancerController.listSkillProjects("Java");
//            try {
//                Result result= searchResults.toCompletableFuture().get();
//                assertEquals(HttpStatus.SC_OK,result.status());
//                assertEquals("text/html",result.contentType().get());
//                assertTrue(contentAsString(result).contains("Displaying Projects For Skill :  Java"));
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            } catch (ExecutionException e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }
//
//    /**
//     * Unit Test the freelancerController.fetchProjectsForSkill
//     * @author Aditi Aditi
//     */
//    @Test
//    public void testfetchProjectsForSkill(){
//        running(provideApplication(),()->{
//            try {
//                when(freelancerService.projectsForSkill("7")).thenReturn(getProjectList());
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            CompletionStage<Result> searchResults = freelancerController.fetchProjectsForSkill("Java","7");
//            try {
//                Result result= searchResults.toCompletableFuture().get();
//                assertEquals(HttpStatus.SC_SEE_OTHER,result.status());
//
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            } catch (ExecutionException e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }
//
//    /**
//     * Unit Test the freelancerController.singleReadabilityScore
//     * @author Anurag Shekhar
//     */
//
//    @Test
//    public void testSingleReadabilityScore() throws ExecutionException, InterruptedException {
//
//        CompletionStage<Result> readabilityScore = freelancerController.singleReadabilityScore("Test ME!!");
//        Result result = readabilityScore.toCompletableFuture().get();
//        assertEquals(OK, result.status());
//    }
//
//    /**
//     * Unit Test the freelancerController.averageFleschIndex
//     * @author Anurag Shekhar
//     */
//    @Test
//    public void testAverageFleschIndex() throws ExecutionException, InterruptedException {
//
//        CompletionStage<Result> avgReadabilityScore = freelancerController.averageFleschIndex("Test Keyword");
//        Result result = avgReadabilityScore.toCompletableFuture().get();
//        assertEquals(OK, result.status());
//    }
//
//    /**
//     * Unit Test the freelancerController.globalWordStatistics
//     * @author Shreyas Patel
//     */
//
//    @Test
//    public void testGlobalWordStatistics(){
//        when(freelancerService.globalWordStatistics(anyString())).thenReturn(MockObjectCreator.createMockLinkedHashMap());
//        CompletionStage<Result> searchResults = freelancerController.globalWordStatistics("Java");
//        try {
//            Result result= searchResults.toCompletableFuture().get();
//            assertEquals(HttpStatus.SC_OK,result.status());
//
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } catch (ExecutionException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//
//
//    /**
//     * Unit Test the freelancerController.individualWordStatistics
//     * @author Shreyas Patel
//     */
//
//    @Test
//    public void testIndividualWordStatistics(){
//        when(freelancerService.individualWordStatistics(anyString())).thenReturn(MockObjectCreator.createMockLinkedHashMap());
//        CompletionStage<Result> searchResults = freelancerController.individualWordStatistics("Java","Using Eclipse, java script");
//        try {
//            Result result= searchResults.toCompletableFuture().get();
//            assertEquals(HttpStatus.SC_OK,result.status());
//
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } catch (ExecutionException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * Unit Test the freelancerController.userInformation
//     * @author Almas Saba
//     */
//
//    @Test
//    public void testUserInformation() {
//        when(freelancerService.getUserDetails(anyString())).thenReturn(MockObjectCreator.createMockUserInformation());
//        CompletionStage<Result> searchResults = freelancerController.userInformation(anyString());
//        try {
//            Result result = searchResults.toCompletableFuture().get();
//            assertEquals(HttpStatus.SC_OK, result.status());
//
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } catch (ExecutionException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
