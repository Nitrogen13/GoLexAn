package lexicalAnalysis.entities

object GoSpecification {
    val letter = Regex("[a-zA-Z_]")
    val identifier = Regex("[a-zA-Z_][\\w\\d_]*")
    val decimalDigit = Regex("\\d")
    val hexDigit = Regex("[\\da-fA-F]")
    val whiteSpace = Regex("\\s+")

    val keywords = listOf(
            "break", "default", "func", "interface", "select",
            "case", "defer", "go", "map", "struct",
            "chan", "else", "goto", "package", "switch",
            "const", "fallthrough", "if", "range", "type",
            "continue", "for", "import", "return", "var"
    )

    val punctuation = listOf("...", ",", ";", "[", "]", "(", ")", "{", "}")

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