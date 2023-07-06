package util;

import model.JobDetails;
import model.ProjectInfo;
import model.SearchResult;
import model.UserInformation;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.IntStream;

public class MockObjectCreator {

    /**
     * Constructor to create mock ProjectInfo object for testing purpose
     * @param ownerId ownerId of the project
     * @param title title of the project
     * @param previewDescription preview description of the project
     * @return ProjectInfo object
     * @author Shreyas Patel, Anurag Shekhar
     */
    public static ProjectInfo createMockProjectInfo(String ownerId, String title, String previewDescription){
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setOwnerId(ownerId);
        projectInfo.setTitle(title);
        projectInfo.setPreviewDescription(previewDescription);
        return projectInfo;
    }

    /**
     * Constructor to create mock LinkedHashMap for word stats testing
     * @return LinkedHashMap object
     * @author Shreyas Patel
     */
    public static LinkedHashMap<String,Integer> createMockLinkedHashMap(){
        LinkedHashMap<String,Integer> result = new  LinkedHashMap<String,Integer>();
        result.put("word",2);
        result.put("w",1);
        return result;
    }

    /**
     * Constructor to create mock HashMap for readability testing
     * @return HashMap object
     * @author Anurag Shekhar
     */
    public static HashMap<String,String> createMockHashMap(){
        HashMap<String,String> result = new  HashMap<String,String>();
        result.put("word","w");
        result.put("sentence","s");
        return result;
    }

    /**
     * Constructor to create mock wsResponse List of ProjectInfo for api testing
     * @return CompletionStage object of List of ProjectInfo
     * @author Shreyas Patel, Anurag Shekhar
     */
    public static CompletionStage<List<ProjectInfo>> createMockCompletionStage(int n){
        CompletionStage<List<ProjectInfo>> wsResponseCompletionStage = CompletableFuture.completedFuture(createMockListProjectInfo(n));
        return wsResponseCompletionStage;
    }

    /**
     * Constructor to create mock List of ProjectInfo for api testing
     * @param n number of projects
     * @return List of ProjectInfo
     * @author Shreyas Patel, Anurag Shekhar
     */
    public static List<ProjectInfo> createMockListProjectInfo(int n){
        List<ProjectInfo> projectInfoList = new ArrayList<>();
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setTitle("title");
        IntStream.range(0,n).forEach( (i) -> {projectInfoList.add(projectInfo);} );
        return projectInfoList;
    }

    /**
     * Constructor to create mock List of SearchResult of size 1 for api testing
     * @param n number of projects
     * @return List of SearchResult
     * @author Shreyas Patel, Anurag Shekhar
     */
    public static List<SearchResult> createMockListSearchResult1(int n){
        SearchResult searchResult = new SearchResult();
        List<SearchResult> searchResultList = new ArrayList<>();
        List<ProjectInfo> projectInfoList1 = new ArrayList<>();

        ProjectInfo projectInfo1 = new ProjectInfo();
        projectInfo1.setTitle("title1");
        IntStream.range(0,n).forEach( (i) -> {projectInfoList1.add(projectInfo1);} );
        searchResult.setKeyword("java");
        searchResult.setProjectInfo(projectInfoList1);
        searchResultList.add(searchResult);

        return searchResultList;
    }

    /**
     * Constructor to create mock List of SearchResult of size 2 for api testing
     * @param n number of projects
     * @return List of SearchResult
     * @author Shreyas Patel, Anurag Shekhar
     */
    public static List<SearchResult> createMockListSearchResult2(int n){
        SearchResult searchResult = new SearchResult();
        List<SearchResult> searchResultList = new ArrayList<>();
        List<ProjectInfo> projectInfoList1 = new ArrayList<>();

        ProjectInfo projectInfo1 = new ProjectInfo();
        projectInfo1.setTitle("title1");
        IntStream.range(0,n).forEach( (i) -> {projectInfoList1.add(projectInfo1);} );
        searchResult.setKeyword("keyword1");
        searchResult.setProjectInfo(projectInfoList1);
        searchResultList.add(searchResult);

        List<ProjectInfo> projectInfoList2 = new ArrayList<>();
        ProjectInfo projectInfo2 = new ProjectInfo();
        projectInfo2.setTitle("title2");
        IntStream.range(0,n).forEach( (i) -> {projectInfoList2.add(projectInfo2);} );
        searchResult.setKeyword("keyword2");
        searchResult.setProjectInfo(projectInfoList2);
        searchResultList.add(searchResult);

        return searchResultList;
    }

    /**
     * Constructor to create mock UserInformation for api testing
     * @return userInformation object
     * @author Almas Saba
     */
    public static UserInformation createMockUserInformation(){
        UserInformation userInformation = new UserInformation();
        userInformation.setUsername("username1");
        userInformation.setCompany("test-company");
        userInformation.setRegistrationDate("1647489381");
        userInformation.setLimitedAccount("true");
        userInformation.setCountry("test-country");
        userInformation.setId("id");
        userInformation.setEmailVerified("true");
        userInformation.setLimitedAccount("true");
        userInformation.setPrimaryCurrency("test");
        userInformation.setRole("test-role");
        List<ProjectInfo> projectInfoList = new ArrayList<>();
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setTitle("title");
        projectInfo.setOwnerId("id");
        projectInfo.setPreviewDescription("pr");
        projectInfo.setType("type");
        projectInfo.setTimeSubmitted("1647489381");
        JobDetails jobDetails = new JobDetails();
        jobDetails.setSkillName("skill");
        jobDetails.setJobID("2");
        JobDetails [] jobDetails1 = new JobDetails[1];
        jobDetails1[0]=jobDetails;
        projectInfo.setSkills(jobDetails1);
        projectInfoList.add(projectInfo);
        userInformation.setProjectList(projectInfoList);
        return userInformation;
    }


    /**
     * Constructor to create mock User JSONObject for api testing
     * @return userInformation object
     * @author Almas Saba
     */
    public static JSONObject createMockUserJSONObject(){
        JSONObject user = new JSONObject("{\"id\": 61316717,\"username\": \"UmerAkhter\",\"role\": \"Engineer\",\"limited_account\": true,\"location\": {\"country\": {\"name\": \"India\"}},\"registration_date\": \"1647734379\",\"closed\": false,\"avatar\": \"/ppic/171709769/logo/34702096/profile_logo_34702096.jpg\",\"jobs\": [{\"id\": 3,\"name\": \"PHP\"},{\"id\": 1,\"name\": \"Websites, IT & Software\"}]}");
        return user;
    }

    /**
     * Constructor to create mock Project JSONObject for api testing
     * @return userInformation object
     * @author Almas Saba
     */
    public static JSONObject createMockProjectJSONOBject(){
        JSONObject project = new JSONObject("{projects: [{\"id\": 61316717,\"owner_id\": 61316717,\"title\": \"Desarrollar modulo Compras\",\"status\": \"active\",\"seo_url\": \"java/Desarrollar-modulo-Compras\",\"currency\": {\"id\": 1,\"code\": \"USD\",\"sign\": \"$\",\"name\": \"US Dollar\",\"exchange_rate\": 1.0,\"country\": \"US\",\"is_external\": false,\"is_escrowcom_supported\": true},\"description\": \"Se requiere programador para desarrollar modulos en cuanto a un sistema de gestion, se requiere que el programador tenga experiencia en las siguientes tecnologias: java con spring boot, angular 9, linux con ubuntu, bd postgres, ademas el programador debe dominar el idioma español para que la comunicacion sea fuida y su comprension sea a plenitud.\",\"jobs\": [{\"id\": 7,\"name\": \"Java\"}, {\"id\": 1,\"name\": \"Websites, IT & Software\"}],\"submitdate\": 1647734379,\"preview_description\": \"Se requiere programador para desarrollar modulos en cuanto a un sistema de gestion, se requiere que \",\"deleted\": false,\"nonpublic\": false,\"hidebids\": false,\"type\": \"fixed\",\"bidperiod\": 7,\"budget\": {\"minimum\": 250.0,\"maximum\": 750.0},\"featured\": false,\"urgent\": false,\"bid_stats\": {\"bid_count\": 2,\"bid_avg\": 502.5},\"time_submitted\": 1647734379,\"upgrades\": {\"featured\": false,\"sealed\": false,\"nonpublic\": false,\"fulltime\": false,\"urgent\": false,\"qualified\": false,\"NDA\": false,\"ip_contract\": false,\"non_compete\": false,\"project_management\": false,\"pf_only\": false},\"language\": \"es\",\"hireme\": false,\"frontend_project_status\": \"open\",\"local\": false,\"negotiated\": false,\"time_free_bids_expire\": 1647728795,\"pool_ids\": [\"freelancer\"],\"enterprise_ids\": [],\"is_escrow_project\": false,\"is_seller_kyc_required\": false,\"is_buyer_kyc_required\": false}]}");
        return project;
    }
}