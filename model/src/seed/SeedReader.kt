package seed

import data.DataReader
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.*

class SeedReader : DataReader<Seed> {

    @Throws(IOException::class)
    override fun readData(): List<Seed> {
        val seeds = ArrayList<Seed>()
        BufferedReader(FileReader(fileName)).use { input ->
            for (nextLine in input.lines()) {
                val tokenizer = StringTokenizer(nextLine)
                seeds.add(readSeed(tokenizer))
            }
        }
        return seeds
    }

    private fun readSeed(tokenizer: StringTokenizer): Seed {
        val area = java.lang.Double.parseDouble(tokenizer.nextToken())
        val perimeter = java.lang.Double.parseDouble(tokenizer.nextToken())
        val compactness = java.lang.Double.parseDouble(tokenizer.nextToken())
        val kernelLength = java.lang.Double.parseDouble(tokenizer.nextToken())
        val kernelWidth = java.lang.Double.parseDouble(tokenizer.nextToken())
        val asymmetryCoefficient = java.lang.Double.parseDouble(tokenizer.nextToken())
        val kernelGrooveLength = java.lang.Double.parseDouble(tokenizer.nextToken())
        val classId = Integer.parseInt(tokenizer.nextToken())

        return Seed(
                area,
                perimeter,
                compactness,
                kernelLength,
                kernelWidth,
                asymmetryCoefficient,
                kernelGrooveLength,
                classId)
    }

    companion object {

        private val fileName = "resources\\seeds_dataset.txt"
    }
}
