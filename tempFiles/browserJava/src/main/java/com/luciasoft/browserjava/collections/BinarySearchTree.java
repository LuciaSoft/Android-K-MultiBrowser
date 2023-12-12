package com.luciasoft.browserjava.collections;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

class Node<T>
{
    Node(T data, Node<T> prnt)
    {
        this.data = data;
        this.prnt = prnt;
    }

    T data;
    Node<T> left;
    Node<T> rght;
    Node<T> prnt;
}

public class BinarySearchTree<T> implements Iterable<T>
{
    public BinarySearchTree(Comparator<T> comparator)
    {
        this.comparator = comparator;
    }

    Node<T> root;

    int count;

    Comparator<T> comparator;

    public int getCount()
    {
        return count;
    }

    public Comparator<T> getComparator()
    {
        return comparator;
    }

    public void add(T data)
    {
        if (root == null)
        {
            root = new Node(data, null);

            count++;

            return;
        }

        add(data, root);
    }

    void add(T data, Node<T> node)
    {
        int cmp = comparator.compare(data, node.data);

        if (cmp < 0)
        {
            if (node.left == null)
            {
                node.left = new Node(data, node);

                count++;

                return;
            }

            add(data, node.left);

            return;
        }

        if (cmp > 0)
        {
            if (node.rght == null)
            {
                node.rght = new Node(data, node);

                count++;

                return;
            }

            add(data, node.rght);
        }

        // cmp == 0
    }

    public T getData(T data)
    {
        if (root == null) return null;

        return getData(data, root);
    }

    T getData(T data, Node<T> node)
    {
        int cmp = comparator.compare(data, node.data);

        if (cmp < 0)
        {
            if (node.left == null) return null;

            return getData(data, node.left);
        }

        if (cmp > 0)
        {
            if (node.rght == null) return null;

            return getData(data, node.rght);
        }

        return node.data;
    }

    public void reset()
    {
        root = null;
        count = 0;
    }

    public void traverseInOrder(DoWithData<T> doWithData)
    {
        traverseInOrder(root, doWithData);
    }

    void traverseInOrder(Node<T> curr, DoWithData<T> doWithData)
    {
        if (curr == null) return;

        traverseInOrder(curr.left, doWithData);
        doWithData.doWithData(curr.data);
        traverseInOrder(curr.rght, doWithData);
    }

    public ArrayList<T> toList()
    {
        final ArrayList<T> list = new ArrayList<>();

        T[] one = null;

        traverseInOrder(new DoWithData<T>()
        {
            @Override
            public void doWithData(T data)
            {
                list.add(data);
            }
        });

        return list;
    }

    @Override
    public Iterator<T> iterator()
    {
        return new TreeIterator<>(this);
    }
}

class TreeIterator<T> implements Iterator<T>
{
    TreeIterator(BinarySearchTree<T> tree)
    {
        this.tree = tree;

        initialize();
    }

    Node<T> curr = null;

    void initialize()
    {
        curr = getLeftMost(tree.root);
    }

    @Override
    public boolean hasNext()
    {
        return curr != null;
    }

    @Override
    public T next()
    {
        if (curr == null) return null;

        T data = curr.data;

        curr = getNextNode(curr);

        return data;
    }

    BinarySearchTree<T> tree;

    static Node getLeftMost(Node node)
    {
        while (node.left != null) node = node.left;

        return node;
    }

    static Node getNextNode(Node node)
    {
        if (node.rght != null)
        {
            return getLeftMost(node.rght);
        }
        else
        {
            while (node.prnt != null && node == node.prnt.rght) node = node.prnt;

            return node.prnt;
        }
    }
}

interface DoWithData<T>
{
    void doWithData(T data);
}




