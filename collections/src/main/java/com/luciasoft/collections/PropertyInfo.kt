package com.luciasoft.collections

import com.luciasoft.collections.BinarySearchTree
import com.luciasoft.collections.StringComparator
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberProperties

class PropertyInfoTree : BinarySearchTree<PropertyInfo>(false, PropertyInfoComparator())
class PropertyInfoComparator: Comparator<PropertyInfo>
{
    private val cmp = StringComparator(false, true)
    override fun compare(pi1: PropertyInfo?, pi2: PropertyInfo?): Int
    {
        return cmp.compare(pi1!!.name, pi2!!.name)
    }
}

enum class Mutability
{
    Mutable, Immutable, Any
}

class PropertyInfo(val instance: Any, val prop: KProperty1<Any, *>, val name: String, val type: KType, val value: Any?)
{
    companion object
    {
        fun getPropertyInfoTree(instance: Any, mutability: Mutability = Mutability.Any, safe: Boolean = true): PropertyInfoTree
        {
            val props = getPropertyInfoList(instance, mutability, safe)
            val tree = PropertyInfoTree()
            tree.add(props, true)
            return tree
        }

        fun getPropertyInfoList(instance: Any, mutability: Mutability = Mutability.Any, safe: Boolean = true): ArrayList<PropertyInfo>
        {
            val clazz = instance.javaClass.kotlin
            val props = clazz.declaredMemberProperties
            val list = ArrayList<PropertyInfo>()
            for (prop in props)
            {
                when (mutability)
                {
                    Mutability.Mutable -> if (prop !is KMutableProperty1<Any, *>) continue
                    Mutability.Immutable -> if (prop is KMutableProperty1<Any, *>) continue
                    else -> { }
                }
                try {
                    list.add(PropertyInfo(instance, prop, prop.name, prop.returnType, prop.get(instance))) }
                catch (ex: Exception) {
                    if (!safe) throw ex
                }
            }
            return list
        }
    }
}
