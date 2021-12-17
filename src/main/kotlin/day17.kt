package day17

val targetX = 175..227
val targetY = -134..-79

fun checkTrajectory(x: Int, y: Int): Int? {
    var velocityX = x
    var velocityY = y

    var currentX = 0
    var currentY = 0
    var maxY = 0

    while (currentX <= targetX.last && currentY >= targetY.first) {
        currentX += velocityX
        currentY += velocityY
        maxY = maxOf(maxY, currentY)

        if (currentX in targetX && currentY in targetY) return maxY

        velocityX = maxOf(0, velocityX - 1)
        velocityY--
    }

    return null
}

val trajectories = buildMap<Pair<Int, Int>, Int> {
    for (x in targetX.last downTo 1)
        for (y in targetY.first..targetX.last)
            checkTrajectory(x, y)?.let { put(Pair(x, y), it) }
}

fun part1() = trajectories.maxOf { it.value }
fun part2() = trajectories.size

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}
