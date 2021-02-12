import java.io.File

class FlashCard {
    private var cardStorage: MutableMap<String, String> = mutableMapOf()

    fun add() {
        val term: String
        val definition: String

        println("The card:")
        while (true) {
            term = readLn()

            if (!cardStorage.containsKey(term)) {
                println("The definition of the card:")
                definition = readLn()

                if (!cardStorage.containsValue(definition)) break
                else println("The definition \"$definition\" already exists."); return
            } else {
                println("The card \"$term\" already exists.")
                return
            }
        }
        cardStorage[term] = definition
        println("The pair (\"%s\":\"%s\") has been added.\n".format(term, definition))
        return
    }

    fun remove() {
        println("Which card?")
        val termToRemove = readLn()

        if (cardStorage.containsKey(termToRemove)) {
            cardStorage.remove(termToRemove)
            println("The card has been removed.\n")
        } else {
            println("Can't remove \"$termToRemove\": there is no such card.")
            print(cardStorage.keys)
        }
    }

    fun import() {
        println("File name:")
        val filename = readLn()
        val file = File(filename)

        val term: MutableList<String> = mutableListOf()
        val definition: MutableList<String> = mutableListOf()

        if (file.exists()) {
            val contents = file.readLines()

            for ((idx, line) in contents.withIndex()) {
                if (idx % 2 == 1) {
                    definition.add(line)
                } else if (idx % 2 == 0) {
                    term.add(line)
                }
            }
        } else {
            println("File not found.")
            return
        }
        val cards = term.zip(definition) { a, b -> Pair(a, b) }

        cards.forEach { (key, value) ->
            cardStorage[key] = value
        }

        println("${term.size} cards have been loaded.")
    }

    fun export() {
        println("File name:")
        val filename = readLn()

        val exportFile = File(filename)
        var content = ""

        cardStorage.forEach { (key, value) ->
            content += "$key\n$value\n"
        }
        exportFile.writeText(content)
        println("${cardStorage.keys.size} cards have been saved.")
    }

    fun ask() {
        println("How many times to ask?")
        val times = readInt()

        repeat(times) {
            val termToAns = cardStorage.keys.random()
            val correctDefinition = cardStorage[termToAns]

            println("Print the definition of \"$termToAns\":")
            val answer = readLn()

            when {
                answer == correctDefinition -> println("Correct!")
                cardStorage.containsValue(answer) -> {
                    println(
                        "Wrong. The right answer is \"$correctDefinition\", " +
                                "but your definition is correct for \"${getKey(cardStorage, answer)}\"."
                    )
                }
                else -> println("Wrong. The right answer is \"$correctDefinition\".")
            }
        }
    }

    override fun toString(): String {
        return """
            Number of Cards: ${cardStorage.size}
            Terms in Deck: ${cardStorage.keys.joinToString(", ")}
            Definitions in Deck: ${cardStorage.values.joinToString(", ")}
        """.trimIndent()
    }
}

fun main() {
    val deck = FlashCard()

    while (true) {
        println("Input the action (add, remove, import, export, ask, exit):")

        when (readLn()) {
            "add" -> deck.add()
            "remove" -> deck.remove()
            "import" -> deck.import()
            "export" -> deck.export()
            "ask" -> deck.ask()
            "exit" -> {
                println("Bye bye!")
                return
            }
        }
    }
}

fun <K, V> getKey(map: MutableMap<K, V>, target: V) = map.filter { target == it.value }.keys.first()
fun readInt() = readLn().toInt()
fun readLn() = readLine()!!