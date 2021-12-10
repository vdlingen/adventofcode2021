package day10

val input = util.readInput("day10.txt")
    .split("\n")

val brackets = mapOf(
    '[' to ']',
    '(' to ')',
    '{' to '}',
    '<' to '>',
)

fun part1() = input.map { line ->
    val expected = mutableListOf<Char>()

    line.forEach { char ->
        when (char) {
            in brackets.keys -> expected += brackets[char]!!
            in brackets.values -> if (char != expected.removeLast()) return@map when (char) {
                ')' -> 3
                ']' -> 57
                '}' -> 1197
                '>' -> 25137
                else -> 0
            }
        }
    }
    0
}.sum()

fun part2() = input.mapNotNull { line ->
    val expected = mutableListOf<Char>()

    line.forEach { char ->
        when (char) {
            in brackets.keys -> expected += brackets[char]!!
            in brackets.values -> if (char != expected.removeLast()) return@mapNotNull null
        }
    }

    expected.reversed().fold(0L) { score, char ->
        when (char) {
            ')' -> 5 * score + 1
            ']' -> 5 * score + 2
            '}' -> 5 * score + 3
            '>' -> 5 * score + 4
            else -> score
        }
    }
}.sorted().let { it[it.size / 2] }

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}
