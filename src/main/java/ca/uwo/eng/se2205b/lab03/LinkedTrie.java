package ca.uwo.eng.se2205b.lab03;

import javax.annotation.Nonnull;
import java.util.*;

/*
 * Implement a Trie via linking Nodes.
 */
public class LinkedTrie implements Trie {

    private TrieNode root;

    public class TrieNode {
        private TreeMap<Character, TrieNode> children;
        private char character;
        private boolean terminates;

        /**
         * Default Constructor
         * Used to initialize the root and other nodes
         */
        public TrieNode(){
            children = new TreeMap<>();
            terminates = false;
        }

        /**
         * Constructor
         * @param c sets the character of the node
         * Used to set all nodes except the root
         */
        public TrieNode (@Nonnull char c){
            this();
            this.character = c;
        }

        /**
         * @return returns the character that maps to the node
         */
        public char getchar(){
            return character;
        }

        /**
         * returns the node that is mapped by child c
         * @param c is the character key of the node
         * @return returns the node mapped by character c
         */
        public TrieNode getChild(char c) {
            return children.get(c);
        }

        /**
         * Returns whether this node represents the end of a complete word.
         * @return terminates
         */
        public boolean terminates() {
            return terminates;
        }

        /**
         * Set whether this node is the end of a complete word
         * @param t boolean to set the terminates property
         */
        public void setTerminates(boolean t) {
            terminates = t;
        }

        /**
         * Find the number of words in the trie
         * @return the number of words in the trie
         */
        public int size() {
            int size = 0;
            for (Map.Entry<Character, TrieNode> entry : children.entrySet()) {
                TrieNode key = entry.getValue();
                if (key.terminates())
                    size++;
                size += key.size();
            }
            return size;
        }

        /**
         * Adds the word in the trie
         * @param word the word that we are adding
         * @return boolean if the word is added
         */
        public boolean addword(String word){
            //check if the word is null or empty
            if (word == null || word.isEmpty())
                return false;
            word= word.toLowerCase();
            //get first character
            char first_letter = word.charAt(0);

            //find if there is a child that has same char
            TrieNode child = getChild(first_letter);
            //no child declared as this char
            if (child == null){
                child = new TrieNode(first_letter);
                children.put(first_letter,child);
            }
            //if the word is longer than 1. recurs again to make a tree
            if (word.length() > 1)
                child.addword(word.substring(1));
            else //when word is all inputted, set the character to true
                child.setTerminates(true);
            //since it eliminated false case return true
            return true;
        }

        /**
         * Iterating through the prefix in the trie
         * @param word the prefix that we are iterating to
         * @return return the node of the last character in the prefix
         */
        private TrieNode prefixNode(@Nonnull String word){
            char firstChar = word.charAt(0);
            word = word.toLowerCase();
            TrieNode child = getChild(firstChar);
            if(word.length() == 1)
                return child;
            else//go in to child class and look if there are next char
                return child.prefixNode(word.substring(1));
        }

        /**
         * Returns a sorted list of the prefixed words
         * @param prefix of the word
         * @param N the number of words in the list
         * @return a sorted list of all the words
         */
        public SortedSet<String> getNextN(@Nonnull String prefix, int N) throws IllegalArgumentException{
            prefix = prefix.toLowerCase();
            TrieNode prefixNode = this.prefixNode(prefix);
            SortedSet<String> word = new TreeSet<>();

            if(prefixNode.terminates)
                word.add(prefix);
            return prefixNode.completeWords(word, N, prefix);
        }

        /**
         * @param prewords the words in the list thus far
         * @param N maximum number of words we want in the list {@Code -1} return all the words
         * @param prefix the prefix of the word
         * @return a sorted set of all the words found based on the parameters
         */
        public SortedSet<String> completeWords(SortedSet<String> prewords, int N, String prefix) {
            SortedSet<String> word = new TreeSet<>();
            word.addAll(prewords);
            StringBuilder stbModified = new StringBuilder();
            stbModified.append(prefix);

            for (Map.Entry<Character, TrieNode> entry : this.children.entrySet()) {
                if(word.size() == N)
                    break;
                TrieNode key = entry.getValue();
                stbModified.append(entry.getKey());
                if (key.terminates) {
                    word.add(stbModified.toString());
                    if(key.children.isEmpty()){
                        stbModified = new StringBuilder(prefix);
                    }
                }
                word.addAll(key.completeWords(word, N, stbModified.toString()));
                stbModified = new StringBuilder(prefix);
            }
            return word;
        }

        /**
         * Looks if an input is in the trie
         * @param word the word being searched for
         * @return true of found, false otherwise
         */
        public boolean contains(@Nonnull String word) {
            //get first char and get the child
            TrieNode lastnode = getChild(word.charAt(0));
            if (lastnode == null)
                return false;
            //return the last letter child, and check if it terminates ex [ifff , ifffff] and check "if"
            return word.length() > 1 ? lastnode.contains(word.substring(1)) : true;
        }
    }

    public LinkedTrie() {
        root = null;
    }

    /**
     * Returns the size of the trie
     * @return an integer of the number of words in the trie
     */
    @Override
    public int size() {
        if (isEmpty())
            return 0;
        return root.size();
    }

    /**
     * @return if the trie is empty aka if the root is null
     */
    @Override
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Places a word in the trie
     * @param word that we are adding
     * @return a boolean of whether the word was added
     */
    @Override
    public boolean put(String word) {
        //create root node if it is empty
        if(isEmpty()){
            root = new TrieNode();
            return root.addword(word);
        }
        else
            return root.addword(word);
    }

    /**
     * Put all words in a sorted list in the
     * @param words Words to put into the Trie
     * @return an integer of all the words that we are adding
     */
    @Override
    public int putAll(SortedSet<? extends String> words) {
        int count = 0;
        //create root node if it is empty
        if (isEmpty())
            root = new TrieNode();
        for (String word : words) {
            word.toLowerCase();
            //if word are successfully inputted, increment count
            if (root.addword(word))
                count++;
        }
        //return number of successful words inputted
        return count;
    }

    /**
     * Returns a sorted list of the prefixed words
     * @param prefix of the word
     * @param N the number of words in the list
     * @return a sorted list of all the words
     */
    @Override
    public SortedSet<String> getNextN(@Nonnull String prefix, int N) throws IllegalArgumentException {
        if (prefix == null || prefix.isEmpty())
            return null;
        if(prefix == ""|| N<-1)
            throw new IllegalArgumentException();
        prefix = prefix.toLowerCase();
        if(N == 0 || !contains(prefix))
            return null;
        else
            return root.getNextN(prefix, N);
    }

    /**
     * @param word Word to look for
     * @return true if the word is in the trie, false otherwise (or false if word is empty)
     */
    @Override
    public boolean contains(@Nonnull String word) throws IllegalArgumentException {
        TrieNode lastnode = root;
        //return if it has nothing, or node is empty
        if (word.isEmpty())
            throw  new IllegalArgumentException();
        word= word.toLowerCase();
        return isEmpty()? false : root.contains(word);
    }
}