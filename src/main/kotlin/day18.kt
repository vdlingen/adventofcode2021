package day18

val input = util.readInput("day18.txt")
    .split("\n")
    .map { it.parseNumber() }

sealed class Number {
    data class Value(val v: Int, val index: Int) : Number()
    data class Pair(val a: Number, val b: Number, val index: Int) : Number()
}

operator fun Number.plus(number: Number) = Number.Pair(this, number, -1).print().parseNumber().reduce()

fun Number.candidateToExplode(depth: Int = 0): Number.Pair? = when {
    depth == 4 && this is Number.Pair && a is Number.Value && b is Number.Value -> this
    this is Number.Pair -> a.candidateToExplode(depth + 1) ?: b.candidateToExplode(depth + 1)
    else -> null
}

fun Number.values(): List<Number.Value> = when (this) {
    is Number.Value -> listOf(this)
    is Number.Pair -> a.values() + b.values()
}

fun Number.candidateToSplit() = values().find { it.v >= 10 }

fun Number.reduce(): Number {
    candidateToExplode()?.let { pair ->
        val literals = values()
        val previous = literals.findLast { it.index < pair.index }
        val next = literals.find { it.index > pair.index + 5 }

        fun Number.replace(): Number = when (this) {
            pair -> Number.Value(0, pair.index)
            previous -> Number.Value(previous.v + (pair.a as Number.Value).v, previous.index)
            next -> Number.Value(next.v + (pair.b as Number.Value).v, next.index)
            is Number.Pair -> Number.Pair(a.replace(), b.replace(), index)
            else -> this
        }

        return replace().reduce()
    }

    candidateToSplit()?.let { value ->
        fun Number.replace(): Number = when (this) {
            value -> Number.Pair(Number.Value(value.v / 2, 0), Number.Value(value.v / 2 + value.v % 2, 0), 0)
            is Number.Pair -> Number.Pair(a.replace(), b.replace(), index)
            else -> this
        }

        return replace().print().parseNumber().reduce()
    }

    return this
}

fun Number.print(): String = when (this) {
    is Number.Value -> v.toString()
    is Number.Pair -> "[${a.print()},${b.print()}]"
}

fun Number.magnitude(): Int = when (this) {
    is Number.Pair -> a.magnitude() * 3 + b.magnitude() * 2
    is Number.Value -> v
}

fun String.parseNumber(offset: Int = 0): Number = when {
    this[0] == '[' -> {
        val contents = substring(1, length - 1)

        val a = if (contents[0].isDigit()) contents.substring(0, indexOf(',') - 1) else {
            var brackets = 1
            var index = 1
            while (brackets > 0) {
                when (contents[index]) {
                    '[' -> brackets++
                    ']' -> brackets--
                }
                index++
            }

            contents.substring(0, index)
        }
        val b = contents.substring(a.length + 1)

        Number.Pair(a.parseNumber(offset + 1), b.parseNumber(offset + 2 + a.length), offset)
    }

    else -> Number.Value(toInt(), offset)
}

fun part1() = input.reduce { a, b -> (a + b).reduce() }.magnitude()
fun part2() = input.flatMapIndexed { i, a -> input.subList(i + 1, input.size).flatMap { b -> listOf(a + b, b + a) } }
    .maxOf { it.magnitude() }

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}
