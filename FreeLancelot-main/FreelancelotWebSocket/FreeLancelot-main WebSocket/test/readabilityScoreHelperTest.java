import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ReadibilityHelper.readabilityScoreHelper;
import org.junit.Test;

public class readabilityScoreHelperTest {

	/**
	 * Test the readabilityScoreHelper.countTotalSyllables for an existing Search Result
	 *
	 * @author Anurag Shekhar
	 */
	readabilityScoreHelper r= new readabilityScoreHelper();
	@Test
	public void testCountTotalSyllables() {
		List<String> words = Arrays.asList("I", "am", "a", "student", "at", "Concordia", "University");
		assertEquals(15.0, readabilityScoreHelper.countTotalSyllables(words), 0.0);
		words = new ArrayList<String>();
		assertEquals(0.0, readabilityScoreHelper.countTotalSyllables(words), 0.0);
	}

	/**
	 * Test the readabilityScoreHelper.countSyllables for an existing Search Result
	 *
	 * @author Anurag Shekhar
	 */
	@Test
	public void testCountSyllables() {

		// Case : Empty String
		assertEquals(0.0, readabilityScoreHelper.countSyllables(""), 0.0);

		// Case : Null String
		assertEquals(0.0, readabilityScoreHelper.countSyllables(null), 0.0);

		// Case : Word with length of 3
		assertEquals(1.0, readabilityScoreHelper.countSyllables("ace"), 0.0);

		// Case : Word with length less than 3
		assertEquals(1.0, readabilityScoreHelper.countSyllables("an"), 0.0);

		// Case : Word ending with "e" and last two characters are "le"
		assertEquals(2.0, readabilityScoreHelper.countSyllables("Angle"), 0.0);

		// Case : Word ending with "e" and last two characters are not "le"
		assertEquals(2.0, readabilityScoreHelper.countSyllables("Complete"), 0.0);

		// Case : Word ending with "es"
		assertEquals(1.0, readabilityScoreHelper.countSyllables("Wolves"), 0.0);

		// Case : Word ending with "ed"
		assertEquals(2.0, readabilityScoreHelper.countSyllables("indeed"), 0.0);

		// Case : Word with consecutive vowels
		assertEquals(3.0, readabilityScoreHelper.countSyllables("AaaamaAaAzOoooOOn"), 0.0);

		// Case : Other Words
		assertEquals(5.0, readabilityScoreHelper.countSyllables("eutopia"), 0.0);
	}

	/**
	 * Test the readabilityScoreHelper.ignoreLastSpCharacters for an existing Search Result
	 *
	 * @author Anurag Shekhar
	 */
	@Test
	public void testIgnoreLastSpCharacters() {

		assertEquals("Individual", readabilityScoreHelper.ignoreLastSpCharacters("Individual"));

		assertEquals("Angle", readabilityScoreHelper.ignoreLastSpCharacters("Angle"));

		assertEquals("Complet", readabilityScoreHelper.ignoreLastSpCharacters("Complete"));

		assertEquals("Wolv", readabilityScoreHelper.ignoreLastSpCharacters("Wolves"));

		assertEquals("Inde", readabilityScoreHelper.ignoreLastSpCharacters("Indeed"));

		assertEquals("", readabilityScoreHelper.ignoreLastSpCharacters(""));

	}

	/**
	 * Test the readabilityScoreHelper.removeConsecutiveVowels for an existing Search Result
	 *
	 * @author Anurag Shekhar
	 */
	@Test
	public void testRemoveConsecutiveVowels() {

		assertEquals("A", readabilityScoreHelper.removeConsecutiveVowels("A"));

		assertEquals("ANURaG", readabilityScoreHelper.removeConsecutiveVowels("AaaaaaANUuuuuuuUuuuURAaaaG"));

		assertEquals("eutopia", readabilityScoreHelper.removeConsecutiveVowels("eutopia"));
	}

	/**
	 * Test the readabilityScoreHelper.computeLevel for an existing Search Result
	 *
	 * @author Anurag Shekhar
	 */
	@Test
	public void testComputeLevel() {

		assertEquals("Professional", readabilityScoreHelper.computeLevel(5.0));

		assertEquals("College graduate", readabilityScoreHelper.computeLevel(10.0));
		assertEquals("College graduate", readabilityScoreHelper.computeLevel(20.2));

		assertEquals("College", readabilityScoreHelper.computeLevel(30.0));
		assertEquals("College", readabilityScoreHelper.computeLevel(45.0));

		assertEquals("10th to 12th grade", readabilityScoreHelper.computeLevel(50.0));
		assertEquals("10th to 12th grade", readabilityScoreHelper.computeLevel(55.0));

		assertEquals("8th & 9th grade", readabilityScoreHelper.computeLevel(60.0));
		assertEquals("8th & 9th grade", readabilityScoreHelper.computeLevel(65.0));

		assertEquals("7th grade", readabilityScoreHelper.computeLevel(70.0));
		assertEquals("7th grade", readabilityScoreHelper.computeLevel(75.0));

		assertEquals("6th grade", readabilityScoreHelper.computeLevel(80.0));
		assertEquals("6th grade", readabilityScoreHelper.computeLevel(85.0));

		assertEquals("5th grade", readabilityScoreHelper.computeLevel(90.0));
		assertEquals("5th grade", readabilityScoreHelper.computeLevel(95.0));

		assertEquals("5th grade", readabilityScoreHelper.computeLevel(100.0));
		assertEquals("Professional", readabilityScoreHelper.computeLevel(-1.0));
	}
}