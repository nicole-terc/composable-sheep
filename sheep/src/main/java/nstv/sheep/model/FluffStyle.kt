package nstv.sheep.model

import kotlin.random.Random

const val MinAnglePercentage = 10.0
const val MaxAnglePercentage = 20.0

sealed class FluffStyle {
    val fluffChunksPercentages: List<Double> by lazy { buildFluffPercentages() }

    data class Uniform(val numberOfFluffChunks: Int) : FluffStyle()

    data class UniformIntervals(
        val percentageIntervals: List<Double> = listOf(
            MinAnglePercentage,
            MaxAnglePercentage
        )
    ) : FluffStyle()

    data class Random(
        val minPercentage: Double = MinAnglePercentage,
        val maxPercentage: Double = MaxAnglePercentage
    ) : FluffStyle()
}

internal fun FluffStyle.buildFluffPercentages(
    totalPercentage: Double = 100.0
): List<Double> {
    val angleChunks: MutableList<Double> = mutableListOf()

    var currentSum = 0.0
    while (currentSum < totalPercentage) {
        var angleChunk = getNextAngleChunkPercentage(
            fluffStyle = this,
            totalPercentage = totalPercentage,
            index = angleChunks.size
        )
        if (currentSum + angleChunk > totalPercentage) {
            angleChunk = totalPercentage - currentSum
        }
        angleChunks.add(angleChunk)
        currentSum += angleChunk
    }
    return angleChunks
}

private fun getNextAngleChunkPercentage(
    fluffStyle: FluffStyle,
    totalPercentage: Double,
    index: Int
): Double =
    when (fluffStyle) {
        is FluffStyle.Uniform -> totalPercentage.div(fluffStyle.numberOfFluffChunks)
        is FluffStyle.UniformIntervals -> fluffStyle.percentageIntervals[index.mod(fluffStyle.percentageIntervals.size)]
        is FluffStyle.Random -> Random.nextDouble(
            fluffStyle.minPercentage,
            fluffStyle.maxPercentage
        )
    }
