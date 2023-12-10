import com.luciasoft.collections.BinarySearchTree
import com.luciasoft.collections.StringComparator
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

class PropertyInfo(val instance: Any, val prop: KProperty1<Any, *>, val name: String, val type: KType, val value: Any?)
{
    companion object
    {
        fun getPropertyInfoTree(instance: Any, safe: Boolean = true): PropertyInfoTree
        {
            val props = getPropertyInfos(instance, safe)
            val tree = PropertyInfoTree()
            tree.add(props, true)
            return tree
        }

        fun getPropertyInfos(instance: Any, safe: Boolean = true): ArrayList<PropertyInfo>
        {
            val clazz = instance.javaClass.kotlin
            val props = clazz.declaredMemberProperties
            val list = ArrayList<PropertyInfo>()
            for (prop in props)
            {

                val name = if (safe) try { prop.name } catch (ex: Exception) { continue } else prop.name
                val type = if (safe) try { prop.returnType } catch (ex: Exception) { continue } else prop.returnType
                val value = if (safe) try { prop.get(instance) } catch (ex: Exception) { continue } else prop.get(instance)
                list.add(PropertyInfo(instance, prop, name, type, value))
            }
            return list
        }
    }
}
