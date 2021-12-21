package day21

import kotlin.system.measureTimeMillis

class Die {
    private var c = 0
    var rolls = 0
        private set

    fun roll() = (c + 1).also {
        rolls++
        c = (c + 1) % 100
    }
}

fun Int.move(steps: Int) = ((this -1 + steps) % 10) + 1

fun part1(): Int {
    var p1 = 4
    var p2 = 5

    val die = Die()

    var s1 = 0
    var s2 = 0

    while (s1 < 1000 && s2 < 1000) {
        p1 = p1.move(die.roll() + die.roll() + die.roll())
        s1 += p1

        if (s1 >= 1000) continue

        p2 = p2.move(die.roll() + die.roll() + die.roll())
        s2 += p2
    }

    return if (s1 > s2) s2 * die.rolls else s1 * die.rolls
}

data class State(val p1: Int, val p2: Int, val s1: Int = 0, val s2: Int = 0, val player: Int = 1)
val cache = mutableMapOf<State, Pair<Long, Long>>()

val rolls = buildList {
    for (r1 in 1..3)
        for (r2 in 1..3)
            for (r3 in 1..3)
                add(r1 + r2 + r3)
}

fun play(state: State): Pair<Long, Long> = cache[state] ?: run {
    var w1 = 0L
    var w2 = 0L

    when (state.player) {
        1 -> {
            rolls.forEach {
                val p1 = state.p1.move(it)
                val s1 = state.s1 + p1

                if (s1 >= 21) w1++
                else play(state.copy(p1 = p1, s1 = s1, player = 2)).let { wins ->
                    w1 += wins.first
                    w2 += wins.second
                }
            }
        }

        2 -> {
            rolls.forEach {
                val p2 = state.p2.move(it)
                val s2 = state.s2 + p2

                if (s2 >= 21) w2++
                else play(state.copy(p2 = p2, s2 = s2, player = 1)).let { wins ->
                    w1 += wins.first
                    w2 += wins.second
                }
            }
        }
    }

    Pair(w1, w2)
}.also { cache[state] = it }

fun part2() = play(State(4, 5)).let { maxOf(it.first, it.second )}

fun main() = measureTimeMillis {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}.let { println("\nExecution took $it ms") }
