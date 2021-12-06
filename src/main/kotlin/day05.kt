package day05

import util.upOrDownTo

val input = util.readInput("day05.txt")
    .split("\n")
    .map {
        it.split(" -> ")
            .map { it.split(",").map { it.toInt() }.let { Point(it[0], it[1]) } }
            .let { Line(it[0], it[1]) }
    }

data class Point(val x: Int, val y: Int)
data class Line(val a: Point, val b: Point)

fun Line.points(diagonal: Boolean = false) = when {
    a.x == b.x -> a.y.upOrDownTo(b.y).map { Point(a.x, it) }
    a.y == b.y -> a.x.upOrDownTo(b.x).map { Point(it, a.y) }

    diagonal -> {
        val rangeX = a.x.upOrDownTo(b.x).toList()
        val rangeY = a.y.upOrDownTo(b.y).toList()

        rangeX.indices.map { Point(rangeX[it], rangeY[it]) }
    }

    else -> emptyList()
}

fun part1() = input.flatMap { it.points() }
    .groupBy { it }
    .filter { it.value.size > 1 }
    .size

fun part2() = input.flatMap { it.points(true) }
    .groupBy { it }
    .filter { it.value.size > 1 }
    .size

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}
