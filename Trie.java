import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Trie {
    TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public TrieNode insert(String word, Object definition) {
        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = Character.toLowerCase(word.charAt(i));
            TrieNode node = current.children.get(ch);
            if (node == null) {
                node = new TrieNode(current);
                current.children.put(ch, node);
            }
            current = node;
        }
        current.isEndOfWord = true;
        if (definition instanceof String) {
            current.addDefinition((String) definition);
        } else if (definition instanceof String[]) {
            current.addDefinition((String[]) definition);
        }
        return current;
    }


    public TrieNode delete(String word) {
        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = Character.toLowerCase(word.charAt(i));
            TrieNode node = current.children.get(ch);
            if (node == null) {
                return null;
            }
            current = node;
        }
        current.isEndOfWord = false;
        current.definitions.clear();
        while (current.children.isEmpty() && !current.isEndOfWord) {
            current.parent.children.remove(word.charAt(word.length() - 1));
            current = current.parent;
        }
        return current;
    }

    public void printAllWords(TrieNode node, String word) {
        if (node.isEndOfWord) {
            System.out.println(word + ": " + node.definitions);
        }
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            printAllWords(entry.getValue(), word + entry.getKey());
        }
    }

    public void printAllWords() {
        printAllWords(root, "");
    }

    public void printFromPrefix(String prefix) {
        TrieNode current = root;
        for (int i = 0; i < prefix.length(); i++) {
            char ch = Character.toLowerCase(prefix.charAt(i));
            TrieNode node = current.children.get(ch);
            if (node == null) {
                return;
            }
            current = node;
        }
        printAllWords(current, prefix);
    }
}

