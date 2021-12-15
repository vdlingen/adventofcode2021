package day15

import java.util.PriorityQueue

val input = util.readInput("day15.txt")
    .split("\n")
    .map { it.map { it } }

fun part1(): Int? {
    val mapWidth = input[0].size
    val mapHeight = input.size

    data class Position(val x: Int, val y: Int) {
        val risk = input[y][x].digitToInt()
    }

    fun Position.neighbors() = buildSet {
        if (x > 0) add(Position(x - 1, y))
        if (x < mapWidth - 1) add(Position(x + 1, y))
        if (y > 0) add(Position(x, y - 1))
        if (y < mapHeight - 1) add(Position(x, y + 1))
    }

    val start = Position(0, 0)
    val target = Position(mapWidth - 1, mapHeight - 1)

    val visited = mutableSetOf<Position>()
    val riskMap = mutableMapOf(start to 0)

    var current: Position? = start

    while (visited.contains(target) == false && current != null) {
        val risk = riskMap[current]!!

        current.neighbors()
            .filterNot { it in visited }
            .forEach { riskMap[it] = minOf(riskMap[it] ?: Integer.MAX_VALUE, it.risk + risk) }

        visited += current

        current = riskMap
            .filterNot { it.key in visited }
            .minByOrNull { it.value }
            ?.key
    }

    return riskMap[target]
}

fun part2(): Int? {
    val inputWidth = input[0].size
    val inputHeight = input.size

    val mapWidth = input[0].size * 5
    val mapHeight = input.size * 5

    data class Position(val x: Int, val y: Int) {
        val risk = input[y % inputHeight][x % inputWidth].digitToInt().let {
            var risk = it

            repeat((x / inputWidth) + (y / inputHeight)) {
                risk = if (risk == 9) 1 else risk + 1
            }

            risk
        }
    }

    fun Position.neighbors() = buildSet {
        if (x > 0) add(Position(x - 1, y))
        if (x < mapWidth - 1) add(Position(x + 1, y))
        if (y > 0) add(Position(x, y - 1))
        if (y < mapHeight - 1) add(Position(x, y + 1))
    }

    val start = Position(0, 0)
    val target = Position(mapWidth - 1, mapHeight - 1)

    val visited = mutableSetOf<Position>()
    val riskMap = mutableMapOf(start to 0)

    var current: Position? = start

    val queue = PriorityQueue<Position>(compareBy { riskMap[it] })

    while (visited.contains(target) == false && current != null) {
        val risk = riskMap[current]!!

        current.neighbors()
            .filterNot { it in visited }
            .forEach {
                val currentRisk = riskMap[it]
                val newRisk = it.risk + risk

                if (currentRisk == null || newRisk < currentRisk) {
                    riskMap[it] = newRisk
                    queue.remove(it)
                    queue.add(it)
                }
            }

        visited += current

        current = queue.poll()
    }

    return riskMap[target]
}

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}
