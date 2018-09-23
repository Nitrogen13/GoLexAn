package lexicalAnalysis


/**
 * @return slice of string starting form [startIndex] up to position with symbol
 * that satisfies [matcher]
 * @param startIndex first index of the slice
 * @param matcher lambda that get index of a symbol and returns true if end of slice must be at that symbol
 */
fun String.sliceUpTo(startIndex: Int, matcher: (pos: Int) -> Boolean): String? {
    var i = startIndex + 1
    while (i < length && !matcher(i))
        ++i
    return if (i < length) slice(startIndex..i) else null
}

/**
 * @return slice of string starting form [startIndex] up to the last position with symbol
 * that satisfies [matcher]
 * @param startIndex first index of the slice
 * @param matcher lambda that get index of a symbol and returns true if the symbol must be part of the slice
 */
fun String.sliceWhile(startIndex: Int, matcher: (Char) -> Boolean): String? {
    var i = startIndex
    while (i < length && matcher(get(i)))
        ++i
    return if (i > startIndex) slice(startIndex until i) else null
}

fun String.sliceWhile(startIndex: Int, regex: Regex): String? =
        sliceWhile(startIndex) { regex.matches(it.toString()) }


/**
 * @return first string form [strings] that matches this string starting form [startIndex]
 * @param startIndex first index of the slices to match the strings
 * @param strings strings to match with this string
 */
fun String.getFirstMatching(startIndex: Int, strings: List<String>) = strings.firstOrNull{
        slice(startIndex until (startIndex + it.length).coerceAtMost(length)) == it
}
