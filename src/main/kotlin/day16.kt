package day16

val input = util.readInput("day16.txt")
    .map { Integer.parseInt("$it", 16).toString(2).padStart(4, '0') }
    .joinToString("")

data class Packet(
    val version: Int,
    val type: Int,
    val length: Int,
    val value: Long = 0,
    val packets: List<Packet> = emptyList()
)

fun parsePacket(offset: Int = 0): Packet {
    val version = input.substring(offset, offset + 3).toInt(2)

    return when (val type = input.substring(offset + 3, offset + 6).toInt(2)) {
        4 -> {
            var valueOffset = 0

            val value = buildString {
                var finished = false

                while (!finished) {
                    val part = input.substring(offset + 6 + valueOffset, offset + 6 + valueOffset + 5)
                    append(part.substring(1))

                    finished = part[0] == '0'
                    valueOffset += 5
                }
            }.toLong(2)

            Packet(version, type, value = value, length = 6 + valueOffset)
        }

        else -> {
            val lengthType = input[offset + 6]
            val lengthSize = if (lengthType == '0') 15 else 11
            val expected = input.substring(offset + 7, offset + 7 + lengthSize).toInt(2)
            var parsed = 0

            val packages: List<Packet> = buildList {
                when (lengthType) {
                    '0' -> {
                        while (parsed < expected) {
                            add(parsePacket(offset + 7 + lengthSize + parsed).also { parsed += it.length })
                        }
                    }

                    '1' -> {
                        while (size < expected) {
                            add(parsePacket(offset + 7 + lengthSize + parsed).also { parsed += it.length })
                        }
                    }
                }
            }

            Packet(version, type, packets = packages, length = 7 + lengthSize + packages.sumOf { it.length })
        }
    }
}

val packet = parsePacket()

fun Packet.versionSum(): Int = version + packets.map { it.versionSum() }.sum()
fun Packet.execute(): Long = when (type) {
    0 -> packets.sumOf { it.execute() }
    1 -> packets.fold(1) { acc, packet -> acc * packet.execute() }
    2 -> packets.minOf { it.execute() }
    3 -> packets.maxOf { it.execute() }
    4 -> value
    5 -> if (packets[0].execute() > packets[1].execute()) 1 else 0
    6 -> if (packets[0].execute() < packets[1].execute()) 1 else 0
    7 -> if (packets[0].execute() == packets[1].execute()) 1 else 0
    else -> error("Unknown type")
}

fun part1() = packet.versionSum()
fun part2() = packet.execute()

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}
