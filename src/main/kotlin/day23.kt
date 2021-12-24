package day23

import kotlin.math.abs
import kotlin.system.measureTimeMillis

val input = util.readInput("day23.txt")
    .split("\n")
    .let {
        State(
            rooms = mapOf(
                Type.A to listOf(Type.valueOf("${it[3][3]}"), Type.valueOf("${it[2][3]}")),
                Type.B to listOf(Type.valueOf("${it[3][5]}"), Type.valueOf("${it[2][5]}")),
                Type.C to listOf(Type.valueOf("${it[3][7]}"), Type.valueOf("${it[2][7]}")),
                Type.D to listOf(Type.valueOf("${it[3][9]}"), Type.valueOf("${it[2][9]}")),
            )
        )
    }

enum class Type(val energy: Int, val target: Int) {
    A(1, 2),
    B(10, 4),
    C(100, 6),
    D(1000, 8),
}

data class State(
    val hallway: Map<Int, Type> = emptyMap(),
    val rooms: Map<Type, List<Type>>,
    val roomSize: Int = 2,
) {
    val isFinished = rooms.all { (type, contents) -> contents.isFinished(type) }
    private fun List<Type>.isFinished(type: Type) = size == roomSize && all { it == type }

    private val hallwaySlots = listOf(0, 1, 3, 5, 7, 9, 10)

    fun isHallwayClear(index: Int, target: Int) = when {
        index < target -> (index + 1..target).all { hallway[it] == null }
        index > target -> (target..index - 1).all { hallway[it] == null }
        else -> true
    }

    fun allowedMoves() = buildMap<State, Int> {
        for ((index, type) in hallway.entries) {
            val targetRoom = rooms[type]!!

            if (targetRoom.all { it == type } && isHallwayClear(index, type.target)) {
                put(
                    copy(
                        hallway = hallway - index,
                        rooms = rooms.mapValues { if (it.key == type) it.value + type else it.value }),
                    (abs(index - type.target) + roomSize - targetRoom.size) * type.energy
                )
            }
        }

        for ((roomType, room) in rooms) {
            if (room.all { it == roomType }) continue

            room.lastOrNull()?.let { type ->
                val targetRoom = rooms[type]!!

                if (targetRoom.all { it == type } && isHallwayClear(roomType.target, type.target)) {
                    put(
                        copy(
                            rooms = rooms.mapValues {
                                when (it.key) {
                                    roomType -> it.value.dropLast(1)
                                    type -> targetRoom + type
                                    else -> it.value
                                }
                            }
                        ),
                        (abs(roomType.target - type.target) + (1 + roomSize - room.size) + (roomSize - targetRoom.size)) * type.energy
                    )
                }

                for (index in hallwaySlots) {
                    if (!hallway.contains(index) && isHallwayClear(roomType.target, index))
                        put(
                            copy(
                                hallway = hallway + mapOf(index to type),
                                rooms = rooms.mapValues { if (it.key == roomType) it.value.dropLast(1) else it.value }
                            ),
                            (abs(roomType.target - index) + (1 + roomSize - room.size)) * type.energy
                        )
                }
            }
        }
    }

//    fun print() {
//        println("#############")
//        print("#")
//        (0..10).forEach { print(hallway[it]?.name ?: ".") }
//        println("#")
//
//        var first = true
//        for (i in (rooms.maxOf { it.value.size } - 1 downTo 0)) {
//            print(if (first) "###" else "  #")
//
//            print(rooms[Type.A]?.getOrNull(i) ?: ".")
//            print("#")
//            print(rooms[Type.B]?.getOrNull(i) ?: ".")
//            print("#")
//            print(rooms[Type.C]?.getOrNull(i)?: ".")
//            print("#")
//            print(rooms[Type.D]?.getOrNull(i) ?: ".")
//
//            println(if (first) "###" else "#")
//
//            first = false
//        }
//
//        println("  #########")
//    }
}

val cache = mutableMapOf<State, Pair<Long, List<State>>>()
fun State.solve(): Pair<Long, List<State>> = cache.getOrPut(this) {
    if (isFinished) Pair(0, listOf(this)) else allowedMoves()
        .map { (state, cost) ->
            val (totalCost, sequence) = state.solve()
            Pair(cost + totalCost, sequence)
        }
        .minByOrNull { it.first }
        ?.let { (cost, sequence) -> Pair(cost, listOf(this) + sequence) }
        ?: Pair(Int.MAX_VALUE.toLong(), emptyList())
}

fun part1() = input.solve().first

val folded = input.copy(
    rooms = input.rooms.mapValues { (type, contents) ->
        buildList {
            add(contents.first())

            addAll(
                when (type) {
                    Type.A -> listOf(Type.D, Type.D)
                    Type.B -> listOf(Type.B, Type.C)
                    Type.C -> listOf(Type.A, Type.B)
                    Type.D -> listOf(Type.C, Type.A)
                }
            )

            add(contents.last())
        }
    },
    roomSize = 4
)

fun part2() = folded.solve().first

fun main() = measureTimeMillis {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}.let { println("\nExecution took $it ms") }
