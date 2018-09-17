import java.io.File

val charRegex = Regex("\\w")
val identifier = Regex("[\\w\\d_]")
val numRegex = Regex("[\\d]")
val bracketRegex = Regex("[(){}\\[\\]]")

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

    val input = "$lines  " // TODO fix
    var pos = 0

    do {
        val token = getToken(input, pos)
        println("$token|")
        token?.length?.let { pos += it }
    } while (token?.isNotEmpty() == true && pos + 1 <= lines.length)
}


fun getToken(line: String, pos: Int): String? =
        when {
            Regex("\\s").matches(line[pos].toString()) -> " " //spaces, new lines, tabs
            charRegex.matches(line[pos].toString()) -> { //character
                keywords.firstOrNull {
                    line.slice(pos until pos + it.length) == it // keyword
                } ?: run {
                    var i = pos + 1
                    while (identifier.matches(line[i].toString()))
                        ++i
                    line.slice(pos until i) // identifier
                }
            }
            bracketRegex.matches(line[pos].toString()) -> line[pos].toString()
            numRegex.matches(line[pos].toString()) -> { // number
                var i = pos + 1
                while (numRegex.matches(line[i].toString()))
                    ++i
                line.slice(pos until i)
            }
            line[pos] == '"' -> { // String
                var i = pos + 1
                while (line[i] != '"')
                    ++i
                line.slice(pos..i)
            }
            line[pos].toString() == "'" -> { // String
                var i = pos + 1
                while (line[i].toString() != "'")
                    ++i
                line.slice(pos..i)
            }
            line[pos] == '+' -> "+" + when (line[pos + 1]) {
                '+', '=' -> line[pos + 1].toString()
                else -> ""
            }
            line[pos] == '-' -> "-" + when (line[pos + 1]) {
                '-', '=' -> line[pos + 1].toString()
                else -> ""
            }
            line[pos] == '*' -> "*" + when  {
                line[pos + 1] == '=' -> line[pos + 1].toString()
                identifier.matches(line[pos + 1].toString()) -> {
                    var i = pos + 1
                    while (identifier.matches(line[i].toString()))
                        ++i
                    line.slice(pos until i) // pointer
                }
                else -> ""
            }
            line[pos] == ',' -> ","
            line[pos] == ';' -> ";"
            line[pos] == '.' -> "." + when (line[pos + 1]) {
                '.' -> ".."
                else -> ""
            }
            line[pos] == '!' -> "!" + when (line[pos + 1]) {
                '=' -> line[pos + 1].toString()
                else -> ""
            }
            line[pos] == '%' -> "%" + when (line[pos + 1]) {
                '=' -> line[pos + 1].toString()
                else -> ""
            }
            line[pos] == '^' -> "^" + when (line[pos + 1]) {
                '=' -> line[pos + 1].toString()
                else -> ""
            }
            line[pos] == ':' -> ":" + when (line[pos + 1]) {
                '=' -> line[pos + 1].toString()
                else -> ""
            }
            line[pos] == '/' -> "/" + when (line[pos + 1]) {
                '/', '=', '*' -> line[pos + 1].toString()
                else -> ""
            }
            line[pos] == '&' -> "&" + when (line[pos + 1]) {
                '&', '=' -> line[pos + 1].toString()
                '^' -> "^="
                else -> ""
            }
            line[pos] == '=' -> "=" + when (line[pos + 1]) {
                '=' -> line[pos + 1].toString()
                else -> ""
            }
            line[pos] == '|' -> "|" + when (line[pos + 1]) {
                '|', '=' -> line[pos + 1].toString()
                else -> ""
            }
            line[pos] == '<' -> "<" + when (line[pos + 1]) {
                '-', '=' -> line[pos + 1].toString()
                '<' -> "<" + when (line[pos + 1]) {
                    '=' -> line[pos + 1].toString()
                    else -> ""
                }
                else -> ""
            }
            line[pos] == '>' -> ">" + when (line[pos + 1]) {
                '=' -> line[pos + 1].toString()
                '>' -> ">" + when (line[pos + 1]) {
                    '=' -> line[pos + 1].toString()
                    else -> ""
                }
                else -> ""
            }
            else -> null
        }


