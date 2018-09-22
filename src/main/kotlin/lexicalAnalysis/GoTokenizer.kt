package lexicalAnalysis

import lexicalAnalysis.entities.*

class GoTokenizer(private val sourceCode: String) : Iterable<Token?> {

    var currentPosition = 0
        private set

    fun nextToken(): Token? = parseNextToken(currentPosition)
            ?.also { currentPosition += it.lexeme.length }


    private fun parseNextToken(pos: Int): Token? = parseWhitespace(pos)
            ?: parseKeyword(pos)
            ?: parseIdentifier(pos)
            ?: parseComment(pos)
            ?: parseLiteral(pos)
            ?: parseOperator(pos)
            ?: parsePunctuation(pos)

    private fun parseWhitespace(pos: Int) = sourceCode.sliceWhile(pos, GoSpecification.whiteSpace)
            ?.let { WhitespaceToken(it) }

    private fun parseKeyword(pos: Int) = sourceCode.getMatching(pos, GoSpecification.keywords)
            ?.let { KeywordToken(it) }

    private fun parseIdentifier(pos: Int) = sourceCode.takeIf { GoSpecification.letter.matches(it[pos].toString()) }
            ?.let { sourceCode.sliceWhile(pos, GoSpecification.identifier) }
            ?.let { IdentifierToken(it) }

    private fun parseComment(pos: Int) = when (sourceCode.slice(pos..pos + 1)) {
        "//" -> sourceCode.sliceUpTo(pos) { index ->
            sourceCode[index] == '\n' || index == sourceCode.length - 1
        }
        "/*" -> sourceCode.sliceUpTo(pos) { index ->
            index > pos + 2 && sourceCode.slice((index - 1)..index) == "*/"
        }
        else -> null
    }?.let { CommentToken(it) }

    private fun parseLiteral(pos: Int) = when {
        sourceCode[pos].isDigit() || sourceCode[pos] == '.' -> parseNumber(pos)
        sourceCode[pos] == '"' -> parseString(pos, '"')
        sourceCode[pos] == '`' -> parseString(pos, '`')
        sourceCode[pos] == '\'' -> parseString(pos, '\'')
        else -> null
    }

    private fun parseString(pos: Int, type: Char): LiteralToken? = sourceCode.sliceUpTo(pos) { index ->
        index != pos && sourceCode[index] == type && sourceCode[index - 1] != '\\'
    }?.let { LiteralToken(it) }

    private fun parsePunctuation(pos: Int) = sourceCode.getMatching(pos, GoSpecification.punctuation)
            ?.let { PunctuationToken(it) }

    private fun parseOperator(pos: Int) = sourceCode.getMatching(pos, GoSpecification.operators)
            ?.let { OperatorToken(it) }


    private fun parseNumber(pos: Int): LiteralToken? =
            if (sourceCode.slice(pos..pos + 1).toLowerCase() == "0x") {
                val hexPart = sourceCode.sliceWhile(pos + 2, GoSpecification.hexDigit)
                hexPart?.let { LiteralToken("0x$it") }
            } else {
                var index = pos
                fun readInteger() = sourceCode.sliceWhile(index, GoSpecification.decimalDigit)?.also { index += it.length }

                // parse integer part
                readInteger()

                // parse floating point part
                if (sourceCode[index] == '.') {
                    index++
                    readInteger()
                }

                if (index > pos && (sourceCode[pos].isDigit() || sourceCode[pos + 1].isDigit())) {
                    // parse exponent part
                    if (sourceCode[index].toLowerCase() == 'e') {
                        ++index
                        if (sourceCode[index] == '+' || sourceCode[index] == '-')
                            ++index
                        readInteger()
                    }

                    // parse imaginary symbol
                    if (sourceCode[index] == 'i')
                        ++index

                    LiteralToken(sourceCode.slice(pos until index))
                } else {
                    null
                }
            }


    override fun iterator(): Iterator<Token?> = object : Iterator<Token?> {
        var error = false

        override fun hasNext(): Boolean = !error && currentPosition < sourceCode.length

        override fun next(): Token? = nextToken().also {
            if (hasNext() && it == null)
                error = true
        }
    }
}