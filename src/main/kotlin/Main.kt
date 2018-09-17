import java.io.File

val letterRegex = Regex("[a-zA-Z]")
val identifier = Regex("[\\w\\d_]")
val numRegex = Regex("[\\d]")
val bracketRegex = Regex("[(){}\\[\\]]")
val whitespaceRegex = Regex("\\s")

// TODO fix regex for hex numbers
//val hexNumRegex = Regex("0x[a-fA-F0-9]{8}")

val keywords = listOf(
        "break", "default", "func", "interface", "select",
        "case", "defer", "go", "map", "struct",
        "chan", "else", "goto", "package", "switch",
        "const", "fallthrough", "if", "range", "type",
        "continue", "for", "import", "return", "var"
)

fun main(args: Array<String>) {
    val lines = File("in.txt").readText()

    var pos = 0
    do {
        val token = getToken(lines, pos)
        println("\"$token\"")
        token?.length?.let { pos += it }
    } while (token?.isNotEmpty() == true && pos < lines.length)
}

fun getToken(line: String, pos: Int): String? =
        when {
            whitespaceRegex.matches(line[pos].toString()) -> line.sliceWhile(pos, whitespaceRegex) //spaces, new lines, tabs
            letterRegex.matches(line[pos].toString()) -> { //character
                keywords.firstOrNull {
                    line.slice(pos until (pos + it.length).coerceAtMost(line.length)) == it // keyword
                } ?: line.sliceWhile(pos, identifier)// identifier
            }
            bracketRegex.matches(line[pos].toString()) -> line[pos].toString()
            numRegex.matches(line[pos].toString()) -> {
                val num = line.sliceWhile(pos, numRegex)
                num + when (line[pos + num!!.length]) {
                    '.', 'e', 'E' -> {
                        line[pos + num.length].toString() +
                                when {
                                    numRegex.matches(line[pos + num.length + 1].toString()) -> line.sliceWhile(pos + num.length + 1, numRegex)
                                    line[pos + num.length + 1] == '-' -> line.sliceWhile(pos + num.length + 1, numRegex)
                                    else -> ""
                                }
                    }
                    else -> ""
                }
            }// number
            else -> when (line[pos]) {
                '"' -> {
                    var num = line.sliceUpTo(pos, '"')
                    while (line[pos + num!!.length - 1] != '"' || line[pos + num.length  - 2] == '\\'){
                        num += line[pos + num.length]
                    }
                    num
                } // String
                '\'' -> line.sliceUpTo(pos, '\'') // Chars
                ',' -> ","
                ';' -> ";"

                '+' -> "+" + when (line[pos + 1]) {
                    '+', '=' -> line[pos + 1].toString()
                    else -> ""
                }
                '-' -> "-" + when (line[pos + 1]) {
                    '-', '=' -> line[pos + 1].toString()
                    else -> ""
                }
                '*' -> "*" + when {
                    line[pos + 1] == '=' -> line[pos + 1].toString()
                // TODO: Shouldn't it be letterRegexp? Isn't it another token?
                    identifier.matches(line[pos + 1].toString()) -> line.sliceWhile(pos + 1, identifier)
                    else -> ""
                }
                '.' -> "." + when (line[pos + 1]) {
                    '.' -> ".."
                    else -> ""
                }

            // equals:
                '!' -> "!" + when (line[pos + 1]) {
                    '=' -> line[pos + 1].toString()
                    else -> ""
                }
                '%' -> "%" + when (line[pos + 1]) {
                    '=' -> line[pos + 1].toString()
                    else -> ""
                }
                '^' -> "^" + when (line[pos + 1]) {
                    '=' -> line[pos + 1].toString()
                    else -> ""
                }
                ':' -> ":" + when (line[pos + 1]) {
                    '=' -> line[pos + 1].toString()
                    else -> ""
                }
                '=' -> "=" + when (line[pos + 1]) {
                    '=' -> line[pos + 1].toString()
                    else -> ""
                }

                '/' -> "/" + when (line[pos + 1]) {
                    '/' -> {
                        "/" + line.sliceUpTo(pos + 1, '\n')
                    }
                    '*' -> {
                            var num = "*"
                            while (line[pos + num.length - 1] != '*' || line[pos + num.length] != '/'){
                                num += line[pos + num.length + 1]
                            }
                            num
                    }
                    '=' -> line[pos + 1].toString()
                    else -> ""
                }
                '&' -> "&" + when (line[pos + 1]) {
                    '&', '=' -> line[pos + 1].toString()
                    '^' -> "^=" // TODO: ???
                    else -> ""
                }
                '|' -> "|" + when (line[pos + 1]) {
                    '|', '=' -> line[pos + 1].toString()
                    else -> ""
                }
                '<' -> "<" + when (line[pos + 1]) {
                    '-', '=' -> line[pos + 1].toString()
                    '<' -> "<" + when (line[pos + 1]) {
                        '=' -> line[pos + 1].toString()
                        else -> ""
                    }
                    else -> ""
                }
                '>' -> ">" + when (line[pos + 1]) {
                    '=' -> line[pos + 1].toString()
                    '>' -> ">" + when (line[pos + 1]) {
                        '=' -> line[pos + 1].toString()
                        else -> ""
                    }
                    else -> ""
                }
                else -> null
            }
        }

private fun String.sliceUpTo(startIndex: Int, matcher: (Char) -> Boolean): String? {
    var i = startIndex + 1
    while (i < length && !matcher(get(i)))
        ++i
    return if (i < length) slice(startIndex..i) else null
}

private fun String.sliceWhile(startIndex: Int, matcher: (Char) -> Boolean): String? {
    var i = startIndex + 1
    while (i < length && matcher(get(i)))
        ++i
    return slice(startIndex until i)
}

private fun String.sliceUpTo(startIndex: Int, char: Char): String? =
        sliceUpTo(startIndex) { it == char }

private fun String.sliceUpTo(startIndex: Int, regex: Regex): String? =
        sliceUpTo(startIndex) { regex.matches(it.toString()) }


private fun String.sliceWhile(startIndex: Int, char: Char): String? =
        sliceWhile(startIndex) { it == char }

private fun String.sliceWhile(startIndex: Int, regex: Regex): String? =
        sliceWhile(startIndex) { regex.matches(it.toString()) }