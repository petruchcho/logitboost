package iris

import data.DataReader
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.*

class IrisReader : DataReader<Iris> {

    @Throws(IOException::class)
    override fun readData(): List<Iris> {
        val irises = ArrayList<Iris>()
        BufferedReader(FileReader(fileName)).use { input ->
            for (nextLine in input.lines()) {
                val tokenizer = StringTokenizer(nextLine, ",")
                irises.add(readIris(tokenizer))
            }
        }
        return irises
    }

    private fun readIris(tokenizer: StringTokenizer): Iris {
        val sepalLength = java.lang.Double.parseDouble(tokenizer.nextToken())
        val sepalWidth = java.lang.Double.parseDouble(tokenizer.nextToken())
        val petalLength = java.lang.Double.parseDouble(tokenizer.nextToken())
        val petalWidth = java.lang.Double.parseDouble(tokenizer.nextToken())
        val irisClass: Iris.IrisClass
        when (tokenizer.nextToken()) {
            "Iris-setosa" -> irisClass = Iris.IrisClass.SETOSA
            "Iris-versicolor" -> irisClass = Iris.IrisClass.VERSICOLOUR
            "Iris-virginica" -> irisClass = Iris.IrisClass.VIRGINICA
            else -> throw RuntimeException("Unexpected class")
        }
        return Iris(
                sepalLength,
                sepalWidth,
                petalLength,
                petalWidth,
                irisClass)
    }

    companion object {
        private val fileName = "resources\\iris.data.txt"
    }
}
