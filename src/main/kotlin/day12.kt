package day12

val reachable = buildMap<String, Set<String>> {
    util.readInput("day12.txt")
        .split("\n")
        .forEach {
            val (a, b) = it.split("-")
            this[a] = (this[a] ?: emptySet()) + b
            this[b] = (this[b] ?: emptySet()) + a
        }
}

fun findPaths(
    visited: List<String>,
    allowTwice: String? = null
): List<List<String>> =
    reachable[visited.last()]?.flatMap { next ->
        when {
            next == "start" -> emptyList()
            next == "end" -> listOf(visited + next)
            next.all { it.isLowerCase() } && visited.count { it == next } > if (next == allowTwice) 1 else 0 -> emptyList()
            else -> findPaths(visited + next, allowTwice)
        }
    } ?: emptyList()

fun part1() = findPaths(listOf("start")).size
fun part2() = reachable.keys
    .filter { it.all { it.isLowerCase() } }
    .flatMap { findPaths(listOf("start"), allowTwice = it) }
    .toSet().size

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}
