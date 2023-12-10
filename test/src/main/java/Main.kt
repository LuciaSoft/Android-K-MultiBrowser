import PropertyInfo.Companion.getPropertyInfos

data class MyData(val name: String, val age: Int)
{
    var ed = "ED"
}

fun main(args: Array<String>) {

    val data = MyData(name = "Sanford", age = 19)

    val array = getPropertyInfos(data)

    array.forEach {
        println("" + it.name + " ... " + it.type + " ... " + it.value)
    }
}