import org.junit.*;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import play.libs.ws.WSResponse;
import services.FreelancerAPI;

import java.io.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import static play.inject.Bindings.bind;

/**
 * Test the FreelancerAPIImplementation
 * @author Aditi Aditi
 */
public class FreelancerAPIImplementationTest {
    private static FreelancerAPI freelancerAPI;

    private static Injector testApp;

    /**
     * Initialise the test application, bind the Freelancer API interface to its mock implementation
     * @author Aditi Aditi
     */
    @BeforeClass
    public static void initTestApp() {
        testApp = new GuiceInjectorBuilder()
                .overrides(bind(FreelancerAPI.class).to(FreelancerTestAPIImplentation.class))
                .build();
        freelancerAPI = testApp.instanceOf(FreelancerAPI.class);

    }


    /**
     * Test the freelancerAPI.search method
     * @throws ExecutionException ExecutionException
     * @throws InterruptedException InterruptedException
     * @throws IOException IOException
     * @author Aditi Aditi
     */
    @Test
    public void testSearch() throws ExecutionException, InterruptedException, IOException {
        WSResponse search = freelancerAPI.search("test","10","0").toCompletableFuture().get();
        String body = search.getBody();
        //System.out.println(body.toString());
        String searchJsonFile = getJsonFileAsString(File.separator + "test" + File.separator + "resources" +
                File.separator + "search.json");
        //System.out.println(searchJsonFile);
        Assert.assertEquals(searchJsonFile, body);
    }

    /**
     * Test the freelancerAPI.searchSkill method
     * @throws ExecutionException ExecutionException
     * @throws InterruptedException InterruptedException
     * @throws IOException IOException
     * @author Aditi Aditi
     */
    @Test
    public void testSearchSkill() throws ExecutionException, InterruptedException, IOException {
        WSResponse search = freelancerAPI.searchSkill("7").toCompletableFuture().get();
        String body = search.getBody();
        String searchJsonFile = getJsonFileAsString(File.separator + "test" + File.separator + "resources" +
                File.separator + "search.json");
        Assert.assertEquals(searchJsonFile, body);
    }


    /**
     * Method to convert json content to string
     * @param path  of the file
     * @return json as string
     * @throws IOException IOException
     * @author Aditi Aditi
     */
    private String getJsonFileAsString(String path) throws IOException {
        String filePath = new File("").getAbsolutePath();
        byte[] encoded = Files.readAllBytes(Paths.get(filePath.concat(path)));
        return new String(encoded, "UTF-8");
    }
}
