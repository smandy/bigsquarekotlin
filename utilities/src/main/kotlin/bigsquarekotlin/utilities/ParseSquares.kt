package bigsquarekotlin.utilities

import java.io.InputStream

data class Coord( val y : Int, val x : Int)
data class MaxData( val value : Int, val items : MutableList<Coord>)
data class MaxDatum( val value : Int, val coord : Coord)

// NB - not generating equals or hashcode - caveat emptor!
class SquareBooleanArray(val size: Int, private val ary: BooleanArray) {
    init {
        require(size * size == ary.size) { "Arguments inconsistent size=${size} ary.size=${ary.size}" }
    }

    private fun indexOf(y: Int, x: Int) = (y * size + x).also { idx ->
        require(idx in ary.indices) { "Out of bounds array access! idx=$idx ary.size=${ary.size}" }
    }

    operator fun set(y: Int, x: Int, value: Boolean) {
        ary[indexOf(y, x)] = value
    }

    operator fun get(y: Int, x: Int): Boolean = ary[indexOf(y, x)]

    override fun toString(): String {
        return "SquareArray(size=$size, ary (${ary.size}) =${ary.contentToString()})"
    }
}



class SquareIntArray(val size: Int, private val ary: IntArray) {
    fun dump(toHighlight : MaxDatum?) {
        val target = if (toHighlight == null) this else {
            SquareIntArray(size, ary.clone()).also {
                for ( yoffset in (toHighlight.coord.y - toHighlight.value + 1)..toHighlight.coord.y) {
                    for ( xoffset in (toHighlight.coord.x - toHighlight.value + 1)..toHighlight.coord.x) {
                        it[yoffset, xoffset] = -1
                    }
                }
            }
        }
        target.dumpInternal()
    }

    private fun dumpInternal() {
        for (y in 0 until size) {
            print('|')
            for (x in 0 until size) {
                this[y,x].let {
                    when(it) {
                        -1 -> 'X'
                        0 -> ' '
                        else -> ('0'.code + this[y,x]).toChar()
                    }
                }.also { char ->
                    print(char)
                    print(' ')
                }
            }
            print( '|')
            println()
        }
    }


    init {
        require(size * size == ary.size) { "Arguments inconsistent size=${size} ary.size=${ary.size}" }
    }


    private fun indexOf(y: Int, x: Int) = (y * size + x).also { idx ->
        require(idx in ary.indices) { "Out of bounds array access! idx=$idx ary.size=${ary.size}" }
    }

    operator fun set(y: Int, x: Int, value: Int) {
        ary[indexOf(y, x)] = value
    }

    operator fun get(y: Int, x: Int) : Int = ary[indexOf(y, x)]

    override fun toString(): String {
        return "SquareArray(size=$size, ary (${ary.size}) =${ary.contentToString()})"
    }
}


fun parseSquare(inputStream: InputStream): SquareBooleanArray {
    val lut = mapOf(' ' to false, 'X' to true)
    return inputStream
        .bufferedReader()
        .readLines()
        .map { it.trimEnd('|') }
        .let { lines -> // Sanity checks
            val sizes = lines.map { it.length }.toSet()
            require(sizes.size == 1) { "Uneven linesizes - have these sizes$sizes" }
            require(sizes.first() == lines.size)
            SquareBooleanArray(sizes.first(), lines.flatMap { line ->
                line.map { c -> lut[c] ?: error("Can't parse char $c in line=$line") }
            }.toBooleanArray())
        }
}

val classLoader = SquareIntArray::javaClass.javaClass.classLoader

fun streamForFile(fn : String) : InputStream{
    return classLoader.getResourceAsStream(fn) ?: error("Can't open file from classloader $fn")
}

fun main() {
    val fn = "dat1.txt"
    val inputStream = streamForFile(fn)
    val myAry = parseSquare(inputStream)
    println(myAry)
    myAry[3, 4] = false
    val tmp = IntArray(10) { 0 }
    println(tmp.contentToString())
}

