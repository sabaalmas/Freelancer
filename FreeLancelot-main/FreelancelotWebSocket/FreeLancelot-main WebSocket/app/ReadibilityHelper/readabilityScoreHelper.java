package ReadibilityHelper;

import java.util.List;

public class readabilityScoreHelper {

	/**
	 * Calculates sum of total syllables present in list of words formed from preview description
	 *
	 * @param  words : list of words formed from preview description
	 * @return Sum of count of syllables
	 * @author Anurag Shekhar
	 */
	public static double countTotalSyllables(List<String> words) {
	    return words.stream()
	    		.mapToDouble(readabilityScoreHelper::countSyllables)
	    		.sum();
	  }

	/**
	 * Calculates total syllables count present in individual words formed from preview description
	 *
	 * @param  word : word from preview description
	 * @return Count of syllables
	 * @author Anurag Shekhar
	 */
	 public static double countSyllables(String word) {
		  double count = 0.0;
	    // null or empty strings have no syllables
	    if (word == null || word.trim().isEmpty())
	      return 0.0;

	    // words of three letters or shorter count as single syllables;
	    if (word.length()<=3)
	      return 1.0;
	    else 
	    	word = readabilityScoreHelper.ignoreLastSpCharacters(word);
	    
	    word = readabilityScoreHelper.removeConsecutiveVowels(word);
	    
	    for(char s:word.toCharArray())
	    {
	    	if (String.valueOf(s).matches("[aeiouAEIOUyY]"))
	    		count = count + 1;
	    }
	    
	    return count;
	  }
	
	//-es, -ed and -e (except -le) endings are ignored
	/**
	 * Removes last characters based on following rule : -es, -ed and -e (except -le) endings are ignored
	 *
	 * @param  word : word from preview description
	 * @return String with endings removed from word based on conditions
	 * @author Anurag Shekhar
	 */
		public static String ignoreLastSpCharacters (String word) {
			if( (word.matches("[a-zA-Z]+[^l]e$") && word.matches("[a-zA-Z]+e$")) || word.matches("[a-zA-Z]+es$") || word.matches("[a-zA-Z]+ed$") )
				{
				if (word.matches("[a-zA-Z]+e$"))
					word = word.substring(0, word.length()-1);
				else
					word = word.substring(0, word.length()-2);
				}
			return word;	
		}

	/**
	 * Removes consecutive vowels in word
	 *
	 * @param  word : word from preview description
	 * @return String with consecutive vowels removed from word
	 * @author Anurag Shekhar
	 */
		public static String removeConsecutiveVowels(String word) {
	        if(word.length()<=1)
	            return word;
	        if(word.toUpperCase().charAt(0)==word.toUpperCase().charAt(1) && String.valueOf(word.charAt(0)).matches("[aeiouAEIOUyY]"))
	            return removeConsecutiveVowels(word.substring(1));
	        else
	            return word.charAt(0) + removeConsecutiveVowels(word.substring(1));
	    }

	/**
	 * Returns Education Level based on Flesch Readability Index
	 *
	 * @param  index : Flesch Readability Index
	 * @return String : education level
	 * @author Anurag Shekhar
	 */
	public static String computeLevel (Double index) {
		String result = "";
		if( index < 10.0)
			result = "Professional";
		else if(index >= 10.0 && index < 30.0)
			result = "College graduate";
		else if(index >= 30.0 && index < 50.0)
			result = "College";
		else if(index >= 50.0 && index < 60.0)
			result = "10th to 12th grade";
		else if(index >= 60.0 && index < 70.0)
			result = "8th & 9th grade";
		else if(index >= 70.0 && index < 80.0)
			result = "7th grade";
		else if(index >= 80.0 && index < 90.0)
			result = "6th grade";
		else if(index >= 90.0)
			result = "5th grade";
		return result;
	}
	
	
	
	
	
	

}
