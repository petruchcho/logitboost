package main.test

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter

fun main(args: Array<String>) {
    val folder = File(System.getProperty("user.dir"))
    val listOfFiles = folder.listFiles()

    val data = ArrayList<DoubleArray>()

    for (i in 1..Int.MAX_VALUE) {
        val file = findFile(listOfFiles, "default-$i") ?: break
        data.add(readFile(file))
    }
    for (i in 1..Int.MAX_VALUE) {
        val file = findFile(listOfFiles, "boosted-$i") ?: break
        data.add(readFile(file))
    }

    if (data.isEmpty()) return

    PrintWriter("total.txt").use { out ->
        val vectorSize = data[0].size
        for (i in 0 until vectorSize) {
            for (vector in data) {
                out.print(" " + vector[i] + " ")
            }
            out.println()
        }
    }
}

fun readFile(file: File): DoubleArray {
    val list = ArrayList<Double>()
    BufferedReader(FileReader(file)).use { input ->
        for (nextLine in input.lines()) {
            list.add(nextLine.toDouble())
        }
    }
    return list.toDoubleArray()
}

fun findFile(listOfFiles: Array<File>, prefixName: String): File? {
    for (i in listOfFiles.indices) {
        if (listOfFiles[i].isFile) {
            if (listOfFiles[i].name.startsWith(prefixName)) {
                return listOfFiles[i]
            }
        }
    }
    return null
}