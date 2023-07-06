import UserDetailsHelper.UserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import model.ProjectInfo;
import model.SearchResult;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import scala.compat.java8.FutureConverters;
import services.FreeLancerService;
import services.FreelancerAPI;
import util.MockObjectCreator;
import model.UserInformation;
import java.util.*;
import services.FreelancerAPIImplementation;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static akka.pattern.Patterns.ask;
import static play.inject.Bindings.bind;


/**
 * Test the FreelancerService
 * @author Aditi Aditi
 */
@RunWith(MockitoJUnitRunner.class)
public class FreelancerServiceTest {

    private static FreeLancerService freeLancerService;

    private static FreeLancerService freeLancerServiceNegative;

    @Mock
    private static UserDetails userDetails;

    @InjectMocks
    private static FreeLancerService freeLancerService3;

    /**
     * Initialise the test application, bind the Freelancer API interface to its mock implementation
     * @author Aditi Aditi
     */
    @BeforeClass
    public static void initTestApp(){
        Injector testApp = new GuiceInjectorBuilder()
                .overrides(bind(FreelancerAPI.class).to(FreelancerTestAPIImplentation.class))
                .build();
        freeLancerService= testApp.instanceOf(FreeLancerService.class);
        Injector testAppNegative = new GuiceInjectorBuilder()
                .overrides(bind(FreelancerAPI.class).to(FreelancerTestNegativeAPIImplentation.class))
                .build();
        freeLancerServiceNegative= testAppNegative.instanceOf(FreeLancerService.class);

    }

    /**
     * Test the freeLancerService.handleSearch method
     * @throws ExecutionException ExecutionException
     * @throws InterruptedException InterruptedException
     * @throws IOException IOException
     * @author Aditi Aditi
     */
    @Test
    public void testHandleSearch() throws ExecutionException, InterruptedException, JsonProcessingException {
        //writeJsonToSearchFile("/resources/searchGoodJson.json");
        String keyword = "Java";
        List<ProjectInfo> projectList =freeLancerService.handleSearch(keyword,"10","0").toCompletableFuture().get();
        Assert.assertEquals(10,projectList.size());
    }

    /**
     * Test the freeLancerService.handleSkillSearch method
     * @throws ExecutionException ExecutionException
     * @throws InterruptedException InterruptedException
     * @throws IOException IOException
     * @author Aditi Aditi
     */
    @Test
    public void testHandleSkillSearch() throws ExecutionException, InterruptedException, JsonProcessingException {

        String skillIdForJava="7";
        List<ProjectInfo> projectListForSkill=freeLancerService.handleSkillSearch(skillIdForJava).toCompletableFuture().get();
        List<model.JobDetails> projectWithId= projectListForSkill.stream().map(element -> element.getSkills()).flatMap(Stream::of).filter(skill -> skill.getJobID().equals(skillIdForJava)).collect(Collectors.toList());
        Assert.assertEquals(7,projectWithId.size());
    }

    /**
     * Test the freeLancerService.projectsForSkill method , positive test
     * @author Aditi Aditi
     */
    @Test
    public void testProjectsForSkill(){
        String skillIdForJava="7";
        List<SearchResult> searchResult =freeLancerService.projectsForSkill(skillIdForJava);
        Assert.assertEquals(1,searchResult.size());
    }

    /**
     * Test the freeLancerServiceNegative.projectsForSkill method for exception, negative test
     * @author Aditi Aditi
     */
    @Test
    public void testProjectsForSkillForException() throws JsonProcessingException {
        String skillIdForJava="abd";
        List<SearchResult> searchResult =freeLancerServiceNegative.projectsForSkill(skillIdForJava);
        Assert.assertNull(searchResult);
        System.out.println("Mapper is:"+ freeLancerServiceNegative.mapper);
    }

    /**
     * Test the ParseProject Json parsing exception
     * @author Aditi Aditi
     */
    @Test
    public void  testParseProject(){
        freeLancerServiceNegative.mapper=null;
        List<ProjectInfo> projectInfo= freeLancerServiceNegative.parseProjectNodes(null);
        Assert.assertNull(projectInfo);
    }

    /**
     * Test the freeLancerService.getOrQuery for an existing Search Result
     * @author Aditi Aditi
     */
    @Test
    public void testGetOrQueryFor1Project(){
        ProjectInfo projectWith1[]= new ProjectInfo[1];
        SearchResult searchResult= new SearchResult();
        searchResult.setKeyword("Java");
        searchResult.setProjectInfo(Arrays.asList(projectWith1));
        freeLancerService.clearResults();
        freeLancerService.listOfSearch.add(searchResult);
        List<String> keywords = new ArrayList<String>();
        keywords.add("Java");
        List<SearchResult> listOfSearch =freeLancerService.getOrQuery(keywords);
        Assert.assertEquals(1,freeLancerService.listOfSearch.size());
    }

    /**
     * Test the freeLancerService.readbilityScoreCalculate for an existing Search Result
     *
     * @author Anurag Shekhar
     */
    @Test
    public void testReadabilityScoreCalculate() {
        String previewDescription = "Looking for someone with expertise in optimizing searches in large SQL databases to provide consulta";

        HashMap<String, String> actualReadabilityResult = new HashMap<String, String>();
        actualReadabilityResult = freeLancerService.readbilityScoreCalculate(previewDescription);

        HashMap<String, String> expectedReadabilityResult = new HashMap<String, String>();
        expectedReadabilityResult.put("Flesch_Index", "30.30000000000001");
        expectedReadabilityResult.put("FKGL", "17.006666666666664");
        expectedReadabilityResult.put("Level", "College");

        Assert.assertTrue(expectedReadabilityResult.equals(actualReadabilityResult));
    }

    /**
     * Test the freeLancerService.averageScoreCalculate for an existing Search Result
     *
     * @author Anurag Shekhar
     */
    @Test
    public void testAverageScoreCalculate() {
        String keyword = "Java";
        List<ProjectInfo> projectInfoList = new ArrayList<>();
        projectInfoList.add(MockObjectCreator.createMockProjectInfo("23515703","Short video recorder and editor code like tiktok in Java","I need short video recorder and editor like tiktok in java, features are below Camera features \\nMu"));
        projectInfoList.add(MockObjectCreator.createMockProjectInfo("9039243","Need a Java Expert having Good Knowledge of RMI","Need done ASAP: Search and sort data related to song objects. The song data will be obtained from database."));

        SearchResult searchResult= new SearchResult();
        searchResult.setKeyword(keyword);
        searchResult.setProjectInfo(projectInfoList);
        freeLancerService.clearResults();
        freeLancerService.listOfSearch.add(searchResult);

        HashMap<String, String> expectedAverageReadabilityResult = new HashMap<String, String>();
        expectedAverageReadabilityResult.put("Flesch_Index","55.6317750257998" );
        expectedAverageReadabilityResult.put("FKGL", "11.701486068111455");
        Assert.assertTrue(expectedAverageReadabilityResult.equals(freeLancerService.averageScoreCalculate(keyword)));

        keyword = "sql";
        Assert.assertNull(freeLancerService.averageScoreCalculate(keyword));


    }

    /**
     * Test the freeLancerService.globalWordStatistics for an existing Search Result - success for valid input
     *
     * @author Shreyas Patel
     */
    @Test
    public void testGlobalWordStatistics_WithValidInput_Success(){

        List<ProjectInfo> projectInfoList = new ArrayList<>();
        projectInfoList.add(MockObjectCreator.createMockProjectInfo("23515703","Short video recorder and editor code like tiktok in Java","short video recorder in Java."));
        projectInfoList.add(MockObjectCreator.createMockProjectInfo("9039243","Need a Java Expert having Good Knowledge of RMI","The song data will be obtained using Java."));
        SearchResult searchResult= new SearchResult();
        searchResult.setKeyword("Java");
        searchResult.setProjectInfo(projectInfoList);
        freeLancerService.clearResults();
        freeLancerService.listOfSearch.add(searchResult);
        LinkedHashMap<String, Integer> result = freeLancerService.globalWordStatistics("Java");
        LinkedHashMap<String, Integer> expectedResult = new LinkedHashMap<>();
        expectedResult.put("Java", 2);
        expectedResult.put("song", 1);
        expectedResult.put("The", 1);
        expectedResult.put("using", 1);
        expectedResult.put("recorder", 1);
        expectedResult.put("be", 1);
        expectedResult.put("data", 1);
        expectedResult.put("will", 1);
        expectedResult.put("in", 1);
        expectedResult.put("short", 1);
        expectedResult.put("obtained", 1);
        expectedResult.put("video", 1);
        Assert.assertTrue(expectedResult.equals(result));

    }

    /**
     * Test the freeLancerService.globalWordStatistics for an existing Search Result - return null for empty preview description
     *
     * @author Shreyas Patel
     */
    @Test
    public void testGlobalWordStatistics_WithEmptyPreviewDesc_ReturnNull(){

        List<ProjectInfo> projectInfoList = new ArrayList<>();
        projectInfoList.add(MockObjectCreator.createMockProjectInfo("46724384","Need a UI/UX Designer",""));

        SearchResult searchResult= new SearchResult();
        searchResult.setKeyword("Designer");
        searchResult.setProjectInfo(projectInfoList);
        freeLancerService.clearResults();
        freeLancerService.listOfSearch.add(searchResult);
        LinkedHashMap<String, Integer> result = freeLancerService.globalWordStatistics("Designer");
        Assert.assertNull(result);
    }

    /**
     * Test the freeLancerService.globalWordStatistics for an existing Search Result - return null for non existing project
     *
     * @author Shreyas Patel
     */
    @Test
    public void testGlobalWordStatistics_WithEmptyProject_ReturnNull(){
        List<ProjectInfo> projectInfoList = new ArrayList<>();
        projectInfoList.add(MockObjectCreator.createMockProjectInfo("46724384","Need a UI/UX Designer",""));
        SearchResult searchResult= new SearchResult();
        searchResult.setKeyword("Designer");
        searchResult.setProjectInfo(projectInfoList);
        freeLancerService.clearResults();
        freeLancerService.listOfSearch.add(searchResult);
        LinkedHashMap<String, Integer> result = freeLancerService.globalWordStatistics("Python");
        Assert.assertNull(result);
    }

    /**
     * Test the freeLancerService.individualWordStatistics for a project - success for valid input
     *
     * @author Shreyas Patel
     */
    @Test
    public void testIndividualWordStatistics_WithValidInput_Success(){
        LinkedHashMap<String, Integer> result = freeLancerService.individualWordStatistics("Using Eclipse, java script, tomcat, MY SQL, ant. I have coding and schema. Need help displaying webp");
        Assert.assertEquals("schema=1",result.entrySet().iterator().next().toString());

    }

    /**
     * Test the freeLancerService.individualWordStatistics for a project - return null for empty preview description
     *
     * @author Shreyas Patel
     */
    @Test
    public void testIndividualWordStatistics_WithEmptyPreviewDesc_ReturnNull(){
        LinkedHashMap<String, Integer> result = freeLancerService.individualWordStatistics(" ");
        Assert.assertNull(result);
    }

    /**
     * Test the freeLancerService.getUserDetails for a project
     *
     * @author Almas Saba
     */
    @Test
    public void testGetUserDetails() {
        //when(userDetails.getUserDetails(anyString())).thenReturn(null);
        UserInformation userInformation = freeLancerService3.getUserDetails("userid");
        Assert.assertEquals("Not found", userInformation.getUsername().toString());
    }

    /**
     * Test the freeLancerService.getIndexFromlistOfSearch method - Success
     * @author Shreyas Patel, Anurag Shekhar
     */
    @Test
    public void getIndexFromlistOfSearchSuccess() {
        freeLancerService.listOfSearch = MockObjectCreator.createMockListSearchResult1(10);
        int index =freeLancerService.getIndexFromlistOfSearch("java");
        Assert.assertEquals(0,index);
    }

    /**
     * Test the freeLancerService.getIndexFromlistOfSearch method - return -1
     * @author Shreyas Patel, Anurag Shekhar
     */
    @Test
    public void getIndexFromlistOfSearchFail() {
        freeLancerService.listOfSearch = MockObjectCreator.createMockListSearchResult1(10);
        int index =freeLancerService.getIndexFromlistOfSearch("python");
        Assert.assertEquals(-1,index);
    }
}
