package day07

import kotlin.math.abs

val input = util.readInput("day07.txt")
    .split(",")
    .flatMap { it.split(",").map { it.toInt() } }
    .sorted()

fun minimalFuelConsumption(offset: Int = 0, cost: (Int) -> Int) = input.minByOrNull { position ->
    input.sumOf { cost(abs(it - position))}
}?.let { position ->
    (position - offset .. position + offset).minOf { offset -> input.sumOf { cost(abs(it - offset))}}
}

fun part1() = minimalFuelConsumption(0) { it }
fun part2() = minimalFuelConsumption(5) { (1..it).sum() }

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}
