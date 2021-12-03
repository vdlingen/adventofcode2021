package day03

val input = util.readInput("day03.txt")
    .split("\n")

fun part1(): Int {
    val gamma = input[0].indices
        .map { index -> input.map { it[index] }.groupBy { it }.maxByOrNull { it.value.size }!!.key }
        .joinToString("")
        .toInt(2)

    val epsilon = input[0].indices
        .map { index -> input.map { it[index] }.groupBy { it }.minByOrNull { it.value.size }!!.key }
        .joinToString("")
        .toInt(2)

    return gamma * epsilon
}

fun part2(): Int {
    val oxygen = input[0].indices
        .fold(input) { acc, i ->
            if (acc.size == 1) return@fold acc

            acc.map { it[i] }
                .groupBy { it }
                .toSortedMap(compareByDescending { it })
                .maxByOrNull { it.value.size }!!.key
                .let { value -> acc.filter { it[i] == value } }
        }
        .first()
        .toInt(2)

    val co2 = input[0].indices
        .fold(input) { acc, i ->
            if (acc.size == 1) return@fold acc

            acc.map { it[i] }
                .groupBy { it }
                .toSortedMap()
                .minByOrNull { it.value.size }!!.key
                .let { value -> acc.filter { it[i] == value } }
        }
        .first()
        .toInt(2)

    return oxygen * co2
}

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}
