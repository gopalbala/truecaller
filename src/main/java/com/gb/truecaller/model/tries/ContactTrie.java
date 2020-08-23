package com.gb.truecaller.model.tries;

import java.util.ArrayList;
import java.util.List;

public class ContactTrie {

    private TrieNode root;
    private int indexOfSingleChild;

    public ContactTrie() {
        this.root = new TrieNode("");
    }


    public void insert(String key) {
        TrieNode tempTrieNode = root;
        for (int i = 0; i < key.length(); ++i) {
            char c = key.charAt(i);
            int asciiIndex = c;
            if (tempTrieNode.getChild(asciiIndex) == null) {
                TrieNode trieNode = new TrieNode(String.valueOf(c));
                tempTrieNode.setChild(asciiIndex, trieNode);
                tempTrieNode = trieNode;
            } else {
                tempTrieNode = tempTrieNode.getChild(asciiIndex);
            }
        }
        tempTrieNode.setLeaf(true);
    }

    public boolean search(String key) {
        TrieNode trieTrieNode = root;
        for (int i = 0; i < key.length(); ++i) {
            char c = key.charAt(i);
            int asciiIndex = c;
            if (trieTrieNode.getChild(asciiIndex) == null) {
                return false;
            } else {
                trieTrieNode = trieTrieNode.getChild(asciiIndex);
            }
        }
        return true;
    }

    public List<String> allWordsWithPrefix(String prefix) {
        TrieNode trieTrieNode = root;
        List<String> allWords = new ArrayList<>();
        for (int i = 0; i < prefix.length(); ++i) {
            char c = prefix.charAt(i);
            int asciiIndex = c;
            trieTrieNode = trieTrieNode.getChild(asciiIndex);
        }
        getSuffixes(trieTrieNode, prefix, allWords);
        return allWords;
    }

    public String longestCommonPrefix() {
        TrieNode trieTrieNode = root;
        String longestCharacerPrefix = "";
        while (countNumOfChildren(trieTrieNode) == 1 && !trieTrieNode.isLeaf()) {
            trieTrieNode = trieTrieNode.getChild(indexOfSingleChild);
            longestCharacerPrefix = longestCharacerPrefix + (char) (indexOfSingleChild + 'a');
        }
        return longestCharacerPrefix;
    }

    private int countNumOfChildren(TrieNode trieTrieNode) {
        int numOfChildren = 0;
        for (int i = 0; i < trieTrieNode.getChildren().length; ++i) {
            if (trieTrieNode.getChild(i) != null) {
                numOfChildren++;
                indexOfSingleChild = i;
            }
        }
        return numOfChildren;
    }

    private void getSuffixes(TrieNode trieNode, String prefix, List<String> allWords) {
        if (trieNode == null) return;
        if (trieNode.isLeaf()) {
            allWords.add(prefix);
        }
        for (TrieNode childTrieNode : trieNode.getChildren()) {
            if (childTrieNode == null) continue;
            String childCharacter = childTrieNode.getCharacter();
            getSuffixes(childTrieNode, prefix + childCharacter, allWords);
        }
    }

    public void delete(String key) {
        if ((root == null) || (key == null)) {
            System.out.println("Null key or Empty trie error");
            return;
        }

        deleteHelper(key, root, key.length(), 0);
        return;
    }

    private boolean deleteHelper(String key, TrieNode currentNode, int length, int level) {
        boolean deletedSelf = false;
        if (currentNode == null) {
            System.out.println("Key does not exist");
            return deletedSelf;
        }
        if (level == length) {
            if (currentNode.isLeaf())
            {
                currentNode = null;
                deletedSelf = true;
            } else {
                currentNode.setLeaf(false);
                deletedSelf = false;
            }
        } else {
			TrieNode childNode = currentNode.getChild(key.charAt(level));
			boolean childDeleted = deleteHelper(key, childNode, length, level + 1);
            if (childDeleted) {
                currentNode.setChild(key.charAt(level), null);
                if (currentNode.isLeaf()) {
                    deletedSelf = false;
                } else if (currentNode.getChildren().length > 0) {
                    deletedSelf = false;
                } else {
                    currentNode = null;
                    deletedSelf = true;
                }
            } else {
                deletedSelf = false;
            }
        }
        return deletedSelf;
    }
}















