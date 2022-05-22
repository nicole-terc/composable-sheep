package nstv.sheep.sheep.model

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

private const val DefaultHeadRotationAngle = -10f

data class Sheep(
    val fluffStyle: FluffStyle = FluffStyle.Random(),
    val headAngle: Float = DefaultHeadRotationAngle,
    val legs: List<Leg> = FourLegs(),
    val fluffColor: Color = Color.Blue,
    val headColor: Color = Color.DarkGray,
    val legColor: Color = Color.DarkGray,
    val glassesColor: Color = Color.Black,
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
