package day09

val input = util.readInput("day09.txt")
    .split("\n")
    .map { it.map { Integer.parseInt("$it") } }

val mapWidth = input[0].size
val mapHeight = input.size

data class Point(val x: Int, val y: Int) {
    val height: Int get() = input[y][x]
}

val points = buildList {
    for (x in 0 until mapWidth)
        for (y in 0 until mapHeight)
            add(Point(x, y))
}

fun Point.neighbors() = buildList {
    if (x > 0) add(Point(x - 1, y))
    if (x < mapWidth - 1) add(Point(x + 1, y))
    if (y > 0) add(Point(x, y - 1))
    if (y < mapHeight - 1) add(Point(x, y + 1))
}

val lowPoints = points.filter { point -> point.neighbors().all { it.height > point.height } }
fun part1() = lowPoints.sumOf { it.height + 1 }

val basins = lowPoints.map { point ->
    buildSet {
        fun expand(point: Point) {
            add(point)
            point.neighbors().forEach {
                it.takeIf { it.height in (point.height + 1)..8 }?.let { expand(it) }
            }
        }
        expand(point)
    }
}
fun part2() = basins.map { it.size }.sortedDescending().subList(0, 3).reduce { a, b -> a * b }

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}
