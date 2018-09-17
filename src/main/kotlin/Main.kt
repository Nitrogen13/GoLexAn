import java.io.File

val charRegex = Regex("\\w")
val identifier = Regex("[\\w\\d_]")
val numRegex = Regex("\\d")

val keywords = listOf(
        "break", "default", "func", "interface", "select",
        "case", "defer", "go", "map", "struct",
        "chan", "else", "goto", "package", "switch",
        "const", "fallthrough", "if", "range", "type",
        "continue", "for", "import", "return", "var"
)

fun main(args: Array<String>) {
    val lines = File("in.txt").readText()

    val input = lines
    var pos = 0

    do {
        val token = getToken(input, pos)
        println(token)
        token?.length?.let { pos += it }
    } while (token?.isNotEmpty() == true)
}


fun getToken(line: String, pos: Int): String? =
        when {
            charRegex.matches(line[pos].toString()) -> { //character
                keywords.firstOrNull {
                    // keyword
                    line.slice(pos until pos + it.length) == it
                } ?: run {
                    // identifier
                    var i = pos + 1
                    while (identifier.matches(line[i].toString()))
                        ++i
                    line.slice(pos until i)
                }
            }
        // spaces
        // new lines
            numRegex.matches(line[pos].toString()) -> "num"
            line[pos] == '/' -> "/" + when (line[pos + 1]) {
                '/', '=', '*' -> line[pos + 1].toString()
                else -> ""
            }

            else -> null
        }


