package bigsquarekotlin.utilities

import java.io.InputStream

object Loader

fun parseSquare(instream : InputStream) {
    val lines = instream.bufferedReader().readLines()

    lines.forEach { println(it) }
}


fun main() {
    val s = Loader::javaClass.javaClass.classLoader
    val instream = s.getResourceAsStream("dat1.txt") ?: error("Can't open file")
    parseSquare(instream)
}