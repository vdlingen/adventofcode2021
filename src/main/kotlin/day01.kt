package day01

val input = util.readInput("day01.txt")
    .split("\n")
    .map { it.toInt() }

fun List<Int>.countIncreases() = filterIndexed { index, value -> index > 0 && value > this[index - 1] }.size

fun part1() = input.countIncreases()
fun part2() = input.windowed(3).map { it.sum() }.countIncreases()

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}
