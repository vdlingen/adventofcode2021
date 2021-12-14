package day14

val input = util.readInput("day14.txt")
    .split("\n")

val polymer = input[0]
val rules = input.subList(2, input.size)
    .associate {
        val (pattern, insert) = it.split(" -> ")
        Pair(pattern, insert[0])
    }

fun grow(value: String) = buildString {
    value.windowed(2).forEach { pair ->
        append(pair[0])
        rules[pair]?.let { append(it) }
    }

    append(value.last())
}

fun part1(): Int {
    var result = polymer
    repeat(10) { result = grow(result) }

    val chars = result.groupBy { it }.mapValues { it.value.size }

    val min = chars.values.minOf { it }
    val max = chars.values.maxOf { it }

    return max - min
}

val cache = mutableMapOf<Pair<String, Int>, Map<Char, Long>>()

fun Map<Char, Long>.combine(counts: Map<Char, Long>): Map<Char, Long> = buildMap {
    (this@combine.keys + counts.keys).forEach { char ->
        put(char, (this@combine[char] ?: 0L) + (counts[char] ?: 0L))
    }
}

fun insertionCount(segment: String, steps: Int): Map<Char, Long> = cache[Pair(segment, steps)] ?: run {
    if (steps == 0) return@run emptyMap()

    rules[segment]?.let { inserted ->
        mapOf(inserted to 1L)
            .combine(insertionCount("${segment[0]}$inserted", steps - 1))
            .combine(insertionCount("$inserted${segment[1]}", steps - 1))
    } ?: emptyMap()
}.also { cache[Pair(segment, steps)] = it }

fun part2(): Long {
    val startCounts = polymer.groupBy { it }.mapValues { it.value.size.toLong() }

    val counts = polymer.windowed(2).fold(startCounts) { totals, segment ->
        val segmentCounts = insertionCount(segment, 40)
        totals.combine(segmentCounts)
    }

    val min = counts.values.minOf { it }
    val max = counts.values.maxOf { it }

    return max - min
}

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}
