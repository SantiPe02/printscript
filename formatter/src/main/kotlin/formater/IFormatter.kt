package formater

interface IFormatter {
    /**
     * Given some sourcecode it will return the same code with a standard format
     */
    fun formatString(sourceCode: String): String
}
