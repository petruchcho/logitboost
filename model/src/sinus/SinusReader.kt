package sinus

import data.DataReader
import java.io.IOException
import java.util.stream.DoubleStream

class SinusReader(private val periodsCount: Double, private val size: Int) : DataReader<Double> {

    @Throws(IOException::class)
    override fun readData(): List<Double> {
        val step = Math.PI * 2.0 * periodsCount / size
        return DoubleStream.iterate(0.0, { i -> i + step }).map { x -> Math.sin(x) }.limit(size.toLong()).toArray().asList()
    }
}
