package inputProvider

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.stream.Collectors

interface InputProvider {
    fun getContent(): String
}

class FileInput(private val path: String): InputProvider {
    override fun getContent(): String {
        return try {
            val file = File(path)
            val stream = FileReader(file)
            val bufferedReader = BufferedReader(stream)
            val code = bufferedReader.lines().collect(Collectors.joining())
            bufferedReader.close()
            code
        } catch (e: IOException) {
            ""
        }
    }
}