import com.luciasoft.collections.BSTofString
import com.luciasoft.collections.randomize

fun main(args: Array<String>)
{
    println("KOTLIN...")

    val bst = BSTofString(false, true, true)
    bst.add(true, "fred", "dave", "al", "irene", "gil")
    for (name in bst) println(name)
}