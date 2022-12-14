object Utils {

    enum class Type {
        IDENTIFIER,
        CONSTANT,
        SEPARATOR,
        OPERATOR,
        KEYWORD
    }

    private val keywords = listOf(
        "daca",
        "altfel",
        "ia",
        "urla",
        "apar",
    )

    private val operators = listOf(
        "+",
        "-",
        "*",
        "/",
        "%",
        "<",
        ">",
        "<=",
        ">=",
        "=",
        "<-",
        "&",
        "|",
    )

    private val separators = listOf(
        ",",
        ";",
        "(",
        ")",
        " ",
        "[",
        "]",
        "{",
        "}",
        "\n",
        "\t",
    )

    fun isIdentifier(token: String): Boolean =
        token.matches(Regex("^[a-zA-Z]([a-z|A-Z|0-9])*\$"))

    fun isConstant(token: String): Boolean =
        token.matches(Regex("^[0-9]+$")) || token.matches(Regex("^\"[a-zA-Z0-9?!@#$%^&*()=+.>< ]*\"$"))

    fun isSeparator(token: String): Boolean = separators.contains(token)

    fun isOperator(token: String): Boolean = operators.contains(token)

    fun isKeyWord(token: String): Boolean = keywords.contains(token)

    fun getType(token: String) : Type? {
        if (isSeparator(token))
            return Type.SEPARATOR
        if (isOperator(token))
            return Type.OPERATOR
        if (isKeyWord(token))
            return Type.KEYWORD
        if (isConstant(token))
            return Type.CONSTANT
        if (isIdentifier(token))
            return Type.IDENTIFIER
        return null
    }

}
