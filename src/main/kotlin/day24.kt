package day24

import kotlin.system.measureTimeMillis

val instructions = util.readInput("day24.txt")
    .split("\n")
    .filterNot { it.isEmpty() }
    .map {
        val data = it.split(" ")
        Instruction(
            data[0],
            data[1],
            data.getOrNull(2)
                ?.let { if (it[0].isLetter()) Placeholder.Reference(it) else Placeholder.Value(it.toInt()) })
    }

sealed class Placeholder {
    data class Reference(val ref: String) : Placeholder()
    data class Value(val value: Int) : Placeholder()
}

data class Instruction(val cmd: String, val a: String, val b: Placeholder?)

fun checkModel(model: String): Boolean {
    val input = model.map { it.digitToInt() }.toMutableList()
    val data = mutableMapOf(
        "w" to 0,
        "x" to 0,
        "y" to 0,
        "z" to 0,
    )

    fun Placeholder.get(): Int = when (this) {
        is Placeholder.Value -> value
        is Placeholder.Reference -> data[ref]!!
    }

    instructions.forEach { instruction ->
        when (instruction.cmd) {
            "inp" -> data[instruction.a] = input.removeFirst()
            "add" -> data[instruction.a] = data[instruction.a]!! + instruction.b!!.get()
            "mul" -> data[instruction.a] = data[instruction.a]!! * instruction.b!!.get()
            "div" -> data[instruction.a] = data[instruction.a]!! / instruction.b!!.get()
            "mod" -> data[instruction.a] = data[instruction.a]!! % instruction.b!!.get()
            "eql" -> data[instruction.a] = if (data[instruction.a]!! == instruction.b!!.get()) 1 else 0
        }
    }

    return data["z"]!! == 0
}

/**
Solved manually by checking the program blocks. There is a pattern where a block is executed per digit.

- each block either multiplies z by 26 or divides it by 26
- if (x is equal to the remainder of z / 26) the rest of the block does nothing
- the blocks that multiply by z can be added on the stack
- for the blocks that divide z by 26 we can determine a rule by checking against the entry on the top of the stack

[0]: z += w0 + 1
[1]: z += w1 + 10
[2]: z += w2 + 2
[3]: w3 = w2 - 8 => stack 1
[4]: z += w4 + 6
[5]: z += w
[6]: z += w6 + 16
[7]: w7 = w6 + 5 => stack 5
[8]: w8 = w5 - 7 => stack 4
[9]: z += w9 + 7
[10]: w10 = w9 - 6 => stack 4
[11]: w11 = w4 + 6 => stack 1
[12]: w12 - 11 = w1 + 10 => stack 0
[13]: w13 = w0 - 1

Rules:
w3 = w2 - 8   => (1, 9) => (1, 9)
w7 = w6 + 5   => (9, 4) => (6, 1)
w8 = w5 - 7   => (2, 9) => (1, 8)
w10 = w9 - 6  => (3, 9) => (1, 7)
w11 = w4 + 6  => (9, 3) => (7, 1)
w12 = w1 + 1  => (8, 9) => (1, 2)
w13 = w0 - 1  => (9, 8) => (2, 1)
 */

fun part1() = "89913949293989".takeIf { checkModel(it) }
fun part2() = "12911816171712".takeIf { checkModel(it) }

fun main() = measureTimeMillis {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}.let { println("\nExecution took $it ms") }
