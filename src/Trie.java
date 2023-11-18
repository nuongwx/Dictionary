package src;

import java.io.*;
import java.util.*;

public class Trie implements Serializable {
    TrieNode root;
    Integer size = 0;

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
            }
            current = node;
        }
        current.isEndOfWord = true;
        current.addDefinition(definition);
        current.setWord(word);
        size++;
        return current;
    }

    public boolean delete(TrieNode node) {
        if (node == null) {
            return false;
        }
        node.isEndOfWord = false;
        size--;
        return true;
    }

    public void save() {
    }

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
}

