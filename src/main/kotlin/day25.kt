package day25

import kotlin.system.measureTimeMillis

val input = util.readInput("day25.txt")
    .split("\n")
    .map { it.map { it } }

fun List<Char>.moveIf(d: Char): List<Char> {
    val canMove = indices.filter { index -> this[index] == d && this[(index + 1) % size] == '.' }

    return mapIndexed { index, p ->
        when {
            (size + index - 1) % size in canMove -> d
            index in canMove -> '.'
            else -> p
        }
    }
}

fun part1(): Int {
    var step = 0
    var positions = input
    var finished = false

    while (!finished) {
        val state = positions.map { it.moveIf('>') }
        val columns = state[0].indices.map { c -> state.map { it[c] }.moveIf('v') }
        val newPositions = state.indices.map { r -> columns.map { it[r] } }

        if (newPositions == positions) finished = true
        else positions = newPositions

        step++
    }

    return step
}

fun part2() = 0

fun main() = measureTimeMillis {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}.let { println("\nExecution took $it ms") }
