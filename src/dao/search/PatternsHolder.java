package dao.search;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author ANAGBLA  Joan */
public class PatternsHolder {
	//" " , "    " , ^"     " 
	//public static String space="\\s"; //use instead blank
	public static String blank="\\s+";
	public static String notBlank="\\S+";
	//[a-zA-Z0-9_] , [^a-zA-Z0-9_] , [a-zA-Z0-9_]+ , [^a-zA-Z0-9_]+
	public static String nonaccentuedchar="\\w";
	public static String notnonaccentuedchar="\\W";
	public static String aword="\\w+";
	public static String notWord="\\W+";
	//[0-9] , [^0-9] ,[0-9]+ , [^0-9]+
	public static String num="\\d";
	public static String notNum="\\D";
	public static String nums="\\d+";
	public static String notNums="\\D+";
	//.+@.+
	public static String email=".+@.+";

	public static final Map<String,String> accents = new HashMap<String, String>();
	static 
	{
		accents.put("e","[����e]");
		accents.put("a","[���a]");
		accents.put("u","[���u]");
		accents.put("o","[��o]");
		accents.put("i","[��i]");
		accents.put("y","[�y]");
		accents.put("c","[�c]");
	}

	/**Replace an accented word by a non-accented word (including �)
	 * @param word
	 * @return */
	public static String refine(String word){
		for(Entry<String, String> entry : accents.entrySet())
			word=word.trim().toLowerCase().replaceAll(entry.getValue(),entry.getKey());
		return word;
	}

	/**Replace a word by an accented string-regex built from the word
	 * @param text
	 * @return */
	public static String stain(String word){
		word=refine(word);
		for(Entry<String, String> entry : accents.entrySet())
			word=word.replaceAll(entry.getKey(),entry.getValue());
		return word;
	}

	public static void main(String[] args) {
		/*System.out.println(Pattern.matches(word, "574ythtgtrg"));
		System.out.println(Pattern.matches(word, "574yth�tgtrg"));
		System.out.println(wordSet("57:y4_y,th�t;gt-rg!ujh",notWord));*/

		/*List<String> words= Arrays.asList("","y","AB","JOE","NOEL","JOANE","JOANNE","TTANCK");
		for(String word : words)
			System.out.println("fuzzyfy("+word+"): "+fuzzyfy(word,2));*/	
		
		//System.out.println(refine("h���l��n���"));
		
		System.out.println(stain("v�lo"));
		System.out.println(stain("h���l��n���"));
	}	


	/**
	 * Return a set of words contained in a string 
	 * (only one occurence of a word)
	 * @param string
	 * @param pattern
	 * @return */
	public static Set<String> wordSet(String string,String pattern) {
		return new HashSet<String>(
				Arrays.asList(string.trim().toLowerCase().split(pattern)));}


	/**
	 * Return a list of words contained in a string 
	 * (only one occurence of a word)
	 * @param string
	 * @param pattern
	 * @return */
	public static List<String> wordList(String string,String pattern) {
		return Arrays.asList(string.trim().toLowerCase().split(pattern));}


	/**
	 * Return a String built from a collection of Strings
	 * @param wordsList
	 * @param old
	 * @param neew
	 * @return */
	public static String stringOfColl(Collection<String> wordsList,String old, String neew){
		return (wordsList.toString()
				.substring(1,wordsList.toString().length()-1)).replace(old,neew);
	}


	/**
	 * Return a String built from a collection of Patterns
	 * @param list
	 * @param old
	 * @param neew
	 * @return */
	public static String stringOfColl(List<Pattern> list, String old, String neew) {
		return (list.toString()
				.substring(1,list.toString().length()-1)).replace(old,neew);
	}	

	/**Return a fuzzy string-regex (with extra, substituted(or transposed) or missing 
	 * characters) from an original string.
	 * @param word
	 * @param fuzzy
	 * @return */  
	public static String fuzzyfy(String word,int fuzzy){
		System.out.println("\nmot="+word+" &fuzzy="+fuzzy);//debug
		if(word.length()==0) return ".{0,}"; //anything
		//the word must be at least one character longer than fuzzy 
		//(because fuzzy characters will be removed from it)
		if(!(word.length()>fuzzy)) return "^"+word.charAt(0);
		//TODO try to avoid the cut effect 
		//(^to find nothing but ^t find tutanck and then tot find also tutanck)
		StringBuilder fuzzyfied = new StringBuilder();
		//i begin at 1(not 0) because : 
		//-substring is end exclusive 
		//-beginning by i=0 is too large (tuo find (l<->(tu=..)ol))
		//-the first character must be harder than rock (it define the direction)
		for(int i=1;i<=word.length()-(fuzzy);i++){ 
			String prefix=word.substring(0,i);System.out.print("prefix : "+prefix+"  ");//debug
			String trunk=word.substring(i,i+fuzzy);System.out.print("trunk : "+trunk+"  ");//debug
			String suffix=word.substring(i+fuzzy);System.out.print("suffix : "+suffix+" ");//debug

			if(i==word.length()-(fuzzy))  
				fuzzyfied.append("^"+prefix+".{0,}"); //last fuzzyfying is "open end"
			else{
				fuzzyfied.append("^"+prefix/*+trunk.substring(0,j)*/
						+".{0,"+(fuzzy)+"}"+suffix+".{0,}"+"|");
				/*@1Failure : fuzzyfy is Not really generic (TODO). 
				  it is too specific for fuzzy=2 especially at this 2 followings 
				  lines that consider trunk's size to 1 
				  ((.replace(j,j+1,) instead of .replace(j,j+k))
				  .replace(j,j+k)) is generic for(k=1,k<=fuzzy;k++)
				  but it creates too much repetitions of some patterns
				  by eg the line before has to repeat only once
				  and the line after "fuzzy" times . 
				  How to conciliate that (line before run for trunk's size of)
				  How to have multistage that repeat only modified trunk size 
				  time for each. By eg the line before modify trunk.length chars
				  so it must repeat once the line after modify 1 char in trunk 
				  so it must repeat trunk.length times TODO later (no more time)*/
				for(int j=0;j<trunk.length();j++)
					//< instead of <= to avoid duplication on last replacement 
					//(replace "" by .{0,}): the second for do the same
					fuzzyfied.append("^"+prefix+new StringBuilder(trunk)
							.replace(j,j+1,".{0,"+(fuzzy)+"}")+suffix+".{0,}"+"|");
				for(int j=0;j<=trunk.length();j++)
					fuzzyfied.append("^"+prefix+new StringBuilder(trunk)
							.replace(j,j,".{0,"+(fuzzy)+"}")+suffix+".{0,}"+"|");
				//StringBuilder do more precise job than substring here because 
				//it makes it possible to replace the first character of the trunk
				//instead of /*+trunk.substring(0,j)*/ that didn't 
			}
			System.out.println(" --> fuzzyfied : "+fuzzyfied); //debug
		}
		return fuzzyfied.toString();
	}

}