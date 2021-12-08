package day08

val input = util.readInput("day08.txt")
    .split("\n")
    .map {
        it.split(" | ").map { it.split(" ").map { it.toSet() } }
    }

fun part1() = input.sumOf {
    it[1].filter {
        when (it.size) {
            2, 3, 4, 7 -> true
            else -> false
        }
    }.size
}

fun part2() = input.map { entry ->
    val digits = mutableMapOf<Int, Set<Char>>()

    while (entry[1].any { !digits.values.contains(it) }) {
        entry.flatten()
            .filterNot { digits.values.contains(it) }
            .forEach { number ->
            when (number.size) {
                2 -> digits[1] = number
                3 -> digits[7] = number
                4 -> digits[4] = number
                7 -> digits[8] = number
                5 -> {
                    when {
                        digits[1]?.let { number.containsAll(it) } == true -> digits[3] = number
                        digits[7]?.let { number.containsAll(it) } == true -> digits[3] = number
                        digits[9]?.containsAll(number) == true -> digits[5] = number
                        digits[3] != null && digits[5] != null -> digits[2] = number
                    }
                }
                6 -> {
                    when {
                        digits[1]?.let { number.containsAll(it) } == false -> digits[6] = number
                        digits[7]?.let { number.containsAll(it) } == false -> digits[6] = number
                        digits[4]?.let { number.containsAll(it) } == true -> digits[9] = number
                        digits[9] != null && digits[6] != null -> digits[0] = number
                    }
                }
            }
        }
    }

    entry[1].map { number -> digits.entries.find { it.value == number }!!.key }.joinToString("").toInt()
}.sum()

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}
