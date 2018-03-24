package finance

import data.DataReader
import data.DataWithResult

import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.ArrayList
import java.util.StringTokenizer

class FinanceReader(val apply: (List<Double>) -> List<DataWithResult>) : DataReader<DataWithResult> {

    val source = RUB_USD

    @Throws(IOException::class)
    override fun readData(): List<DataWithResult> {
        val resultData = ArrayList<Double>()
        BufferedReader(FileReader(source.filename)).use { input ->
            input.readLine()
            for (nextLine in input.lines()) {
                resultData.add(source.converter(nextLine))
            }
        }
        return apply(resultData)
    }
}

val RUB_USD = Source("resources\\USD RUB 5 years daily.csv", investmentsConverter(5, 0)) // Z_MAX = 0.06, WINDOW_SIZE = 80

val GOLD_1 = Source("resources\\Gold Futures 1 year.csv", investmentsConverter(6, 0))
val GOLD_2 = Source("resources\\Gold Futures 2 years.csv", investmentsConverter(6, 0))
val GOLD_4 = Source("resources\\Gold Futures 4 years.csv", investmentsConverter(6, 0))
val GOLD_8 = Source("resources\\Gold Futures 8 years.csv", investmentsConverter(6, 0))

val S_P_500 = Source("resources\\S&P 500 from 2016-01-04.csv", yahooConverter(7, 1))
val BITCOIN = Source("resources\\bitcoin.csv", yahooConverter(8, 7))

fun yahooConverter(arguments: Int, id: Int = 0) = { line: String ->
    val tokenizer = StringTokenizer(line, ",")
    var res = 0.0
    for (i in 0..arguments - 1) {
        if (i == id) {
            res = java.lang.Double.parseDouble(tokenizer.nextToken())
        } else {
            tokenizer.nextToken()
        }
    }
    res
}

fun investmentsConverter(arguments: Int, id: Int) = { line: String ->
    val tokenizer = StringTokenizer(line.replace(",", "").replace("\"", " "), " ,")
    tokenizer.nextToken()
    tokenizer.nextToken()
    tokenizer.nextToken()
    var res = 0.0
    for (i in 0..arguments - 1) {
        if (i == id) {
            res = java.lang.Double.parseDouble(tokenizer.nextToken())
        } else {
            tokenizer.nextToken()
        }
    }
    res
}

class Source(val filename: String, val converter: (String) -> Double) {
}