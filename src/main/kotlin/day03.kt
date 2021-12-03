package day03

val input = util.readInput("day03.txt")
    .split("\n")

fun part1(): Int {
    val gamma = input[0].indices.map { index ->
        val counts = input.map { it[index] }.groupBy { it }.mapValues { it.value.size }

        val count0 = counts['0'] ?: 0
        val count1 = counts['1'] ?: 0
        if (count1 > count0) '1' else '0'
    }.joinToString("").toInt(2)

    val epsilon = input[0].indices.map { index ->
        val counts = input.map { it[index] }.groupBy { it }.mapValues { it.value.size }

        val count0 = counts['0'] ?: 0
        val count1 = counts['1'] ?: 0
        if (count1 < count0) '1' else '0'
    }.joinToString("").toInt(2)

    return gamma * epsilon
}

fun part2(): Int {
    val oxygen = input[0].indices.fold(input) { acc, i ->
        if (acc.size == 1) return@fold acc

        val counts = acc.map { it[i] }.groupBy { it }.mapValues { it.value.size }

        val count0 = counts['0'] ?: 0
        val count1 = counts['1'] ?: 0

        if (count1 >= count0) acc.filter { it[i] == '1' } else acc.filter { it[i] == '0' }
    }.first().toInt(2)

    val co2 = input[0].indices.fold(input) { acc, i ->
        if (acc.size == 1) return@fold acc

        val counts = acc.map { it[i] }.groupBy { it }.mapValues { it.value.size }

        val count0 = counts['0'] ?: 0
        val count1 = counts['1'] ?: 0

        if (count1 < count0) acc.filter { it[i] == '1' } else acc.filter { it[i] == '0' }
    }.first().toInt(2)

    return oxygen * co2
}

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}
