import java.io.*;
import java.util.*;

public class Trie implements Serializable {
    TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public TrieNode insert(String word, List<String> definition) {
        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = Character.toLowerCase(word.charAt(i));
            TrieNode node = current.children.get(ch);
            if (node == null) {
                node = new TrieNode(current);
                current.children.put(ch, node);
            } else {
//                System.out.println("Append definition to " + node);
//                if (node.isEndOfWord) {
//                    node.addDefinition(definition);
//                    return null;
//                }
            }
            current = node;
        }
        current.isEndOfWord = true;
        current.addDefinition(definition);
        current.setWord(word);
        return current;
    }

    public boolean delete(TrieNode node) {
        if (node == null) {
            return false;
        }
        node.isEndOfWord = false;
        node.definitions.clear();
        // traceback
//        while (node.children.isEmpty() && !node.isEndOfWord) {
//            node.parent.children.remove(node.word.charAt(node.word.length() - 1));
//            node = node.parent;
//        }
        return true;
    }

    // really sketchy
    public TrieNode edit(TrieNode node, String newWord, List<String> newDefinition, boolean merge) {
        if (node == null) {
            return null;
        }
        if(merge == false) {
            TrieNode overwritten = get(newWord);
            if(overwritten != null) {
                overwritten.definitions.clear();
                overwritten.addDefinition(newDefinition);
            } else {
                return insert(newWord, newDefinition);
            }
        }
        delete(node);
        return insert(newWord, newDefinition);
    }

//    public TrieNode edit(TrieNode updatedNode) {
//        if (updatedNode == null) {
//            return null;
//        }
//        delete(updatedNode);
//        return insert(updatedNode.word, updatedNode.definitions);
//    }

    public void save() {
        try {
            FileWriter fw = new FileWriter("funkyname.txt");
            BufferedWriter bw = new BufferedWriter(fw);
//            for (String line : cache) {
//                bw.write(line);
//                bw.newLine();
//            }
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    public void printAllWords(TrieNode node, String word) {
//        if (node.isEndOfWord) {
//            System.out.println(word + ": " + node.definitions);
//        }
//        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
//            printAllWords(entry.getValue(), word + entry.getKey());
//        }
//    }
//
//    public void printAllWords() {
//        printAllWords(root, "");
//    }
//
//    public void printFromPrefix(String prefix) {
//        TrieNode current = root;
//        for (int i = 0; i < prefix.length(); i++) {
//            char ch = Character.toLowerCase(prefix.charAt(i));
//            TrieNode node = current.children.get(ch);
//            if (node == null) {
//                return;
//            }
//            current = node;
//        }
//        printAllWords(current, prefix);
//    }

    public List<TrieNode> getFromPrefix(String prefix) {
        TrieNode current = root;
        List<TrieNode> nodes = new ArrayList<>();
        for (int i = 0; i < prefix.length(); i++) {
            char ch = Character.toLowerCase(prefix.charAt(i));
            TrieNode node = current.children.get(ch);
            if (node == null) {
                return nodes;
            }
            current = node;
        }

        getFromPrefix(current, nodes);
        nodes.sort((a, b) -> a.word.compareTo(b.word));
        return nodes;
    }

    public void getFromPrefix(TrieNode node, List<TrieNode> nodes) {
        if (node.isEndOfWord) {
            nodes.add(node);
        }
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            getFromPrefix(entry.getValue(), nodes);
        }
    }

    public TrieNode get(String word) {
        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = Character.toLowerCase(word.charAt(i));
            TrieNode node = current.children.get(ch);
            if (node == null) {
                return null;
            }
            current = node;
        }
        return current;
    }
    public static void main(String[] args) {
        Trie trie = new Trie();
        trie.insert("hello", List.of("xin chao"));
        trie.insert("hel", List.of("xin chao"));
        trie.insert("he", List.of("xin chao"));
        trie.insert("kllo", List.of("xin chao"));
        trie.insert("klo", List.of("xin chao"));
        List<TrieNode> nodes = trie.getFromPrefix("he");
        trie.save();
        for (TrieNode node : nodes) {
            System.out.println(node);
        }

    }
}

