import PropertyInfo.Companion.getPropertyInfoList

data class MyData(val name: String, val age: Int)
{
    var ed = "ED"
}

fun main(args: Array<String>) {

    val data = MyData(name = "Sanford", age = 19)

    val array = getPropertyInfoList(data)

    array.forEach {
        println("" + it.name + " ... " + it.type + " ... " + it.value)
    }
}