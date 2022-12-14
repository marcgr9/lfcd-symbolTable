import Utils.getType
import Utils.Type
import java.io.File
import java.text.ParseException

fun main(args: Array<String>) {

    val input = "src/main/resources/p1.txt"
    val outputPath = "src/main/resources/output.txt"
    val identifiers = SymbolTable()
    val constants = SymbolTable()

    val tokenAndLine = mutableListOf<Pair<String, Int>>()

    var lineNumber = 0
    File(input).forEachLine { line ->
        lineNumber += 1
        tokenize(line).forEach {
            tokenAndLine.add(Pair(it, lineNumber))
        }
    }

    File(outputPath).writeText(
        try {
            buildPIF(tokenAndLine, identifiers, constants).asPif()
        } catch (ex: ParseException) {
            println("${ex.message}    ${input.split("\\").last()}:${ex.errorOffset}")
            ""
        }
    )
}

fun tokenize(line: String): List<String> {
    val tokens = mutableListOf<String>()
    var lastSeparator = -1
    var inString = false

    for (index in line.indices) {
        if (Utils.isSeparator(line[index].toString()) && !inString) {
            if (index - lastSeparator > 1) {
                val token = line.subSequence(lastSeparator + 1, index).toString()
                tokens.add(token)
            }
            lastSeparator = index
            if (line[index] != ' ')
                tokens.add(line[index].toString())
        }
        else if (index == line.length - 1) {
            val token = line.subSequence(lastSeparator + 1, index + 1).toString()
            tokens.add(token)
        }
        if (line[index] == '"')
            inString = !inString
    }

    return tokens.filter { it.isNotEmpty() && it.isNotBlank() }
}

fun buildPIF(
    tokenAndLine: MutableList<Pair<String, Int>>,
    identifiersTable: SymbolTable,
    constTable: SymbolTable,
) : MutableList<Pair<String, Int>> {
    val result = mutableListOf<Pair<String, Int>>()

    tokenAndLine.forEach {
        when (getType(it.first)) {
            Type.IDENTIFIER -> {
                identifiersTable.put(it.first)
                identifiersTable.getPosition(it.first)?.let { pos ->
                    result.add(Pair(it.first, pos))
                }
            }
            Type.CONSTANT -> {
                constTable.put(it.first)
                constTable.getPosition(it.first)?.let { pos ->
                    result.add(Pair(it.first, pos))
                }
            }
            Type.KEYWORD -> {
                result.add(Pair(it.first, -1))
            }
            Type.OPERATOR -> {
                result.add(Pair(it.first, -1))
            }
            Type.SEPARATOR -> {
                result.add(Pair(it.first, -1))
            }
            else -> {
                throw ParseException("Invalid token: ${it.first}", it.second)
            }
        }
    }

    return result
}

fun MutableList<Pair<String, Int>>.asPif(): String
    = this.joinToString {
        "${it.first}, ${it.second}\n"
    }
