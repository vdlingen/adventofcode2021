package util

fun readInput(filename: String)
 = object {}.javaClass.getResource("/$filename")!!.readText().trimEnd()

infix fun Int.upOrDownTo(to: Int): IntProgression {
    val step = if (this > to) -1 else 1
    return IntProgression.fromClosedRange(this, to, step)
}
