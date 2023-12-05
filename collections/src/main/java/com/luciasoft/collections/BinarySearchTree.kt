package com.luciasoft.collections

class BSTofString(allowDupes: Boolean, ignoreCase: Boolean, special: Boolean) : BinarySearchTree<String>(allowDupes, StringComparator(ignoreCase, special))

open class BinarySearchTree<T>(private val allowDupes: Boolean, private val comparator: Comparator<T>) : Iterable<T>
{
    private class Node<T>(val data: T, val prnt: Node<T>?)
    {
        var left: Node<T>? = null
        var rght: Node<T>? = null
    }

    private var root: Node<T>? = null

    var count: Int = 0
    private set

    fun getSameAs(data: T): T?
    {
        return getSameAs(data, root)
    }

    private fun getSameAs(data: T, node: Node<T>?): T?
    {
        if (root == null) return null

        checkNotNull(node) {"node cannot be null."}

        val compare = comparator.compare(data, node.data)

        if (compare < 0) return getSameAs(data, node.left)
        if (compare > 0) return getSameAs(data, node.rght)

        return node.data
    }
    
    fun add(data: T)
    {
        add(data, root)
    }

    fun add(vararg data: T)
    {
        for (datum in data) add(datum)
    }

    fun add(randomize: Boolean, vararg data: T)
    {
        if (!randomize) for (datum in data) add(datum)
        else add(data.toList(), randomize)
    }

    fun add(data: Collection<T>, randomize: Boolean)
    {
        if (!randomize)
        {
            for (datum in data) add(datum)
        }
        else
        {
            val list = randomize(data);
            for (datum in list) add(datum)
        }
    }

    private fun add(data: T, node: Node<T>?)
    {
        if (root == null)
        {
            root = Node(data, null)
            count++
            return
        }

        checkNotNull(node) {"node cannot be null."}

        var compare = comparator.compare(data, node.data)
        if (allowDupes && compare == 0) compare = 1

        if (compare < 0)
        {
            if (node.left == null)
            {
                node.left = Node(data, node)
                count++
                return
            }

            add(data, node.left)
            return
        }

        if (compare > 0)
        {
            if (node.rght == null)
            {
                node.rght = Node(data, node)
                count++
                return
            }

            add(data, node.rght)
            return
        }
    }

    override fun iterator(): Iterator<T> = MyIterator(this)

    private class MyIterator<T>(private val tree: BinarySearchTree<T>) : Iterator<T>
    {
        private var current = getFirst()

        override fun hasNext(): Boolean = current != null

        override fun next(): T
        {
            val data = current!!.data
            current = getNext(current!!)
            return data
        }

        private fun getFirst(): Node<T>?
        {
            if (tree.root == null) return null;
            return getLeftMost(tree.root!!)
        }

        private fun getNext(node: Node<T>): Node<T>?
        {
            var n = node
            if (n.rght != null) return getLeftMost(n.rght!!);
            while (n.prnt != null && n == n.prnt!!.rght) n = n.prnt!!
            return n.prnt
        }
        
        private fun getLeftMost(node: Node<T>): Node<T>
        {
            var n = node
            while (n.left != null) n = n.left as Node<T>
            return n
        }
    }
}