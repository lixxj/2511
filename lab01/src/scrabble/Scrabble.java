package scrabble;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Scrabble {

    private String word = null;
    private Set<String> all_subwords = new HashSet<String>();

    public Scrabble(String word) {
        this.word = word;
        if (dictionary.contains(word)) {
            this.all_subwords.add(word);
        }
    }

    private List<String> dictionary = new ArrayList<String>(Arrays.asList("ab", "abe", "able", "ad", "ae", "ae",
            "ah", "al", "ale", "at", "ate", "ba", "bad", "be", "be", "bead", "bed", "bra", "brad", "bread", "bred",
            "cabble", "cable", "ea", "ea", "eat", "eater", "ed", "ha", "hah", "hat", "hate", "hater", "hath", "he",
            "heat", "heater", "heath", "heather", "heathery", "het", "in", "io", "ion", "li", "lin", "lion", "on",
            "program", "ra", "rad", "re", "rea", "read", "red", "sa", "sat", "scabble", "scrabble", "se", "sea", "seat",
            "seathe", "set", "seth", "sh", "sha", "shat", "she", "shea", "sheat", "sheath", "sheathe", "sheather",
            "sheathery", "sheth", "st", "te"));
          
    // Recursive function - Count the number of subwords
    private void findScore(String subword) {

        for (int i = 0; i < subword.length(); i++) {
            String new_str = subword;
            if (i != subword.length() - 1) {
                new_str = subword.substring(0, i) + subword.substring(i + 1, subword.length());
            } else {
                new_str = subword.substring(0, subword.length() - 1);
            }

            if (dictionary.contains(new_str)) {
                findScore(new_str);
                this.all_subwords.add(new_str);
            }
        }
    }

    public int score() {
        findScore(this.word);
        return this.all_subwords.size();
    }

    public static void main(String[] args) {
        Scrabble s = new Scrabble("scrabble");
        System.out.println(s.score()); 
    }

}