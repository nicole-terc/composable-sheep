package nstv.sheep.sheep.model

private const val DefaultLegRatioHeightOuter = 0.20f
private const val DefaultLegRatioHeight = 0.15f
private const val DefaultLegRatioWidth = 0.1f
private const val FourLegAngle = 40f
private const val FourLegAngleInner = 0f

data class Leg(
    val positionAngleInCircle: Float,
    val rotationDegrees: Float = 0f,
    val legBodyRatioWidth: Float = DefaultLegRatioWidth,
    val legBodyRatioHeight: Float = DefaultLegRatioHeight,
)

fun fourLegs(innerAngle: Float = FourLegAngleInner, outerAngle: Float = FourLegAngle): List<Leg> =
    listOf(
        Leg( // outer right
            positionAngleInCircle = 50f,
            rotationDegrees = -outerAngle,
            legBodyRatioWidth = DefaultLegRatioWidth,
            legBodyRatioHeight = DefaultLegRatioHeightOuter
        ),
        Leg( // inner right
            positionAngleInCircle = 65f,
            rotationDegrees = -innerAngle,
            legBodyRatioWidth = DefaultLegRatioWidth,
            legBodyRatioHeight = DefaultLegRatioHeight
        ),
        Leg( // inner left
            positionAngleInCircle = 115f,
            rotationDegrees = innerAngle,
            legBodyRatioWidth = DefaultLegRatioWidth,
            legBodyRatioHeight = DefaultLegRatioHeight
        ),
        Leg( // outer left
            positionAngleInCircle = 130f,
            rotationDegrees = outerAngle,
            legBodyRatioWidth = DefaultLegRatioWidth,
            legBodyRatioHeight = DefaultLegRatioHeightOuter
        ),
    )

fun twoLegsStraight(): List<Leg> =
    listOf(65f, 115f).map { angle ->
        Leg(positionAngleInCircle = angle)
    }
