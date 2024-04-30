package cycloplex;

import java.util.HashMap;

public class wordsLang {

    public static final HashMap<String, String[]> langBlocks = new HashMap<String, String[]>() {
        {
            /**\/ ;*/
            String[] blocks = {"{", "}"};
            put("java", blocks);

            blocks = new String[]{"{", "}"};
            put("js", blocks);
        }
    };

    public static final HashMap<String, String[]> langWords = new HashMap<String, String[]>() {
        {
            /**\/ ;*/
            String[] keywords = {"if","else","while","case","for","switch","do","continue","break","&&","||","?",":","catch","finally","throw","throws","default","return"};
            put("java", keywords);

            keywords = new String[]{"if","else","while","case","for","switch","do","continue","break","&&","||","?",":","catch","finally","throw","throws","default","return"};
            put("js", keywords);
        }
    };

}