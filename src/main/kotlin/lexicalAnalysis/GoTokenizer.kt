package lexicalAnalysis

import lexicalAnalysis.entities.*

class GoTokenizer(private val sourceCode: String) : Iterable<Token?> {

    /** Position of the tokenizer, after each call of [nextToken] points to the beginning of the next token */
    private var currentPosition = 0

    /**
     * Parses next token and increases [currentPosition] if token was parsed
     * @return next token or null if there is no more tokens or there was parsing error
     */
    fun nextToken(): Token? = parseNextToken(currentPosition)
            ?.also { currentPosition += it.lexeme.length }

    /** @return true if there is more tokens to parse, false otherwise */
    fun hasNextToken(): Boolean = currentPosition < sourceCode.length

    /**
     * Parses next token starting from position [pos]
     * @return next [Token] or null if there is no more tokens or there was parsing error
     */
    private fun parseNextToken(pos: Int): Token? = parseWhitespace(pos)
            ?: parseKeyword(pos)
            ?: parseIdentifier(pos)
            ?: parseComment(pos)
            ?: parseLiteral(pos)
            ?: parseOperator(pos)
            ?: parsePunctuation(pos)

    /**
     * Parses next whitespace token starting from position [pos]
     * @return [WhitespaceToken] with whitespace symbols if any of them was found, null otherwise
     */
    private fun parseWhitespace(pos: Int) = sourceCode.sliceWhile(pos, GoSpecification.whiteSpace)
            ?.let { WhitespaceToken(it) }

    /**
     * Parses next keyword starting from position [pos]
     * @return [KeywordToken] with keyword lexeme if any keyword was found, null otherwise
     */
    private fun parseKeyword(pos: Int) = sourceCode.getFirstMatching(pos, GoSpecification.keywords)
            ?.let { KeywordToken(it) }

    /**
     *  Parses next identifier starting from position [pos]
     *  @return [IdentifierToken] with identifier lexeme if any identifier was found, null otherwise
     */
    private fun parseIdentifier(pos: Int) = sourceCode.takeIf { GoSpecification.letter.matches(it[pos].toString()) }
            ?.let { sourceCode.sliceWhile(pos, GoSpecification.identifier) }
            ?.let { IdentifierToken(it) }

    /**
     * Parses next identifier starting from position [pos]
     * @return [CommentToken] with comment lexeme if any comment was found, null otherwise
     */
    private fun parseComment(pos: Int) = when (sourceCode.slice(pos..pos + 1)) {
        "//" -> sourceCode.sliceUpTo(pos) { index ->
            sourceCode[index] == '\n' || index == sourceCode.length - 1
        }
        "/*" -> sourceCode.sliceUpTo(pos) { index ->
            index > pos + 2 && sourceCode.slice((index - 1)..index) == "*/"
        }
        else -> null
    }?.let { CommentToken(it) }

    /**
     * Parses next literal starting from position [pos]
     * @return [LiteralToken] with literal lexeme if any comment was found, null otherwise
     */
    private fun parseLiteral(pos: Int) = when {
        sourceCode[pos].isDigit() || sourceCode[pos] == '.' -> parseNumber(pos)
        sourceCode[pos] == '"' -> parseString(pos, '"')
        sourceCode[pos] == '`' -> parseString(pos, '`')
        sourceCode[pos] == '\'' -> parseString(pos, '\'')
        else -> null
    }

    /**
     * Parses next string with given [bound] symbol starting from position [pos]
     * @return [LiteralToken] with string lexeme if any string was found, null if closed string was not found
     */
    private fun parseString(pos: Int, bound: Char): LiteralToken? = sourceCode.sliceUpTo(pos) { index ->
        index != pos && sourceCode[index] == bound && sourceCode[index - 1] != '\\'
    }?.let { LiteralToken(it) }

    /**
     * Parses next punctuation token starting from position [pos]
     * @return [PunctuationToken] with punctuation lexeme if any punctuation token was found, null otherwise
     */
    private fun parsePunctuation(pos: Int) = sourceCode.getFirstMatching(pos, GoSpecification.punctuation)
            ?.let { PunctuationToken(it) }

    /**
     * Parses next operator starting from position [pos]
     * @return [OperatorToken] with operator lexeme if any operator token was found, null otherwise
     */
    private fun parseOperator(pos: Int) = sourceCode.getFirstMatching(pos, GoSpecification.operators)
            ?.let { OperatorToken(it) }

    /**
     * Parses next number token starting from position [pos]
     * @return [LiteralToken] with number lexeme if any number token was found, null otherwise
     */
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

        override fun hasNext(): Boolean = !error && hasNextToken()

        override fun next(): Token? = nextToken().also {
            if (hasNext() && it == null)
                error = true
        }
    }
}