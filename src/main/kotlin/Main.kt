import lexicalAnalysis.GoTokenizer
import lexicalAnalysis.entities.WhitespaceToken
import java.io.File

fun main(args: Array<String>) {
    val source = File("in.txt").readText()

    for (token in GoTokenizer(source)){
        if (token !is WhitespaceToken)
            println("$token")
    }
}