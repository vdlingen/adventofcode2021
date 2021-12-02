package day02

val input = util.readInput("day02.txt")
    .split("\n")
    .map {
        val (dir, length) = it.split(" ")
        Instruction(Direction.valueOf(dir), length.toInt())
    }

enum class Direction { forward, down, up }
data class Instruction(val direction: Direction, val units: Int)

fun part1(): Int {
    var position = 0
    var depth = 0

    input.forEach {
        when(it.direction) {
            Direction.forward -> position += it.units
            Direction.down -> depth += it.units
            Direction.up -> depth -= it.units
        }
    }

    return position * depth
}
fun part2(): Int {
    var position = 0
    var depth = 0
    var aim = 0

    input.forEach {
        when(it.direction) {
            Direction.forward -> {
                position += it.units
                depth += aim * it.units
            }
            Direction.down -> aim += it.units
            Direction.up -> aim -= it.units
        }
    }

    return position * depth
}

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}
