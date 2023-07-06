
import model.SearchResult;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import services.FreeLancerService;
import util.MockObjectCreator;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@RunWith(MockitoJUnitRunner.class)
public class FreeLancerServiceTest_getOrQuery {

    @Spy
    private FreeLancerService freeLancerService;
    /**
     * Test the freeLancerService.getOrQuery for New Search Result having more than 100 projects
     *
     * @author Shreyas Patel, Anurag Shekhar
     */

    @Test
    public void testGetOrQuery_With250Projects() throws ExecutionException, InterruptedException {
        Mockito.doReturn(MockObjectCreator.createMockCompletionStage(90)).when(freeLancerService).handleSearch("Java","90","0");
        Mockito.doReturn(MockObjectCreator.createMockCompletionStage(90)).when(freeLancerService).handleSearch("Java","90","90");
        Mockito.doReturn(MockObjectCreator.createMockCompletionStage(70)).when(freeLancerService).handleSearch("Java","70","180");
        List<String> keywords = new ArrayList<String>();
        keywords.add("Java");
        List<SearchResult> listOfSearch =freeLancerService.getOrQuery(keywords);
        Assert.assertEquals(250,listOfSearch.get(0).getSizeOfList());
    }

    /**
     * Test the freeLancerService.getOrQuery for handling NullPointerException
     *
     * @author Shreyas Patel, Anurag Shekhar
     */
    @Test
    public void testGetOrQuery_ThrowsException(){
        Mockito.doThrow(NullPointerException.class).when(freeLancerService).handleSearch("Java","90","0");
        List<String> keywords = new ArrayList<String>();
        keywords.add("Java");
        List<SearchResult> listOfSearch =freeLancerService.getOrQuery(keywords);
        Assert.assertNull(listOfSearch);
    }

    @Test
    public void testGetOrQuery_Withlessthan90Projects() throws ExecutionException, InterruptedException {
        Mockito.doReturn(MockObjectCreator.createMockCompletionStage(91)).when(freeLancerService).handleSearch("Java","90","0");
        List<String> keywords = new ArrayList<String>();
        keywords.add("Java");
        List<SearchResult> listOfSearch =freeLancerService.getOrQuery(keywords);
        Assert.assertEquals(91,listOfSearch.get(0).getSizeOfList());
    }

    @Test
    public void testGetOrQuery_Withlessthan250Projects() throws ExecutionException, InterruptedException {
        Mockito.doReturn(MockObjectCreator.createMockCompletionStage(90)).when(freeLancerService).handleSearch("Java","90","0");
        Mockito.doReturn(MockObjectCreator.createMockCompletionStage(85)).when(freeLancerService).handleSearch("Java","90","90");
        List<String> keywords = new ArrayList<String>();
        keywords.add("Java");
        List<SearchResult> listOfSearch =freeLancerService.getOrQuery(keywords);
        Assert.assertEquals(175,listOfSearch.get(0).getSizeOfList());
    }
}