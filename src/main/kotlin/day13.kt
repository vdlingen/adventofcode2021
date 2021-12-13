package day13

val input = util.readInput("day13.txt")
    .split("\n\n")

val dots = input[0].split("\n").map {
    val (x, y) = it.split(",").map { it.toInt() }
    Dot(x, y)
}

val instructions = input[1].split("\n").map {
    val (axis, line) = it.substring(11).split("=")
    Instruction(Axis.valueOf(axis), line.toInt())
}

enum class Axis { x, y }
data class Dot(val x: Int, val y: Int)
data class Instruction(val axis: Axis, val line: Int)

fun Int.fold(line: Int) = if (this > line) line - (this - line) else this

fun List<Dot>.fold(instruction: Instruction) = mapNotNull { dot ->
    when (instruction.axis) {
        Axis.x -> Dot(dot.x.fold(instruction.line),dot.y).takeIf { dot.x != instruction.line }
        Axis.y -> Dot(dot.x, dot.y.fold(instruction.line)).takeIf { dot.y != instruction.line }
    }
}

fun List<Dot>.print() {
    val minX = minOf { it.x }
    val maxX = maxOf { it.x }
    val minY = minOf { it.y }
    val maxY = maxOf { it.y }

    println("x = $minX -> $maxX, y = $minY -> $maxY")
    for (y in (minY .. maxY)) {
        for (x in minX..maxX) print(if (contains(Dot(x, y))) "#" else ".")
        println()
    }
}

fun part1() = dots.fold(instructions[0]).toSet().size
fun part2() {
    val result = instructions.fold(dots) { dots, instruction -> dots.fold(instruction) }
    result.print()
}

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}
