package lexicalAnalysis.entities

open class Token(val name: String, val lexeme: String) {
    override fun toString(): String {
        return "$name (${lexeme.replace("\r", "").replace("\n", "\\n")})"
    }
}


class KeywordToken(lexeme: String) : Token("Keyword", lexeme)

class IdentifierToken(lexeme: String) : Token("Identifier", lexeme)

class WhitespaceToken(lexeme: String) : Token("Whitespace", lexeme)

class PunctuationToken(lexeme: String) : Token("Punctuation", lexeme)

class OperatorToken(lexeme: String) : Token("Operator", lexeme)

class LiteralToken(lexeme: String) : Token("Literal", lexeme)

class CommentToken(lexeme: String) : Token("Comment", lexeme)