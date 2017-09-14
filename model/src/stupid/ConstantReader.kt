package stupid

import data.DataReader
import java.util.stream.DoubleStream

class ConstantReader(private val const: Double, private val size: Int) : DataReader<Double> {
    override fun readData(): List<Double> {
        return DoubleStream.generate({ const }).limit(size.toLong()).toArray().asList()
    }
}