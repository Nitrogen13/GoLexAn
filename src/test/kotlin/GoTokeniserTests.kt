import lexicalAnalysis.GoTokenizer
import lexicalAnalysis.entities.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GoTokeniserTests {

    private val identifiers = listOf(
            "a", "_x9", "ThisVariableIsExported", "αβ", "a٩", "ИдентифаерНаВеликомИМогучемРусскомЯзыке"
    )

    @Test
    fun identifiersTest() {
        identifiers.forEach {
            val token = GoTokenizer(it).nextToken()
            Assertions.assertEquals(it, token?.lexeme)
            Assertions.assertTrue(token is IdentifierToken)
        }
    }

    private val strings = listOf(
            "'a'", "'ä'", "'本'", "'\\t'", "'\\000'", "'\\007'", "'\\377'", "'\\x07'", "'\\xff'", "'\\u12e4'",
            "'\\U00101234'", "'\\''", "`abc`", "`\\n\n\\n`", "\"\\n\"", "\"\\\"\"", "\"Hello, world!\\n\"", "\"日本語\"",
            "\"\\u65e5本\\U00008a9e\"", "\"\\xff\\u00FF\"", "\"\\U00110000\""
    )

    @Test
    fun stringsTest() {
        strings.forEach {
            val token = GoTokenizer(it).nextToken()
            Assertions.assertEquals(it, token?.lexeme)
            Assertions.assertTrue(token is LiteralToken)
        }
    }

    private val numbers = listOf(
            "0", "42", "0600", "0xBadFace", "170141183460469231731687303715884105727", "0.", "72.40", "072.40", "2.71828",
            "1.e+0", "6.67428e-11", "1E6", ".25", ".12345E+5", "0i", "011i", "0.i", "2.71828i", "1.e+0i",
            "6.67428e-11i", "1E6i", ".25i", ".12345E+5i"
    )

    @Test
    fun numbersTest() {
        numbers.forEach {
            val token = GoTokenizer(it).nextToken()
            Assertions.assertEquals(it, token?.lexeme)
            Assertions.assertTrue(token is LiteralToken)
        }
    }

    @Test
    fun operatorsTest() {
        GoSpecification.operators.forEach {
            val token = GoTokenizer(it).nextToken()
            Assertions.assertEquals(it, token?.lexeme)
            Assertions.assertTrue(token is OperatorToken)
        }
    }

    @Test
    fun punctuationTest() {
        GoSpecification.punctuation.forEach {
            val token = GoTokenizer(it).nextToken()
            Assertions.assertEquals(it, token?.lexeme)
            Assertions.assertTrue(token is PunctuationToken)
        }
    }

    @Test
    fun keywordsTest() {
        GoSpecification.keywords.forEach {
            val token = GoTokenizer(it).nextToken()
            Assertions.assertEquals(it, token?.lexeme)
            Assertions.assertTrue(token is KeywordToken)
        }
    }

    private val code = mapOf(
            "++a" to listOf(OperatorToken("++"), IdentifierToken("a")),
            "[1, 2]" to listOf(
                    PunctuationToken("["), LiteralToken("1"), PunctuationToken(","),
                    LiteralToken("2"), PunctuationToken("]")
            ),
            "a = b + \"text\"" to listOf(
                    IdentifierToken("a"), OperatorToken("="), IdentifierToken("b"),
                    OperatorToken("+"), LiteralToken("\"text\"")
            ),
            "import test;" to listOf(
                    KeywordToken("import"), IdentifierToken("test"), PunctuationToken(";")
            ),
            "if n != .12345E+5i { return \"oh no...\" }" to listOf(
                    KeywordToken("if"), IdentifierToken("n"), OperatorToken("!="),
                    LiteralToken(".12345E+5i"), PunctuationToken("{"), KeywordToken("return"),
                    LiteralToken("\"oh no...\""), PunctuationToken("}")
            )
    )

    @Test
    fun codeTest(){
        code.forEach { code, tokens ->
            GoTokenizer(code).filter { it !is WhitespaceToken }.forEachIndexed { index, token ->
                Assertions.assertEquals(tokens[index].lexeme, token?.lexeme)
                Assertions.assertEquals(tokens[index].name, token?.name)
            }
        }
    }

}