package day22

import kotlin.system.measureTimeMillis

val input = util.readInput("day22.txt")
    .split("\n")
    .map {
        val (action, ranges) = it.split(" ")
        ranges.split(",")
            .map {
                val (from, to) = it.substring(2).split("..").map { it.toInt() }
                (from..to)
            }.let {
                Step(action == "on", it[0], it[1], it[2])
            }
    }

data class Step(val on: Boolean, val rangeX: IntRange, val rangeY: IntRange, val rangeZ: IntRange)
data class Cube(val x: Int, val y: Int, val z: Int)

fun IntRange.clamp(min: Int = -50, max: Int = 50) = (maxOf(min, first)..minOf(max, last))

fun Step.cubes() = buildSet {
    for (x in rangeX.clamp())
        for (y in rangeY.clamp())
            for (z in rangeZ.clamp())
                add(Cube(x, y, z))
}

fun Set<Cube>.toggle(step: Step) = if (step.on) this + step.cubes() else this - step.cubes()

fun part1() = input.subList(0, 20).fold(emptySet<Cube>()) { acc, step -> acc.toggle(step) }.size

fun IntRange.contains(range: IntRange) = range.first in this && range.last in this
fun IntRange.overlaps(range: IntRange) = range.first in this || range.last in this || this.first in range || this.last in range

data class Box(val rangeX: IntRange, val rangeY: IntRange, val rangeZ: IntRange)

fun Box.contains(box: Box) = rangeX.contains(box.rangeX) && rangeY.contains(box.rangeY) && rangeZ.contains(box.rangeZ)
fun Box.overlaps(box: Box) = rangeX.overlaps(box.rangeX) && rangeY.overlaps(box.rangeY) && rangeZ.overlaps(box.rangeZ)

fun IntRange.split(other: IntRange): List<IntRange> = buildList {
    if (other.first > first) add(first until other.first)
    if (other.last < last) add(other.last + 1..last)
    add(maxOf(first, other.first)..minOf(last, other.last))
}.distinct()

operator fun Box.minus(box: Box): List<Box> = when {
    box.contains(this) -> emptyList()
    box.overlaps(this) -> {
        val rangesX = rangeX.split(box.rangeX)
        val rangesY = rangeY.split(box.rangeY)
        val rangesZ = rangeZ.split(box.rangeZ)

        buildList {
            for (rangeX in rangesX)
                for (rangeY in rangesY)
                    for (rangeZ in rangesZ) {
                        Box(rangeX, rangeY, rangeZ)
                            .takeIf { !box.contains(it) }
                            ?.let { add(it) }
                    }
        }
    }

    else -> listOf(this)
}

val Box.size: Long
    get() = (1L + rangeX.last - rangeX.first) * (1L + rangeY.last - rangeY.first) * (1L + rangeZ.last - rangeZ.first)

fun List<Box>.totalCount() = sumOf { it.size }

fun part2() = input.fold(emptyList<Box>()) { boxes, step ->
    buildList {
        val box = Box(step.rangeX, step.rangeY, step.rangeZ)

        val overlapping = boxes.filter { !box.contains(it) && it.overlaps(box) }
        addAll(boxes.filterNot { it.overlaps(box) })

        if (step.on) {
            addAll(overlapping)
            addAll(overlapping.fold(listOf(box)) { acc, overlap -> acc.flatMap { it - overlap } })
        } else addAll(overlapping.flatMap { it - box })
    }
}.totalCount()

fun main() = measureTimeMillis {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}.let { println("\nExecution took $it ms") }
