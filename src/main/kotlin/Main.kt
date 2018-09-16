import java.io.File

var literalRegex = Regex("a-z | A-Z")
var numRegex = Regex("0-9")

fun main(args: Array<String>) {
    val lines = File("in.txt").readLines()
    analyzeLine(lines[0])
}

fun analyzeLine(line: String) {
    line.trim()
    var ch = line[0]
    loop@ for (i in 1 until line.length) {
        when {
            literalRegex.matches(ch.toString()) -> print("LITERAL")
            numRegex.matches(ch.toString()) -> print("NUMBER")
            ch == '/' -> when {
                line[1] == '/' -> {
                    print("//")
                    break@loop
                }
                line[1] == '=' -> print("/=")
            }
        }
    }
}

