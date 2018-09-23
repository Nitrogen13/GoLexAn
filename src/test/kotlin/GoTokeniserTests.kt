import lexicalAnalysis.GoTokenizer
import lexicalAnalysis.entities.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GoTokeniserTests {

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

}