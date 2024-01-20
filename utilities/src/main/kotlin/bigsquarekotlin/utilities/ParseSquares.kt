package bigsquarekotlin.utilities

import java.io.InputStream

object Loader

// NB - not generating equals or hashcode - caveat emptor!
data class SquareArray( val size : Int, val ary : BooleanArray) {
    private fun indexOf( y : Int, x : Int) = (y * size + x).also { idx ->
        require(idx in 0 until ary.size) { "Out of bounds!" }
    }

    operator fun set(y: Int, x: Int, value: Boolean) {
        ary[indexOf(y,x)] = value
    }

    operator fun get(y: Int, x: Int): Boolean = ary[indexOf(y,x)]
}

fun parseSquare(instream : InputStream) : SquareArray {
    val lut = mapOf( ' ' to false, 'X' to true)
    return instream
        .bufferedReader()
        .readLines()
        .map { it.trimEnd( '|')}
        .let { lines -> // Sanity checks
            val sizes = lines.map { it.length }.toSet()
            require(sizes.size == 1) { "Uneven linesizes $sizes"}
            require(sizes.first() == lines.size )
            SquareArray( sizes.first(), lines.flatMap { line ->
                 line.map { c -> lut[c] ?: error("Can't parse char $c") } }.toBooleanArray() ).also {
                require( it.size * it.size == it.ary.size) {"Not a square array?!"} }
        }
}


fun main() {
    val s = Loader::javaClass.javaClass.classLoader
    val instream = s.getResourceAsStream("dat1.txt") ?: error("Can't open file")
    val myAry = parseSquare(instream)
    myAry[3,4] = false
    val bla = myAry[4,5]
}