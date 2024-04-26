package formater

data class FormatterConfiguration(
    val spaceBeforeColon: Boolean = false,
    val spaceAfterColon: Boolean = true,
    val spaceBeforeAndAfterSpace: Boolean = true,
    val lineJumpBeforePrintln: Int = 1,
    val indentation: Int = 4,
)
