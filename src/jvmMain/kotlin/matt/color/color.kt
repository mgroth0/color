
package matt.color

import matt.color.common.IntColor
import matt.color.common.rgb
import matt.lang.assertions.require.requireEquals
import matt.lang.assertions.require.requireIn
import java.awt.color.ColorSpace
import java.awt.Color as AwtColor


typealias AwtColor = AwtColor

fun IntColor.toAwtColor() =
    AwtColor(
        red.toInt(),
        green.toInt(),
        blue.toInt(),
        alpha.toInt()
    )

private val CS_SRGB: ColorSpace by lazy {
    ColorSpace.getInstance(ColorSpace.CS_sRGB)
}

fun AwtColor.toMColor(): IntColor =
    when (colorSpace) {
        CS_SRGB -> {

            /*requireEquals(this.transparency, Transparency.OPAQUE)*/
            val components = getColorComponents(null)
            requireIn(CS_SRGB.numComponents, 3..4)
            requireEquals(CS_SRGB.numComponents, components.size)
            rgb(
                r = red,
                b = blue,
                g = green,
                a = alpha
            )
        }

        else    -> error("What should I do with colorspace: $colorSpace?")
    }




/*
fun AColor.copy(
    r: Float? = null,
    g: Float? = null,
    b: Float? = null,
    a: Float? = null
): AColor {
    val components by lazy {
        getRGBComponents(null)
    }
    return AColor(
        r ?: components[0], g ?: components[1], b ?: components[2], a ?: components[3]
    )
}
*/


fun colorMap(numColors: Int): Map<Int, IntColor> {
    val hueStep = (1.0 / numColors)
    return List(numColors) {


        /*val hue = it*hueStep*360

        this is only for FXColor*/
        val hue = it * hueStep


        AwtColor.getHSBColor(hue.toFloat(), 0.5.toFloat(), 1.0.toFloat()).toMColor()
    }.withIndex().associate { it.index to it.value }
}


