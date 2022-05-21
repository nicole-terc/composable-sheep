package nstv.sheep.sheep

const val MinAnglePercentage = 10.0
const val MaxAnglePercentage = 20.0

sealed interface FluffStyle {
    class Uniform(val numberOfFluffChunks: Int) : FluffStyle

    class UniformIntervals(
        val percentageIntervals: List<Double> = listOf(
            MinAnglePercentage,
            MaxAnglePercentage
        )
    ) : FluffStyle

    class Random(
        val minPercentage: Double = MinAnglePercentage,
        val maxPercentage: Double = MaxAnglePercentage
    ) : FluffStyle
}
