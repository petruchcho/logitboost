package finance

import data.DataReader
import data.DataWithResult

import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.ArrayList
import java.util.StringTokenizer

class FinanceReader(val valueIndex: Int,
                    val apply: (List<Double>) -> List<DataWithResult>) : DataReader<DataWithResult> {

    @Throws(IOException::class)
    override fun readData(): List<DataWithResult> {
        val resultData = ArrayList<Double>()
        BufferedReader(FileReader(fileName)).use { input ->
            input.readLine()
            for (nextLine in input.lines()) {
                val tokenizer = StringTokenizer(nextLine, ",")
                resultData.add(readValueFromLine(tokenizer))
            }
        }
        return apply(resultData)
    }

    private fun readValueFromLine(tokenizer: StringTokenizer): Double {
        tokenizer.nextToken()
        val values = DoubleArray(ARGUMENTS_SIZE)
        for (i in 0..ARGUMENTS_SIZE - 1) {
            values[i] = java.lang.Double.parseDouble(tokenizer.nextToken())
        }
        return values[valueIndex]
    }

    companion object {
        private val fileName = "resources\\table.csv"
        private val ARGUMENTS_SIZE = 6
    }
}
