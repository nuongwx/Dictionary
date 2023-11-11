package src;

import java.io.*;
import java.util.*;

public class Dictionary extends Trie implements Serializable {
    HashMap<String, HashSet<TrieNode>> invertedIndex;

    List<TrieNode> history;
    @Serial
    private static final long serialVersionUID = 42L;

    public Dictionary(String original, boolean fromOriginal) {
        super();
        history = new ArrayList<>();
        List<String> cache = new ArrayList<>();
        invertedIndex = new HashMap<>();
        BufferedReader br;
        if (!fromOriginal) {
            try {
                br = new BufferedReader(new FileReader("slangdb"));
                String line;
                if (!br.readLine().equals("#without-regex")) {
                    throw new IOException("Invalid file format");
                } else {
                    br.readLine();
                }
                while ((line = br.readLine()) != null) {
                    if (line.equals("Inverted Index")) {
                        break;
                    }
                    try {
                        String[] parts = line.split("`");
                        String word = parts[0];
                        String[] definitions = parts[1].split("\\|");
                        insert(word, Arrays.asList(definitions));
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println(line);
                    }
                }
                List<TrieNode> a = getFromPrefix("");
                while ((line = br.readLine()) != null) {
                    if (line.equals("History")) {
                        break;
                    }
//                    System.out.println(line);
                    String[] parts = line.split("`");
                    String key = parts[0];
                    if(key.isBlank() || parts.length < 2) {
                        // balls`
                        continue;
                    }
                    String[] words = parts[1].split("\\|");
                    HashSet<TrieNode> nodes = new HashSet<>();
                    for (String word : words) {
                        TrieNode node = get(word);
                        if (node != null) {
                            nodes.add(node);
                        }
                    }
                    invertedIndex.put(key, nodes);
                }
                while ((line = br.readLine()) != null) {
                    TrieNode node = get(line);
                    if (node != null) {
                        history.add(node);
                    }
                }
                br.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                System.err.println("Creating new database");
                fromOriginal = true;
            }
        }
        if (fromOriginal) {
            try {
                br = new BufferedReader(new FileReader(original));
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
        }
    }

    public void save() {
        try {
            FileWriter fw = new FileWriter("slangdb");
            fw.write("#without-regex\n");
            fw.write("src.Dictionary\n");
            for (TrieNode node : listAll()) {
                fw.write(node.toString() + "\n");
            }
            fw.write("Inverted Index\n");
            for (String word : invertedIndex.keySet()) {
                fw.write(word + "`" + String.join("|", invertedIndex.get(word).stream().map(n -> n.word).toList()) + "\n");
            }
            fw.write("History\n");
            for (TrieNode node : history) {
                fw.write(node.word + "\n");
            }
            fw.close();
        } catch (IOException i) {
            System.err.println(i.getMessage());
        }
    }

    public TrieNode insert(String word, String[] definition) {
        word = word.trim();
        if (word.isBlank()) {
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
        for (String definition : node.definitions) {
            String[] words = definition.replaceAll("[^a-zA-Z\\d']", " ").toLowerCase().split("\\s+");
            for (String w : words) {
                invertedIndex.get(w).remove(node);
                if(invertedIndex.get(w).isEmpty()) {
                    invertedIndex.remove(w);
                }
                System.out.println(w);
            }
        }
        return super.delete(node);
    }

    public TrieNode edit(TrieNode node, String newWord, List<String> newDefinition) {
        if (node == null) {
            return null;
        }
        delete(node);

        TrieNode newNode = insert(newWord, newDefinition);
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
        return getFromPrefix("");
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

}


