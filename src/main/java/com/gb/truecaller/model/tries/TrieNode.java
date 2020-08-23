package com.gb.truecaller.model.tries;

public class TrieNode {

	public static final int ALPHABET_SIZE = 256;

	private String character;
	private TrieNode[] children;
	private boolean leaf;
	private boolean visited;

	public TrieNode(String character) {
		this.character = character;
		children = new TrieNode[ALPHABET_SIZE];
	}

	public void setChild(int index, TrieNode trieNode) {
		this.children[index] = trieNode;
	}

	public String getCharacter() {
		return character;
	}

	public void setCharacter(String character) {
		this.character = character;
	}

	public TrieNode[] getChildren() {
		return children;
	}

	public void setChildren(TrieNode[] children) {
		this.children = children;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public TrieNode getChild(int index) {
		return children[index];
	}

	@Override
	public String toString() {
		return this.character;
	}

}
