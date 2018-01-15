package finance

import data.DataReader
import data.DataWithResult

import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.ArrayList
import java.util.StringTokenizer

class FinanceReader(val apply: (List<Double>) -> List<DataWithResult>) : DataReader<DataWithResult> {

    @Throws(IOException::class)
    override fun readData(): List<DataWithResult> {
        val resultData = ArrayList<Double>()
        BufferedReader(FileReader(fileName)).use { input ->
            input.readLine()
            for (nextLine in input.lines()) {
                resultData.add(converter(nextLine))
            }
        }
        return apply(resultData)
    }

    companion object {
        private val fileName = RUB_USD
        private val converter = investmentsConverter
    }
}

const val YAHOO_ARGUMENTS = 6
const val INVESTMENTS_ARGUMENTS = 5

const val RUB_USD = "resources\\USD RUB 5 years daily.csv"
const val S_P_500 = "resources\\S&P 500 from 2016-01-04.csv"


val yahooConverter = { line: String ->
    val tokenizer = StringTokenizer(line, ",")
    tokenizer.nextToken()
    val values = DoubleArray(YAHOO_ARGUMENTS)
    for (i in 0..YAHOO_ARGUMENTS - 1) {
        values[i] = java.lang.Double.parseDouble(tokenizer.nextToken())
    }
    values[0]
}

val investmentsConverter = { line: String ->
    val tokenizer = StringTokenizer(line.replace("\"", ""), " ,")
    tokenizer.nextToken()
    tokenizer.nextToken()
    tokenizer.nextToken()
    val values = DoubleArray(INVESTMENTS_ARGUMENTS)
    for (i in 0..INVESTMENTS_ARGUMENTS - 1) {
        values[i] = java.lang.Double.parseDouble(tokenizer.nextToken())
    }
    values[0]
}