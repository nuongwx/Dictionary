package src;

import java.util.*;
import java.io.*;

public class TrieNode implements Serializable {
    TrieNode parent;
    HashMap<Character, TrieNode> children;
    boolean isEndOfWord;
    ArrayList<String> definitions;

    String word;


    public TrieNode() {
        children = new HashMap<Character, TrieNode>();
        isEndOfWord = false;
        definitions = new ArrayList<String>();
        word = "";
    }

    public TrieNode(TrieNode parent) {
        this();
        this.parent = parent;
    }

    private void addDefinition(String definition) {
        if (!definitions.contains(definition)) {
            definitions.add(definition);
        }
    }

    public void addDefinition(List<String> definitions) {
        for (String definition : definitions) {
            addDefinition(definition);
        }
    }

    public String[] getDefinitions() {
        return definitions.toArray(new String[0]);
    }

    public void removeDefinition(String definition) {
        definitions.remove(definition);
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String toString() {
        return word + "`" + String.join("|", definitions);
    }

    public String getWord() {
        return word;
    }
}

