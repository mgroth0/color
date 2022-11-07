package matt.color

import matt.color.name.ColorUtils
import matt.prim.str.upper

typealias AColor = java.awt.Color

actual typealias Color = java.awt.Color

fun AColor.hex() = kotlin.String.format("#%02x%02x%02x", this.red, green, blue)

fun awtColor(r: Int, g: Int, b: Int) = AColor(r, g, b)

fun AColor.mostContrastingForMe(): AColor {
  return awtColor(
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