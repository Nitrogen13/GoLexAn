import lexicalAnalysis.GoTokenizer
import lexicalAnalysis.entities.CommentToken
import lexicalAnalysis.entities.WhitespaceToken
import java.io.File

const val INPUT_FILE_NAME = "in.txt"
const val OUTPUT_FILE_NAME = "out.txt"

/** Reads Go code from [INPUT_FILE_NAME], parses tokens and puts output to [OUTPUT_FILE_NAME] */
fun main(args: Array<String>) {
    val sourceCode = File(INPUT_FILE_NAME).readText()

    val output = File(OUTPUT_FILE_NAME).bufferedWriter()
    for (token in GoTokenizer(sourceCode)){
        if (token !is WhitespaceToken && token !is CommentToken){
            println(token)
            output.write("$token\n")
        }
    }
    output.close()
}