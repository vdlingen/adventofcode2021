package day20

import kotlin.system.measureTimeMillis

val input = util.readInput("day20.txt")
    .split("\n")

val algorithm = input[0]
val image = input.subList(2, input.size).let { data ->
    val height = data.size
    val width = data[0].length

    Image(
        lit = buildSet {
            data.forEachIndexed { row, line ->
                line.forEachIndexed { column, char ->
                    if (char == '#') add(Pair(column, row))
                }
            }
       },
        rangeX = 0 until width,
        rangeY = 0 until height,
    )
}

data class Image(
    val lit: Set<Pair<Int, Int>>,
    private val rangeX: IntRange,
    private val rangeY: IntRange,
    private val default: Char = '0'
) {
    private fun sample(x: Int, y: Int) = buildString {
        for (row in y - 1..y + 1)
            for (column in x - 1..x + 1)
                append(
                    when {
                        !rangeX.contains(column) || !rangeY.contains(row) -> default
                        lit.contains(Pair(column, row)) -> '1'
                        else -> '0'
                    }
                )
    }.toInt(2).let { algorithm[it] }

    fun enhance(): Image {
        val newRangeX = rangeX.first - 1..rangeX.last + 1
        val newRangeY = rangeY.first - 1..rangeY.last + 1

        val newDefault = List(9, { default }).joinToString("").toInt(2).let {
            if (algorithm[it] == '#') '1' else '0'
        }

        return Image(
            lit = buildSet {
                for (x in newRangeX)
                    for (y in newRangeY)
                        if (sample(x, y) == '#')
                            add(Pair(x, y))
            },
            rangeX = newRangeX,
            rangeY = newRangeY,
            default = newDefault,
        )
    }

    override fun toString() = buildString {
        appendLine("Image: x = $rangeX, y = $rangeY, default=$default")

        for (y in rangeY) {
            for (x in rangeX)
                append(if (Pair(x, y) in lit) '#' else '.')

            appendLine()
        }
    }
}

fun part1() = (1..2).fold(image) { img, _ -> img.enhance() }.lit.size
fun part2() = (1..50).fold(image) { img, _ -> img.enhance() }.lit.size

fun main() = measureTimeMillis {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}.let { println("\nExecution took $it ms") }
