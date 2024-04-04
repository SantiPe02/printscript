package formater

data class FormatterConfiguration(
    val spaceBeforeColon: Boolean = true,
    val spaceAfterColon: Boolean = true,
    val spaceBeforeAndAfterSpace: Boolean = true,
    val lineJumpBeforePrintln: Int = 1,
)
