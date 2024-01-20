package bigsquarekotlin.utilities

import java.io.InputStream

// NB - not generating equals or hashcode - caveat emptor!
class SquareArray(val size : Int, private val ary : BooleanArray) {
    init {
        require(size * size == ary.size)  { "Arguments inconsistent size=${size} ary.size=${ary.size}"}
    }

    private fun indexOf( y : Int, x : Int) = (y * size + x).also { idx ->
        require(idx in 0 until ary.size) { "Out of bounds!" }
    }

    operator fun set(y: Int, x: Int, value: Boolean) {
        ary[indexOf(y,x)] = value
    }

    operator fun get(y: Int, x: Int): Boolean = ary[indexOf(y,x)]

    override fun toString(): String {
        return "SquareArray(size=$size, ary (${ary.size}) =${ary.contentToString()})"
    }
}

fun parseSquare(inputStream : InputStream) : SquareArray {
    val lut = mapOf( ' ' to false, 'X' to true)
    return inputStream
        .bufferedReader()
        .readLines()
        .map { it.trimEnd( '|')}
        .let { lines -> // Sanity checks
            val sizes = lines.map { it.length }.toSet()
            require(sizes.size == 1) { "Uneven linesizes $sizes"}
            require(sizes.first() == lines.size )
            SquareArray( sizes.first(), lines.flatMap { line ->
                 line.map { c -> lut[c] ?: error("Can't parse char $c") } }.toBooleanArray() )
        }
}


fun main() {
    val s = SquareArray::javaClass.javaClass.classLoader
    val fn = "dat1.txt"
    val inputStream = s.getResourceAsStream(fn) ?: error("Can't open file from classloader $fn")
    val myAry = parseSquare(inputStream)
    println(myAry)
    myAry[3,4] = false
    val bla = myAry[4,5]
}
