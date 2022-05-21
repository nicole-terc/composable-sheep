package nstv.sheep.sheep

import kotlin.random.Random

data class Sheep(
    val fluffStyle: FluffStyle,
    val fluffChunksPercentages: List<Double> = getFluffPercentages(fluffStyle = fluffStyle)
)

private fun getFluffPercentages(
    fluffStyle: FluffStyle,
    totalPercentage: Double = 100.0
): List<Double> {
    val angleChunks: MutableList<Double> = mutableListOf()

    var currentSum = 0.0
    while (currentSum < totalPercentage) {
        var angleChunk = getNextAngleChunkPercentage(
            fluffStyle = fluffStyle,
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
