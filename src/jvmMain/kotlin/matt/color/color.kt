package matt.color

import matt.color.name.ColorUtils
import matt.prim.str.upper

typealias AColor = java.awt.Color

actual typealias Color = java.awt.Color

fun AColor.hex() = kotlin.String.format("#%02x%02x%02x%02x", this.red, green, blue,alpha)

fun rgbToAwtColor(r: Int, g: Int, b: Int) = AColor(r, g, b)

fun hexToAwtColor(hex: String): AColor {
  return Color.decode(hex)
}

fun AColor.mostContrastingForMe(): AColor {
  return rgbToAwtColor(
	r = if (red >= 128) 0 else 255,
	g = if (green >= 128) 0 else 255,
	b = if (blue >= 128) 0 else 255
  )
}

fun getAwtColor(name: String) = AColor::class.java.getField(name.upper()).get(null) as AColor


fun AColor.findName() = ColorUtils.getColorNameFromColor(this)

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
	r ?: components[0],
	g ?: components[1],
	b ?: components[2],
	a ?: components[3]
  )
}


fun colorMap(numColors: Int): Map<Int, AColor> {
  val hueStep = (1.0/numColors)
  return List(numColors) {


	/*val hue = it*hueStep*360*/ /*this is only for FXColor*/
	val hue = it*hueStep


	AColor.getHSBColor(hue.toFloat(), 0.5.toFloat(), 1.0.toFloat())
  }.withIndex().associate { it.index to it.value }
}