package utils

import java.util.Random

class RandomUtils private constructor() {

    private val random: Random

    init {
        random = Random()
    }

    fun nextInt(): Int {
        return random.nextInt()
    }

    fun nextInt(bound: Int): Int {
        return random.nextInt(bound)
    }

    fun nextDouble(): Double {
        return random.nextDouble()
    }

    fun nextDouble(maxRange: Double): Double {
        return nextDouble() * maxRange
    }

    companion object {

        val instance = RandomUtils()
    }
}
