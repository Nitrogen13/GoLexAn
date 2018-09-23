package lexicalAnalysis.entities

object GoSpecification {
    val letter = Regex("[a-zA-Z_]") // allowed first symbol at an identifier
    val identifier = Regex("\\w") // allowed symbols (other than first one) at an identifier
    val decimalDigit = Regex("\\d") // decimal digit
    val hexDigit = Regex("[\\da-fA-F]") // hexadecimal digit
    val whiteSpace = Regex("\\s") // whitespace symbol


    /** Keyword lexemes */
    val keywords = listOf(
            "break", "default", "func", "interface", "select", "case",
            "defer", "goto", "go", "map", "struct", "chan", "else",
            "package", "switch", "const", "fallthrough", "if", "range",
            "type", "continue", "for", "import", "return", "var"
    )

    /** Punctuation lexemes */
    val punctuation = listOf("...", ",", ";", "[", "]", "(", ")", "{", "}")

    /** Operator lexemes */
    val operators = listOf(
            ".",
            "!=", "!",
            "%=", "%",
            "*=", "*",
            "/=", "/",
            ":=", ":",
            "==", "=",
            "^=", "^",
            "|=", "||", "|",
            "++", "+=", "+",
            "--", "-=", "-",
            ">>=", ">=", ">>", ">",
            "&^=", "&^", "&=", "&&", "&",
            "<<=", "<-", "<<", "<=", "<"
    )
}