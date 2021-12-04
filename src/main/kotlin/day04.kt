package day04

import java.util.regex.Pattern

val input = util.readInput("day04.txt")
    .split("\n")

val order = input[0].split(",").map { it.toInt() }
val boards = input.subList(1, input.size)
    .filterNot { it.isEmpty() }
    .chunked(5)
    .map {
        val rows = it.map {
            it.trim().replace("  ", " ").split(Pattern.compile(" ")).map { it.toInt() }
        }

        Board(rows)
    }

data class Board(val rows: List<List<Int>>) {
    val lines = rows + rows.indices.map { column -> rows.map { it[column] } }
}

fun Board.hasBingo(drawn: List<Number>) = lines.any { it.all { it in drawn } }
fun Board.winsAfter() = (5 until order.size).first { hasBingo(order.subList(0, it)) }
fun Board.score(drawn: List<Int>) = rows.flatten().filter { !drawn.contains(it) }.sum() * drawn.last()

fun part1() = boards.minByOrNull { it.winsAfter() }!!.let { it.score(order.subList(0, it.winsAfter())) }
fun part2() = boards.maxByOrNull { it.winsAfter() }!!.let { it.score(order.subList(0, it.winsAfter())) }

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}
