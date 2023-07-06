package services;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ReadibilityHelper.readabilityScoreHelper;
import model.ProjectInfo;
import model.SearchResult;
import play.libs.ws.WSResponse;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import model.UserInformation;
import UserDetailsHelper.UserDetails;

/**
 * Use the data provided by the FreelancerAPIImplementation to parse it and return
 * nice POJO instances of the Project Searches
 * @author Aditi Aditi
 */
public class FreeLancerService {

    @Inject
    private services.FreelancerAPI freelancerAPI;
    public ObjectMapper mapper = new ObjectMapper();
    private List<ProjectInfo>  listOfProjects= new ArrayList<ProjectInfo>();
    public List<SearchResult> listOfSearch = new ArrayList<>();
    public List<SearchResult> listOfSearchSkills = new ArrayList<>();


    /**
     * Checks of if result is already searched returns it otherwise calls the Freelancer API
     * via FreelancerAPIImplmentation
     * @param keyword for the keyword being searched
     * @param limitValue to limit the results from the API
     * @param offsetValue to handle pagination if needed
     * @return List of Projects as CompletionStage list of Project Info
     * @author Aditi Aditi
     */
    public CompletionStage<List<ProjectInfo>> handleSearch(String keyword,String limitValue,String offsetValue){
        return freelancerAPI.search(keyword, limitValue, offsetValue).thenApplyAsync(WSResponse::asJson )
                .thenApplyAsync(jsonNode-> jsonNode.get("result").get("projects"))
                .thenApplyAsync(this::parseProjectNodes);

    }

    public int getIndexFromlistOfSearch(String keyword) {
        for(int i=0;i< listOfSearch.size();i++) {
            if(listOfSearch.get(i).getKeyword().equals(keyword)){
                return i;
            }
        }
        return -1;
    }

    public List<SearchResult> getOrQuery(List<String> keywords) {
        System.out.println("getOrQuery inside");
        for (String keyword : keywords) {
            int indexOfKeywordResult = -1;
            try {
                SearchResult searchResult = new SearchResult();
                searchResult.setKeyword(keyword.trim());
                Optional<SearchResult> result = this.listOfSearch.stream().filter(element -> element.getKeyword().equalsIgnoreCase(keyword.trim())).findAny();

                if (result.isPresent()) {
                    System.out.println("result keyword ==:" + result.get().getKeyword());
                    System.out.println("listOfSearch size ==:" + listOfSearch.size());
                    indexOfKeywordResult = this.getIndexFromlistOfSearch(result.get().getKeyword());
                }
                //else {
                searchResult.setProjectInfo(handleSearch(keyword, "90", "0").toCompletableFuture().get());
                if (searchResult.getSizeOfList() == 90) {
                    searchResult.addProjectInfo(handleSearch(keyword, "90", "90").toCompletableFuture().get());
                    if (searchResult.getSizeOfList() == 180) {
                        searchResult.addProjectInfo(handleSearch(keyword, "70", "180").toCompletableFuture().get());
                    }
                }
                if (indexOfKeywordResult == -1) {
                    listOfSearch.add(0, searchResult);
                } else {
                    listOfSearch.set(indexOfKeywordResult, searchResult);
                }
            } catch (Exception e) {
                return null;
            }
        }
        return listOfSearch;
    }


    /**
     * Parses the projects obtained from FreelancerAPI to ProjectInfo POJO
     * @param jsonNode representing the projects as json
     * @return List of ProjectInfo Objects
     * @author Aditi Aditi
     */
    public List<ProjectInfo>  parseProjectNodes(JsonNode jsonNode) {
        try {
            return Arrays.asList(mapper.treeToValue(jsonNode,
                    ProjectInfo[].class));
        } catch (Exception e) {
            System.out.println ("Issue While Parsing the results");
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Add the Project List and Keyword Searched to the list of Searches
     * @param id represents the ID of the Skill or Job
     * @return the list of Search
     * @author Aditi Aditi
     */
    public List<SearchResult> projectsForSkill(String id){
        try {
            listOfSearchSkills.clear();
            SearchResult searchResult = new SearchResult();
            searchResult.setKeyword(id);
            searchResult.setProjectInfo(handleSkillSearch(id).toCompletableFuture().get());
            listOfSearchSkills.add(searchResult);
            return listOfSearchSkills;
        }
        catch(Exception e){
            return null;
        }
    }

    /**
     * Calls the API implementation and process the results to convert to POJO
     * @param id represents the id of the Skill or Job clicked on
     * @return Completion Stage List of Project Info
     * @throws JsonProcessingException json processing exception
     * @author Aditi Aditi
     */
    public CompletionStage<List<ProjectInfo>> handleSkillSearch(String id) throws JsonProcessingException{
        return freelancerAPI.searchSkill(id).thenApplyAsync(WSResponse::asJson )
                .thenApplyAsync(jsonNode-> jsonNode.get("result").get("projects"))
                .thenApplyAsync(this::parseProjectNodes);
    }


    /**
     * Calculates global Word Stats for all preview descriptions returned by api call
     * @param keyword represents the search keyword
     * @return LinkedHashMap with key as individual words in preview description and value as their frequency, sorted in descending order of the frequencies.
     * @author Shreyas Patel
     */
	public LinkedHashMap <String, Integer> globalWordStatistics(String keyword) {
        Optional<SearchResult> result = this.listOfSearch.stream().filter(element -> element.getKeyword().equalsIgnoreCase(keyword.trim())).findAny();
        if (result.isPresent()) {
            String globalPreviewDescription = result.get().getProjectInfo().stream()
                    .map(projectInfo -> projectInfo.getPreviewDescription().replaceAll("[^A-Za-z0-9\\s]+", ""))
                    .map(Object::toString).collect(Collectors.joining(" "));
    		if(globalPreviewDescription.isEmpty())
    		{
    			return null;
    		}
    		Map <String, Integer > wordCounter = Stream.of(globalPreviewDescription).map(w -> w.split("\\s+")).flatMap(Arrays::stream).collect(Collectors.toMap(word -> word, word -> 1, Integer::sum));
    		LinkedHashMap <String, Integer > wordCounterDesc = wordCounter.entrySet() .stream() .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()) .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    		return wordCounterDesc;
        } else {
        	return null;
        }

    }

    /**
     * Calculates Word Stats for preview descriptions of individual project returned by api call
     * @param previewDescription represents the preview descriptions in a project
     * @return LinkedHashMap with key as individual words in preview description and value as their frequency, sorted in descending order of the frequencies.
     * @author Shreyas Patel
     */
	public LinkedHashMap <String, Integer> individualWordStatistics(String previewDescription)
	{
		if(previewDescription.trim().isEmpty())
		{
			return null;
		}
		Map <String, Integer > wordCounter = Stream.of(previewDescription.replaceAll("[^A-Za-z0-9\\s]+", "")).map(w -> w.split("\\s+")).flatMap(Arrays::stream).collect(Collectors.toMap(word -> word, word -> 1, Integer::sum));
		LinkedHashMap <String, Integer > wordCounterDesc = wordCounter.entrySet() .stream() .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()) .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		return wordCounterDesc;
	}

    /**
     * Empties the search Results on new invocation
     * @author Aditi Aditi
     */
    public void clearResults() {
        this.listOfSearch.clear();
    }

    /**
     * Calculates Flesch Readability Index, FKGL and Education Level required for preview descriptions for a specific search result returned by api call
     * @param previewDescription represents the preview descriptions in a search result
     * @return HashMap with  Flesch Readability Index, FKGL and Education Level as key and their calculated scores as value
     * @author Anurag Shekhar
     */
    public HashMap<String, String> readbilityScoreCalculate(String previewDescription) {
		HashMap<String,String> readabilityResult = new HashMap<String,String>();

        List<String> input = new ArrayList<String>();
        input.add(previewDescription);

        double sentence_count=0.0;
		double words_count= 0.0;
		double syllables_count = 0.0;
		double flesch_Index = 0.0;
		double fkgl = 0.0;
		String level="";
		
		String regex_pattern ="([.!?:;]+)([\\s\\n]+)";
		List<String> sentences = input.stream()
										.map(sentence -> sentence.trim().split(regex_pattern))
										.flatMap(Arrays::stream)
										.filter(s->!s.contentEquals(""))
										.collect(Collectors.toList());
	
		sentence_count = sentences.stream().count();
		List<String> words = sentences.stream()
								.map(word -> word.trim().split("\\s"))
								.flatMap(Arrays::stream)
                                .map(w->w.replaceAll("[^A-Za-z\\s]",""))
								.filter(w->w.matches("[a-zA-Z]+"))
								.collect(Collectors.toList());
		words_count = words.stream().count();

		 syllables_count =  readabilityScoreHelper.countTotalSyllables(input);

		 flesch_Index = 206.835 - ((84.6*(syllables_count/words_count)-1.015*(words_count/sentence_count)));
		 fkgl = 0.39*(words_count/sentence_count) + 11.8* (syllables_count/words_count) - 15.59;
		 level = readabilityScoreHelper.computeLevel(flesch_Index);


		 readabilityResult.put("Flesch_Index",Double.toString(flesch_Index));
		 readabilityResult.put("FKGL",Double.toString(fkgl));
		 readabilityResult.put("Level",level);

		 return readabilityResult;
	}

    /**
     * Calculates average Flesch Readability Index, FKGL  required for preview descriptions for all search results returned by api call
     * @param keyword search keyword for API call
     * @return HashMap with  Flesch Readability Index, FKGL  as key and their calculated average scores as value
     * @author Anurag Shekhar
     */
    public HashMap<String, String> averageScoreCalculate(String keyword) {
        
    	HashMap<String,String> averageReadabilityResult = new HashMap<String,String>();
        HashMap<String,String> indReadabilityResult = new HashMap<String,String>();

        List<Double> flesch_indexes = new ArrayList<Double>();
        List<Double> fkgl = new ArrayList<Double>();

        double avgFleschIndex , avgFKGL ;
        Optional<SearchResult> result = this.listOfSearch.stream()
                                                        .filter(i -> i.getKeyword().equalsIgnoreCase(keyword.trim()))
                                                        .findAny();
        if (result.isPresent()) {
        	List<String> previewDescriptionList = result.get().getProjectInfo().stream()
                                                    .map(projectInfo -> projectInfo.getPreviewDescription())
                                                    .collect(Collectors.toList());
            for(String s : previewDescriptionList ){
                indReadabilityResult = readbilityScoreCalculate(s);
                //if(!indReadabilityResult.get("Flesch_Index").equals(""))
                    flesch_indexes.add(Double.parseDouble(indReadabilityResult.get("Flesch_Index")));
                //if(!indReadabilityResult.get("FKGL").equals(""))
                    fkgl.add(Double.parseDouble(indReadabilityResult.get("FKGL")));
                indReadabilityResult = new HashMap<String,String>();
            }
           avgFleschIndex = flesch_indexes.stream().mapToDouble(n->n).average().orElse(-1);
            avgFKGL = fkgl.stream().mapToDouble(n->n).average().orElse(-1);

        } else {
        	return null;
        }
        averageReadabilityResult.put("Flesch_Index",Double.toString(avgFleschIndex));
        averageReadabilityResult.put("FKGL",Double.toString(avgFKGL));
		 return averageReadabilityResult;
	}

    /**
     * Method to call helper function which returns User Information for an Owner
     * @param ownerId Owner ID
     * @return UserInformation object
     * @author Almas Saba
     */
    public UserInformation getUserDetails(String ownerId)
    {
        UserDetails userDetails = new UserDetails();
        return  userDetails.getUserDetails(ownerId);
    }
}
