import com.luciasoft.collections.BSTofString

fun main(args: Array<String>)
{
    println("KOTLIN...")
    val bst = BSTofString(false, true, true)
    bst.add("fred", "dave", "al", "irene", "gil")
    for (name in bst) println(name)
}