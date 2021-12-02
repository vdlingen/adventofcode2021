package util

fun readInput(filename: String)
 = object {}.javaClass.getResource("/$filename")!!.readText().trimEnd()
