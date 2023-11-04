import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Dictionary extends Trie {
    HashMap<String, List<TrieNode>> invertedIndex;
    public Dictionary(String filename) {
        super();
        invertedIndex = new HashMap<String, List<TrieNode>>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    String[] parts = line.split("`");
                    String word = parts[0];
                    String[] definitions = parts[1].trim().split("\\|");
                    insert(word, definitions);

                    for(String definition : definitions) {
                        String[] words = definition.replaceAll("[^a-zA-Z\\d']", "").toLowerCase().split("\\s+");
                        for(String w : words) {
                            if (!invertedIndex.containsKey(w)) {
                                invertedIndex.put(w, new ArrayList<TrieNode>());
                            }
                            invertedIndex.get(w).add(root);
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println(line);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TrieNode delete(String word, String definition) {
        TrieNode node = super.delete(word);
        if (node != null) {
            for(String w : definition.replaceAll("[^a-zA-Z\\d']", "").toLowerCase().split("\\s+")) {
                invertedIndex.get(w).remove(node);
            }
        }
        return node;
    }

    public static void main(String[] args) {
    }
}


