package matt.color

import matt.klib.str.upper

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
