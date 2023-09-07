package matt.color

import kotlinx.serialization.Serializable
import matt.lang.require.requireIn
import matt.lang.require.requireZero
import matt.lang.safeconvert.requireIsUByte
import matt.lang.safeconvert.requireIsUInt
import matt.prim.str.mybuild.string
import kotlin.jvm.JvmInline
import kotlin.random.Random
import kotlin.random.nextInt

fun IntColor.mostContrastingForMe(): IntColor {
    require(hasDefaultAlpha) {
        "not ready"
    }
    return rgb(
        r = if (red >= 128u) 0 else 255, g = if (green >= 128u) 0 else 255, b = if (blue >= 128u) 0 else 255
    )
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
    override val css: String get() = if (hasDefaultAlpha) "rgb($red,$green,$blue)" else "rgba($red,$green,$blue,$alpha)"

    val hasDefaultAlpha get() = alpha == max
}

//
//fun hexToColor(hex: Long): IntColor {
//    require(hex <= 0xFF_FF_FF_FF)
//    val component1 = hex shr 24
//    val component2 = hex shr 16 and 0xFF
//    val component3 = hex shr 8 and 0xFF
//    val component4 = hex and 0xFF
//    return rgb(
//        component1,
//        component2,
//        component3,
//        component4
//    )
//    /*return Color.decode(hex)*/
//}


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
) = run {
    r.requireIsUByte()
    g.requireIsUByte()
    b.requireIsUByte()
    a.requireIsUByte()
    IntColor(

        ((r shl 24) or (g shl 16) or (b shl 8) or a).toUInt()

//                red = r . requireIsUByte (),
//        green = g.requireIsUByte(),
//        blue = b.requireIsUByte(),
//        alpha = a.requireIsUByte()
    )
}//  else IntColor(red = r.requireIsUByte(), green = g.requireIsUByte(), blue = b.requireIsUByte())


@JvmInline
@Serializable
value class IntColor(
    val data: UInt
) : ColorBase {

    constructor(data: Long) : this(data.requireIsUInt())


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
    fun hex() = string {
        if (alpha != MAX) TODO("not ready for alpha in hex")
        append("#")
        append(red.asHex())
        append(green.asHex())
        append(blue.asHex())
    }

    override fun toString(): String {
        return "IntColor[data=$data](r=${red},g=${green},b=${blue},a=${alpha})"
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

}

fun Random.nextColor() = nextRandomColor(this)
fun nextRandomColor(rand: Random = Random) =
    rgb(rand.nextInt(0..255), rand.nextInt(0..255), rand.nextInt(0..255))