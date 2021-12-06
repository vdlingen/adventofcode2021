package day06

val input = util.readInput("day06.txt")
    .split(",")
    .map { it.toInt() }

val cache = mutableMapOf<Pair<Int, Int>, Long>()

fun Int.countAfter(days: Int): Long = cache[Pair(this, days)] ?: when {
    days == 0 -> 1
    this == 0 -> (6).countAfter(days - 1) + 8.countAfter(days - 1)
    else -> (this - 1).countAfter(days - 1)
}.also { cache[Pair(this, days)] = it }

fun part1() = input.sumOf { it.countAfter(80) }
fun part2() = input.sumOf { it.countAfter(256) }

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}
