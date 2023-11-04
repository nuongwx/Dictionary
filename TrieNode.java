import java.util.*;
import java.io.*;

public class TrieNode {
    TrieNode parent;
    HashMap<Character, TrieNode> children;
    boolean isEndOfWord;
    ArrayList<String> definitions;

    public TrieNode() {
        children = new HashMap<Character, TrieNode>();
        isEndOfWord = false;
        definitions = new ArrayList<String>();
    }

    public TrieNode(TrieNode parent) {
        this();
        this.parent = parent;
    }

    public void addDefinition(String definition) {
        if (!definitions.contains(definition)) {
            definitions.add(definition);
        }
    }

    public void addDefinition(String[] definitions) {
        for (String definition : definitions) {
            addDefinition(definition);
        }
    }

    public void removeDefinition(String definition) {
        definitions.remove(definition);
    }
}

