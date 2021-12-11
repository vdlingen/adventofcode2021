package day11

val input = util.readInput("day11.txt")
    .split("\n")
    .flatMap { it.map { it.digitToInt() }}

const val mapWidth = 10
const val mapHeight = 10

data class Octopus(val x: Int, val y: Int) {
    val index = y * mapWidth + x
}
fun Octopus.adjacent() = buildList {
    val rangeX = (maxOf(0, x - 1) .. minOf(mapWidth-1, x + 1))
    val rangeY = (maxOf(0, y - 1) .. minOf(mapHeight-1, y + 1))

    for (newX in rangeX)
        for (newY in rangeY)
            if (x != newX || y != newY)
                add(Octopus(newX, newY))
}

val octopuses = buildList {
    for (x in 0 until mapWidth)
        for (y in 0 until mapHeight)
            add(Octopus(x, y))
}

fun List<Int>.step(): Pair<Int, List<Int>> {
    val state = map { it + 1 }.toMutableList()

    val hasFlashed = mutableSetOf<Octopus>()
    var toFlash = octopuses.filter { state[it.index] > 9 }.toSet()

    while (toFlash.isNotEmpty()) {
        val trigger = mutableSetOf<Octopus>()
        hasFlashed += toFlash

        toFlash.forEach { octopus ->
            octopus.adjacent().forEach {
                state[it.index] += 1
                if (state[it.index] > 9 && !hasFlashed.contains(it)) {
                    trigger += it
                }
            }
        }

        toFlash = trigger
    }

    hasFlashed.forEach {
        state[it.index] = 0
    }

    return Pair(hasFlashed.size, state)
}

fun part1(): Int {
    var state = input
    var total = 0

    var steps = 0

    repeat(100) {
        steps++

        val (flashes, newState) = state.step()
        total += flashes
        state = newState
    }

    return total
}
fun part2(): Int {
    var state = input
    var steps = 0

    while (!state.all { it == state[0] }) {
        val (_, newState) = state.step()
        state = newState
        steps++
    }

    return steps
}

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}
