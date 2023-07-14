package matt.color

import kotlinx.serialization.Serializable
import matt.lang.require.requireEquals
import matt.lang.require.requireIn
import matt.lang.require.requireStartsWith
import matt.lang.safeconvert.requireIsUByte
import matt.prim.str.mybuild.string
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


fun hexToColor(hex: String): IntColor {
    requireStartsWith(hex, "#")
    requireEquals(hex.length, 7)
    return rgb(
        hex[1].digitToInt(16) * 16 + hex[2].digitToInt(16),
        hex[3].digitToInt(16) * 16 + hex[4].digitToInt(16),
        hex[5].digitToInt(16) * 16 + hex[5].digitToInt(16)
    )
    /*return Color.decode(hex)*/
}

fun rgb(
    r: Int,
    g: Int,
    b: Int,
    a: Int? = null
) = if (a != null)
    IntColor(r.requireIsUByte(), g.requireIsUByte(), b.requireIsUByte(), a.requireIsUByte())
else IntColor(r.requireIsUByte(), g.requireIsUByte(), b.requireIsUByte())


/*could be encoded as a single Integer if I wanted to increase performance*/
@Serializable
data class IntColor(
    override val red: UByte,
    override val blue: UByte,
    override val green: UByte,
    override val alpha: UByte = MAX
) : ColorBase {
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