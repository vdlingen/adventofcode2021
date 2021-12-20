package day19

import kotlin.math.abs
import kotlin.system.measureTimeMillis

val input = util.readInput("day19.txt")
    .split("\n\n")
    .map {
        val lines = it.split("\n")
        lines.subList(1, lines.size).map {
            val (x, y, z) = it.split(",").map { it.toInt() }
            Pos(x, y, z)
        }
    }

enum class Axis { X, Y, Z }

fun Pos.rotate(axis: Axis) = when (axis) {
    Axis.X -> Pos(x, z, -y)
    Axis.Y -> Pos(-z, y, x)
    Axis.Z -> Pos(y, -x, z)
}

val orientations = listOf<(Pos) -> Pos>(
    { it },
    { it.rotate(Axis.Z) },
    { it.rotate(Axis.Z).rotate(Axis.Z) },
    { it.rotate(Axis.Z).rotate(Axis.Z).rotate(Axis.Z) },

    { it.rotate(Axis.Y) },
    { it.rotate(Axis.Y).rotate(Axis.Z) },
    { it.rotate(Axis.Y).rotate(Axis.Z).rotate(Axis.Z) },
    { it.rotate(Axis.Y).rotate(Axis.Z).rotate(Axis.Z).rotate(Axis.Z) },

    { it.rotate(Axis.Y).rotate(Axis.Y) },
    { it.rotate(Axis.Y).rotate(Axis.Y).rotate(Axis.Z) },
    { it.rotate(Axis.Y).rotate(Axis.Y).rotate(Axis.Z).rotate(Axis.Z) },
    { it.rotate(Axis.Y).rotate(Axis.Y).rotate(Axis.Z).rotate(Axis.Z).rotate(Axis.Z) },

    { it.rotate(Axis.Y).rotate(Axis.Y).rotate(Axis.Y) },
    { it.rotate(Axis.Y).rotate(Axis.Y).rotate(Axis.Y).rotate(Axis.Z) },
    { it.rotate(Axis.Y).rotate(Axis.Y).rotate(Axis.Y).rotate(Axis.Z).rotate(Axis.Z) },
    { it.rotate(Axis.Y).rotate(Axis.Y).rotate(Axis.Y).rotate(Axis.Z).rotate(Axis.Z).rotate(Axis.Z) },

    { it.rotate(Axis.X) },
    { it.rotate(Axis.X).rotate(Axis.Z) },
    { it.rotate(Axis.X).rotate(Axis.Z).rotate(Axis.Z) },
    { it.rotate(Axis.X).rotate(Axis.Z).rotate(Axis.Z).rotate(Axis.Z) },

    { it.rotate(Axis.X).rotate(Axis.X).rotate(Axis.X) },
    { it.rotate(Axis.X).rotate(Axis.X).rotate(Axis.X).rotate(Axis.Z) },
    { it.rotate(Axis.X).rotate(Axis.X).rotate(Axis.X).rotate(Axis.Z).rotate(Axis.Z) },
    { it.rotate(Axis.X).rotate(Axis.X).rotate(Axis.X).rotate(Axis.Z).rotate(Axis.Z).rotate(Axis.Z) },
)

data class Pos(val x: Int, val y: Int, val z: Int) {
    operator fun plus(pos: Pos) = Pos(x + pos.x, y + pos.y, z + pos.z)
    operator fun minus(pos: Pos) = Pos(x - pos.x, y - pos.y, z - pos.z)
}

fun Pos.distance(pos: Pos): Int = abs(x - pos.x) + abs(y - pos.y) + abs(z - pos.z)

data class ScannerPosition(val offset: Pos, val rotation: (Pos) -> Pos) {
    fun convert(scans: List<Pos>) = scans.map { rotation(it) + offset }
}

fun List<Pos>.findOrientation(knownScans: List<Pos>): ScannerPosition? {
    orientations.forEach { rotation ->
        val scans = map { rotation(it) }

        for (i in scans.indices)
            for (j in knownScans.indices) {
                val offset = knownScans[j] - scans[i]

                if (scans.count { (it + offset) in knownScans } >= 12) {
                    return ScannerPosition(offset, rotation)
                }
            }
    }

    return null
}

fun solve(): Pair<Set<Pos>, List<ScannerPosition>> {
    val scannerPositions = mutableMapOf<Int, ScannerPosition>()
    val beacons = mutableSetOf<Pos>()

    scannerPositions[0] = ScannerPosition(Pos(0, 0, 0), orientations[0])
    beacons += input[0]

    var toCheck = setOf(0)

    while (toCheck.isNotEmpty()) {
        val found = mutableSetOf<Int>()

        input.indices.filterNot { it in scannerPositions.keys }.forEach { index ->
            val scans = input[index]

            toCheck.forEach { i ->
                val position = scannerPositions[i]!!
                val knownScans = position.convert(input[i])

                scans.findOrientation(knownScans)?.let { scannerPosition ->
                    scannerPositions[index] = scannerPosition
                    beacons += scannerPosition.convert(scans)

                    found += index
                }
            }
        }

        toCheck = found
    }

    return Pair(beacons, scannerPositions.values.toList())
}

val solution by lazy { solve() }
fun part1() = solution.first.size
fun part2() = solution.second.let { scannerPositions ->
    val pairs = buildList {
        for (i in scannerPositions.indices)
            for (j in i + 1 until scannerPositions.size)
                add(Pair(scannerPositions[i], scannerPositions[j]))
    }

    pairs.maxOf { it.first.offset.distance(it.second.offset) }
}

fun main() = measureTimeMillis {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}.let { println("\nExecution took $it ms") }
