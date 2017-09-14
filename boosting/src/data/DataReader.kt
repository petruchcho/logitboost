package data

import java.io.IOException

interface DataReader<out T> {
    @Throws(IOException::class)
    fun readData(): List<T>
}
