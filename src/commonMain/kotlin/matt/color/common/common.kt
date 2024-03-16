package matt.color.common

import kotlinx.serialization.Serializable
import matt.lang.anno.Open
import matt.lang.assertions.require.requireIn
import matt.lang.assertions.require.requireZero
import matt.lang.safeconvert.verifyToUByte
import matt.lang.safeconvert.verifyToUInt
import matt.prim.str.mybuild.api.string
import kotlin.jvm.JvmInline
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.random.nextInt

fun parseColor(input: String): IntColor {



    val cleaned = input.trim()

    return when {
        cleaned.startsWith("#") -> {
            val cleanedHex = cleaned.removePrefix("#")
            val normalizedHexInput =
                when (val l = cleanedHex.length) {
                    3 ->
                        buildString {
                            append(cleanedHex[0])
                            append(cleanedHex[0])
                            append(cleanedHex[1])
                            append(cleanedHex[1])
                            append(cleanedHex[2])
                            append(cleanedHex[2])
                        }
                    6 -> cleanedHex
                    else -> error("unsure how to parse hex with length of $l: $cleanedHex")
                }
            val r = normalizedHexInput.substring(0, 2).toInt(16)
            val g = normalizedHexInput.substring(2, 4).toInt(16)
            val b = normalizedHexInput.substring(4, 6).toInt(16)
            rgb(
                r = r,
                g = g,
                b = b
            )
        }
        else -> error("how do I parse this color ?: $input")
    }
}


interface ColorLike {
    val css: String
}

interface ColorBase : ColorLike {
    val red: Any
    val green: Any
    val blue: Any
    val alpha: Any
    val max: Any

    @Open
    override val css: String get() = if (hasDefaultAlpha) "rgb($red,$green,$blue)" else "rgba($red,$green,$blue,$alpha)"

    @Open
    val hasDefaultAlpha get() = alpha == max

    fun invert(): ColorBase
}

fun rgb(hex: Int): IntColor {
    requireZero(hex shr 24)
    IntColor(((hex shl 8) or 0xFF).toUInt())
    val component1 = hex shr 24
    val component2 = hex shr 16 and 0xFF
    val component3 = hex shr 8 and 0xFF
    val component4 = hex and 0xFF
    return rgb(
        component1,
        component2,
        component3,
        component4
    )
    /*return Color.decode(hex)*/
}


fun rgb(
    r: Int,
    g: Int,
    b: Int,
    a: Int = UByte.MAX_VALUE.toInt()
) = rgb(
    r.verifyToUByte(),
    g.verifyToUByte(),
    b.verifyToUByte(),
    a.verifyToUByte()
)


fun rgb(
    r: UByte,
    g: UByte,
    b: UByte,
    a: UByte = UByte.MAX_VALUE
) = run {
    IntColor(
        ((r.toInt() shl 24) or (g.toInt() shl 16) or (b.toInt() shl 8) or a.toInt()).toUInt()
    )
}


@JvmInline
@Serializable
value class IntColor(
    val data: UInt
) : ColorBase {

    constructor(data: Long) : this(data.verifyToUInt())


    override val red get() = (data shr 24).toUByte()
    override val green get() = (data shr 16 and 0xFFu).toUByte()
    override val blue get() = (data shr 8 and 0xFFu).toUByte()
    override val alpha get() = (data and 0xFFu).toUByte()

    override val max get() = MAX

    private companion object {
        val MAX = UByte.MAX_VALUE
        val VALID_RANGE = UByte.MIN_VALUE..UByte.MAX_VALUE
    }

    init {
        requireIn(red.toUInt(), VALID_RANGE)
        requireIn(blue.toUInt(), VALID_RANGE)
        requireIn(green.toUInt(), VALID_RANGE)
        requireIn(alpha.toUInt(), VALID_RANGE)
    }

    private fun UByte.asHex() = toString(16).padStart(2, '0')
    fun hex() =
        string {
            if (alpha != MAX) TODO("not ready for alpha in hex")
            append("#")
            append(red.asHex())
            append(green.asHex())
            append(blue.asHex())
        }

    override fun toString(): String = "IntColor[data=$data](r=$red,g=$green,b=$blue,a=$alpha)"

    override fun invert(): IntColor {
        if (alpha != MAX) TODO()
        return rgb(r = (MAX - red).toUByte(), g = (MAX - green).toUByte(), b = (MAX - blue).toUByte())
    }
}


@Serializable
data class FloatColor(
    override val red: Float,
    override val blue: Float,
    override val green: Float,
    override val alpha: Float = 1.0f
) : ColorBase {
    override val max get() = MAX
    override fun invert(): FloatColor {
        if (alpha != MAX) TODO()
        return FloatColor(red = MAX - red, green = MAX - green, blue = MAX - blue)
    }

    private companion object {
        const val MAX = 1f
        val VALID_RANGE = 0f..1f
    }

    init {
        requireIn(red, VALID_RANGE)
        requireIn(blue, VALID_RANGE)
        requireIn(green, VALID_RANGE)
        requireIn(alpha, VALID_RANGE)
    }


    fun toIntColor(): IntColor {
        val d = UByte.MAX_VALUE.toInt()

        return rgb(
            r = (red * d).roundToInt(),
            g = (green * d).roundToInt(),
            b = (blue * d).roundToInt(),
            a = (alpha * d).roundToInt()
        )
    }
}

fun Random.nextColor() = nextRandomColor(this)
fun nextRandomColor(rand: Random = Random) =
    rgb(rand.nextInt(0..255), rand.nextInt(0..255), rand.nextInt(0..255))

