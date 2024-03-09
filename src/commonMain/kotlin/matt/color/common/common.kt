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

fun IntColor.calculateContrastingColor(contrastor: ContrastAlgorithm): IntColor = contrastor.contrastingColorOf(this)


interface ContrastAlgorithm {
    fun contrastingColorOf(input: IntColor): IntColor
}


object LetsTrySomethingMoreAdvanced : ContrastAlgorithm {
    override fun contrastingColorOf(input: IntColor): IntColor =
        input.run {
            require(hasDefaultAlpha) {
                "not ready"
            }
            val yellow = (red + green) / 2u
            return rgb(
                r =
                    when {
                        (red >= 128u) ->
                            when {
                                red <= 192u ->
                                    when {
                                        green >= 128u -> 255
                                        else          -> 0
                                    }

                                else        -> 0
                            }

                        else          -> 255
                    },
                g =
                    when {
                        (green >= 128u) ->
                            when {
                                green <= 192u ->
                                    when {
                                        red >= 128u -> 255
                                        else        -> 0
                                    }

                                else          -> 0
                            }

                        else            -> 255
                    },
                b =
                    when {
                        (blue >= 128u) ->
                            when {
                                blue <= 192u ->
                                    when {
                                        yellow >= 128u -> 255
                                        else           -> 0
                                    }

                                else         -> 0
                            }

                        else           -> 255
                    }
            )
        }
}

object SimpleButOftenStupid : ContrastAlgorithm {
    override fun contrastingColorOf(input: IntColor) =
        input.run {


            require(hasDefaultAlpha) {
                "not ready"
            }
            rgb(
                r =
                    when {
                        (red >= 128u) -> 0
                        else          -> 255
                    },
                g =
                    when {
                        (green >= 128u) -> 0
                        else            -> 255
                    },
                b =
                    when {
                        (blue >= 128u) -> 0
                        else           -> 255
                    }
            )
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
) = run {
    r.verifyToUByte()
    g.verifyToUByte()
    b.verifyToUByte()
    a.verifyToUByte()
    IntColor(

        ((r shl 24) or (g shl 16) or (b shl 8) or a).toUInt()

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
