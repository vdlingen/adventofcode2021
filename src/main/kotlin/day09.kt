package day09

val input = util.readInput("day09.txt")
    .split("\n")
    .map { it.map { Integer.parseInt("$it") } }

val width = input[0].size
val height = input.size

data class Point(val x: Int, val y: Int)

val Point.value: Int get() = input[y][x]

val points = buildList {
    for (x in 0 until width)
        for (y in 0 until height)
            add(Point(x, y))
}

fun Point.neighbors() = buildList {
    if (x > 0) add(Point(x - 1, y))
    if (x < width - 1) add(Point(x + 1, y))
    if (y > 0) add(Point(x, y - 1))
    if (y < height - 1) add(Point(x, y + 1))
}

val basins = points.filter { point -> point.neighbors().all { it.value > point.value } }

fun part1() = basins.sumOf { it.value + 1 }

fun part2() = basins.map { point ->
    val basin = mutableSetOf(point)

    fun expand(center: Point): Unit = center.neighbors().forEach {
        if (it.value != 9 && it.value > center.value) {
            basin += it
            expand(it)
        }
    }

    expand(point)

    basin.size
}.sortedDescending().subList(0, 3).reduce { a, b -> a * b }

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}
