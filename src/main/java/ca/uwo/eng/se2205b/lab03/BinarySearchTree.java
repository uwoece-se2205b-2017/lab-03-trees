package ca.uwo.eng.se2205b.lab03;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.ArrayList;

/**
 * Binary Search Tree
 */
@ParametersAreNonnullByDefault
public class BinarySearchTree<E extends Comparable<E>> implements Tree<E> {
    private BinaryNode<E> root;

    /**
     * Internal Node structure used for the BinaryTree
     * @param <E extends Comparable<E>>
     */
    static class BinaryNode<E extends Comparable<E>> implements Node<E> {
        BinaryNode<E> parent;
        BinaryNode<E> left;
        BinaryNode<E> right;
        E value;

        BinaryNode(E elem, @Nullable BinaryNode parent) {
            value = elem;
            this.parent = parent;
        }

        @Nullable BinaryNode<E> getLeft() {
            return this.left;
        }

        @Nullable BinaryNode<E> getRight() {
            return this.right;
        }

        @Nullable BinaryNode<E> getParent() {
            return this.parent;
        }

        @Override
        public int size() {
            int nsize = 1;
            if(this.left != null)
                nsize += this.left.size();
            if(this.right!= null)
                nsize += this.right.size();
            return nsize;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public int height() {
            int h = 0;
            if(this.isLeaf())
                return 0;
            else if(this.left != null && this.right == null)
                return left.height() + 1;
            else if(this.left == null && this.right != null)
                return right.height() + 1;
            else
                return Math.max(right.height(), left.height()) + 1;
        }

        @Override
        public boolean isProper() {
            if(this.isLeaf())
                return true;
            else if(this.left != null && this.right != null){
                return (this.left.isProper() && this.right.isProper());
            }
            else
                return false;
        }

        @Override
        public boolean isBalanced() {
            if(isLeaf())
                return true;
            else if(this.left == null && this.right != null){
                if(this.right.height() >= 1)
                    return false;
                else
                    return this.right.isBalanced();
            }
            else if(this.left != null && this.right == null){
                if(this.left.height() >= 1)
                    return false;
                else
                    return this.left.isBalanced();
            }
            else {
                if(Math.abs(this.left.height() - this.right.height()) > 1)
                    return false;
                else
                    return (this.left.isBalanced() && this.right.isBalanced());
            }
        }

        @Override
        public E getElement() {
            return this.value;
        }

        @Nonnull
        @Override
        public Collection<? extends Node<E>> children() {
            List<Node<E>> children = new ArrayList<>(2);
            children.add(this.left);
            children.add(this.right);
            return children;
        }

        @Override
        public boolean isInternal() {
            return (this.left != null || this.right != null);
        }

        @Override
        public boolean isLeaf() {
            return (this.left == null && this.right == null);
        }

        public boolean contains(E element){
            if(this.value.compareTo(element) == 0)
                return true;
            else if(this.value.compareTo(element) > 0 && this.left != null)
                return this.left.contains(element);
            else if(this.right != null)
                return this.right.contains(element);
            else
                return false;
        }

        public boolean put(E element){
            if(this.value.compareTo(element) > 0) {
                if(this.left == null) {
                    this.left = new BinaryNode<E>(element, this);
                    return true;
                }
                return this.left.put(element);
            }
            else if(this.value.compareTo(element) < 0) {
                if(this.right == null) {
                    this.right = new BinaryNode<E>(element, this);
                    return true;
                }
                return this.right.put(element);
            }
            else
                return false;
        }

        public boolean remove(E element){
            if(this.value.compareTo(element) == 0){
                if(this.left != null && this.right != null){
                    if(this.left.height() > this.right.height()){
                        this.value = this.left.maximum();
                        left.remove(this.value);
                    }
                    else{
                        this.value = this.right.minimum();
                        right.remove(this.value);
                    }
                }
                else if(this.isLeaf()){
                    if(this.parent.left == this)
                        this.parent.left = null;
                    else
                        this.parent.right = null;
                }
                else if(this.parent.left == this){
                    BinaryNode temp = (left != null) ? left : right;
                    this.parent.left = temp;
                    temp.parent = this.parent;
                }
                else {
                    BinaryNode temp = (left != null) ? left : right;
                    this.parent.right = temp;
                    temp.parent = this.parent;
                }
                return true;
            }
            else if(this.value.compareTo(element) > 0 && this.left != null){
                return this.left.remove(element);
            }
            else if(this.right != null)
                return this.right.remove(element);
            else
                return false;
        }

        private E minimum(){
            if (this.left == null)
                return this.value;
            else
                return this.left.minimum();
        }
        private E maximum(){
            if (this.right == null)
                return this.value;
            else
                return this.right.maximum();
        }

    }

    public BinarySearchTree() {
        root = null;
    }

    @Override
    public int size() {
        if(this.isEmpty())
            return 0;
        else
            return root.size();
    }

    @Override
    public boolean isEmpty() {
        return (root == null);
    }

    @Override
    public int height() {
        if(isEmpty())
            return 0;
        else
            return root.height();
    }

    @Override
    public boolean isProper() {
        if(isEmpty()){
            return true;
        }
        else
            return root.isProper();
    }

    @Override
    public boolean isBalanced() {
        if(isEmpty()){
            return true;
        }
        else
            return root.isBalanced();
    }


    public class InOrderBinaryIterator<E extends Comparable<E>> implements Iterator<E> {
        Stack<BinaryNode> stack;
        BinaryNode currentPos = null;

        public InOrderBinaryIterator() {
            stack = new Stack<BinaryNode>();
            BinaryNode temp = root;
            while (temp != null) {
                stack.push(temp);
                temp = temp.left;
            }
        }

        public boolean hasNext() {
            return !stack.isEmpty();
        }
        public E next() {
            BinaryNode node = stack.pop();
            currentPos = node;

            E result = (E) node.value;
            if (node.right != null) {
                node = node.right;
                while (node != null) {
                    stack.push(node);
                    node = node.left;
                }
            }
            return result;
        }
        @Override
        public void remove(){
            if(currentPos != null)
                currentPos.remove(currentPos.getElement());
            else
                throw new NullPointerException();
        }
    }

    public class PostOrderBinaryIterator<E extends Comparable<E>> implements Iterator<E> {
        Stack<BinaryNode> stack = new Stack<>();
        BinaryNode currentPos = null;

        public PostOrderBinaryIterator() {
            findNextLeaf(root);
        }

        private void findNextLeaf(BinaryNode cur) {
            while (cur != null) {
                stack.push(cur);
                if (cur.left != null) {
                    cur = cur.left;
                } else {
                    cur = cur.right;
                }
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            BinaryNode res = stack.pop();
            currentPos = res;
            if (!stack.isEmpty()) {
                BinaryNode top = stack.peek();
                if (res == top.left) {
                    findNextLeaf(top.right); // find next leaf in right sub-tree
                }
            }
            return (E) res.value;
        }

        @Override
        public void remove(){
            if(currentPos != null)
                currentPos.remove(currentPos.getElement());
            else
                throw new NullPointerException();
        }
    }

    public class PreOrderBinaryIterator<E extends Comparable<E>> implements Iterator<E> {
        ArrayDeque<BinaryNode> stack = new ArrayDeque<>();
        BinaryNode currentPos = null;

        /** Constructor */
        public PreOrderBinaryIterator() {
            if (root != null) {
                stack.push(root); // add to end of queue
            }
        }
        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            BinaryNode res = stack.pop(); // retrieve and remove the head of queue
            currentPos = res;
            if (res.right != null) stack.push(res.right);
            if (res.left != null) stack.push(res.left);

            return (E) res.value;
        }

        @Override
        public void remove(){
            if(currentPos != null)
                currentPos.remove(currentPos.getElement());
            else
                throw new NullPointerException();
        }
    }

    @Override
    public Iterator<E> iterator(Traversal how) {
        switch (how){
            case InOrder:{
                return new InOrderBinaryIterator<>();
            }
            case PostOrder: {
                return new PostOrderBinaryIterator<>();
            }
            case PreOrder: {
                return new PreOrderBinaryIterator<>();
            }
            default:
                return null;
        }
    }
    @Override
    public boolean contains(E element) {
        if(this.isEmpty()){
            return false;
        }
        return root.contains(element);
    }
    @Override
    public boolean put(E element) {
        if(this.isEmpty()){
            root = new BinaryNode<E>(element, null);
            return true;
        }
        return root.put(element);
    }

    @Override
    public boolean remove(E element) {
        if (this.isEmpty()) {
            return false;
        }
        if(root.value.compareTo(element) == 0){
            if(root.left != null && root.right != null){
                if(root.left.height() > root.right.height()){
                    root.value = root.left.maximum();
                    root.left.remove(root.value);
                }
                else{
                    root.value = root.right.minimum();
                    root.remove(root.value);
                }
            }
            else if(root.isLeaf())
                root = null;
            else if(root.left != null){
                root = root.left;
                root.parent = null;
            }
            else {
                root = root.right;
                root.parent = null;
            }
            return true;
        }
        else
            return root.remove(element);
    }

    @Nullable
    @Override
    public BinaryNode<E> getRoot() {
        return this.root;
    }
}