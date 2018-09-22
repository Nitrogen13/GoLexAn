package lexicalAnalysis

fun String.sliceUpTo(startIndex: Int, matcher: (pos: Int) -> Boolean): String? {
    var i = startIndex + 1
    while (i < length && !matcher(i))
        ++i
    return if (i < length) slice(startIndex..i) else null
}

fun String.sliceWhile(startIndex: Int, matcher: (Char) -> Boolean): String? {
    var i = startIndex
    while (i < length && matcher(get(i)))
        ++i
    return if (i > startIndex) slice(startIndex until i) else null
}

fun String.sliceWhile(startIndex: Int, char: Char): String? =
        sliceWhile(startIndex) { it == char }

fun String.sliceWhile(startIndex: Int, regex: Regex): String? =
        sliceWhile(startIndex) { regex.matches(it.toString()) }


fun String.sliceUpTo(startIndex: Int, char: Char): String? =
        sliceUpTo(startIndex) { pos -> this[pos] == char }


fun String.sliceUpTo(startIndex: Int, regex: Regex): String? =
        sliceUpTo(startIndex) { pos -> regex.matches(this[pos].toString()) }


fun String.getMatching(startIndex: Int, strings: List<String>) = strings.firstOrNull{
        slice(startIndex until (startIndex + it.length).coerceAtMost(length)) == it
}
