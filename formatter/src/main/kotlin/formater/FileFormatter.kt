package formater

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.nio.charset.Charset

class FileFormatter(val textFormatter: IFormatter) {
    fun format(dir: String) {
        val file = File(dir)
        if (file.isDirectory) {
            file.listFiles()?.forEach { format(it.path) }
        } else if (file.isFile) {
            formatFile(file)
        }
    }

    private fun formatFile(file: File) {
        if (file.extension != "ps") return
        val temptFile = File(file.parent, "tempt.ps")

        file.inputStream().buffered().use { inputStream ->
            temptFile.outputStream().buffered().use { outputStream ->
                readFileAndWriteFormattedCode(inputStream, outputStream)
            }
        }

        temptFile.renameTo(file)
    }

    /**
     * Read from input stream in blocks then format the block and writes it to the outputStream.
     * @param inputStream the stream were the sourcecode is read from.
     * @param outputStream the stream were the formatted code will be witten too.
     * @param blockSize is the size of the blocks that are read, default value is 4Kb
     */
    private fun readFileAndWriteFormattedCode(
        inputStream: BufferedInputStream,
        outputStream: BufferedOutputStream,
        blockSize: Int = 4096,
    ) {
        val buffer = ByteArray(blockSize)
        var bytesRead = inputStream.read(buffer)
        while (bytesRead != -1) {
            val block = String(buffer, 0, bytesRead, Charset.defaultCharset())
            val formattedBlock = textFormatter.formatString(block)
            outputStream.write(formattedBlock.toByteArray(Charset.defaultCharset()))
            bytesRead = inputStream.read(buffer)
        }
        inputStream.close()
        outputStream.close()
    }
}
