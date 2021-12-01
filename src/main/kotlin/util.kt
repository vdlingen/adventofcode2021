package day01

fun readInput(filename: String)
 = object {}.javaClass.getResource("/$filename")!!.readText().trimEnd()
