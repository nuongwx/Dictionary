import java.io.*;
import java.util.*;

public class Dictionary extends Trie implements Serializable {
    HashMap<String, HashSet<TrieNode>> invertedIndex;

    List<TrieNode> history;
    @Serial
    private static final long serialVersionUID = 42L;

    public Dictionary(String filename) {
        super();
        history = new ArrayList<>();
        List<String> cache = new ArrayList<>();
        invertedIndex = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    StringBuilder formatted;
                    String[] parts = line.split("`");
                    String word = parts[0].trim();
                    String[] definitions = parts[1].trim().split("\\|\s*");
                    TrieNode node = insert(word, Arrays.asList(definitions));
                    formatted = new StringBuilder(word + "`" + String.join("|", definitions));
                    for (String definition : definitions) {
                        String[] words = definition.replaceAll("[^a-zA-Z\\d']", " ").toLowerCase().split("\\s+");
                        formatted.append("`").append(String.join(" ", words));
                        for (String w : words) {
                            if (!invertedIndex.containsKey(w)) {
                                invertedIndex.put(w, new HashSet<>());
                            }
                            invertedIndex.get(w).add(node);
                        }
                    }
                    cache.add(formatted.toString());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println(line);
                }
            }
            br.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
//        super.setCache(cache);
    }

    public TrieNode insert(String word, String[] definition) {
        word = word.trim();
        if(word.isBlank()) {
            return null;
        }
        TrieNode node = super.insert(word, List.of(definition));
        if (node != null) {
            for (String w : definition) {
                String[] words = w.replaceAll("[^a-zA-Z\\d']", " ").toLowerCase().split("\\s+");
                for (String word1 : words) {
                    if (!invertedIndex.containsKey(word1)) {
                        invertedIndex.put(word1, new HashSet<>());
                    }
                    invertedIndex.get(word1).add(node);
                }
            }
        }
        return node;
    }

    public boolean delete(TrieNode node) {
        if (node == null) {
            return false;
        }
        for (String w : node.definitions) {
            try {
                invertedIndex.get(w).remove(node);
            } catch (NullPointerException e) {
                System.out.println(w);
            }
        }
        return super.delete(node);
    }

    public TrieNode edit(TrieNode node, String newWord, List<String> newDefinition, boolean merge) {
        if (node == null) {
            return null;
        }
        for (String w : node.definitions) {
            try {
                invertedIndex.get(w).remove(node);
            } catch (NullPointerException e) {
                System.out.println(w);
            }
        }
        TrieNode newNode = super.edit(node, newWord, newDefinition, merge);
        for (String w : newDefinition) {
            String[] words = w.replaceAll("[^a-zA-Z\\d']", " ").toLowerCase().split("\\s+");
            for (String word1 : words) {
                if (!invertedIndex.containsKey(word1)) {
                    invertedIndex.put(word1, new HashSet<>());
                }
                invertedIndex.get(word1).add(newNode);
            }
        }
        return newNode;
    }

    private List<TrieNode> listAll() {
        return new ArrayList<>(invertedIndex.values()).stream().flatMap(Collection::stream).toList();
    }

    public List<TrieNode> getFromDefinition(String keyWords) {
        HashMap<TrieNode, Integer> nodes = new HashMap<>();
        if (keyWords.isBlank()) {
            return getFromPrefix("");
        }
        for (String word : keyWords.replaceAll("[^a-zA-Z\\d']", " ").toLowerCase().split("\\s+")) {
            List<TrieNode> node = new ArrayList<>();
            try {
                node = invertedIndex.get(word).stream().toList();
            } catch (NullPointerException e) {
//                xd
            }
            for (TrieNode n : node) {
                if (!nodes.containsKey(n)) {
                    nodes.put(n, 0);
                }
                nodes.put(n, nodes.get(n) + 1);
            }
        }
        List<TrieNode> result = new ArrayList<>(List.of(nodes.keySet().toArray(new TrieNode[0])));
        result.sort(Comparator.comparingInt(nodes::get).reversed());
        return result;
    }

    public TrieNode motd() {
        Random rand = new Random();
        List<String> keys = new ArrayList<>(invertedIndex.keySet());
        String randomKey = keys.get(rand.nextInt(keys.size()));
        List<TrieNode> nodes = invertedIndex.get(randomKey).stream().toList();
        return nodes.get(rand.nextInt(nodes.size()));
    }

    public List<TrieNode> quiz() {
        HashSet<TrieNode> nodes = new HashSet<>();
        while (nodes.size() < 4) {
            nodes.add(motd());
        }
        return List.of(nodes.toArray(new TrieNode[0]));
    }

    public static void main(String[] args) {
        Dictionary dict = new Dictionary("slang.txt");
        List<TrieNode> list = dict.getFromDefinition("");
        for (TrieNode node : list) {
            System.out.println(node);
        }
    }
}


