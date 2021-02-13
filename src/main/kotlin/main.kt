import java.io.File

class FlashCard {
    private var cardStorage: MutableMap<String, String> = mutableMapOf()
    var logStorage: MutableList<String> = mutableListOf()
    private var hardestCard: MutableMap<String, Int> = mutableMapOf()

    override fun toString(): String {
        return """
            Number of Cards: ${cardStorage.size}
            Terms in Deck: ${cardStorage.keys.joinToString(", ")}
            Definitions in Deck: ${cardStorage.values.joinToString(", ")}
        """.trimIndent()
    }

    fun add() {
        val term: String
        val definition: String

        println("The card:".log(logStorage))
        while (true) {
            term = readLn(logStorage)

            if (!cardStorage.containsKey(term)) {
                println("The definition of the card:".log(logStorage))
                definition = readLn(logStorage)

                if (!cardStorage.containsValue(definition)) break
                else println("The definition \"$definition\" already exists.".log(logStorage)); return
            } else {
                println("The card \"$term\" already exists.".log(logStorage))
                return
            }
        }
        cardStorage[term] = definition
        println("The pair (\"%s\":\"%s\") has been added.\n".format(term, definition))
        return
    }

    fun remove() {
        println("Which card?".log(logStorage))
        val termToRemove = readLn(logStorage)

        if (cardStorage.containsKey(termToRemove)) {
            cardStorage.remove(termToRemove)
            println("The card has been removed.\n".log(logStorage))
        } else {
            println("Can't remove \"$termToRemove\": there is no such card.".log(logStorage))
            print(cardStorage.keys)
        }
    }

    fun import() {
        println("File name:".log(logStorage))
        val filename = readLn(logStorage)
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
            println("File not found.".log(logStorage))
            return
        }
        val cards = term.zip(definition) { a, b -> Pair(a, b) }

        cards.forEach { (key, value) ->
            cardStorage[key] = value
        }

        println("${term.size} cards have been loaded.".log(logStorage))
    }

    fun export() {
        println("File name:".log(logStorage))
        val filename = readLn(logStorage)

        val exportFile = File(filename)
        var content = ""

        cardStorage.forEach { (key, value) ->
            content += "$key\n$value\n"
        }
        exportFile.writeText(content)
        println("${cardStorage.keys.size} cards have been saved.".log(logStorage))
    }

    fun ask() {
        println("How many times to ask?".log(logStorage))
        val times = readInt()

        repeat(times) {
            val termToAns = cardStorage.keys.random()
            val correctDefinition = cardStorage[termToAns]

            var countWrong = 0

            println("Print the definition of \"$termToAns\":".log(logStorage))
            val answer = readLn(logStorage)

            when {
                answer == correctDefinition -> println("Correct!".log(logStorage))
                cardStorage.containsValue(answer) -> {
                    println(
                        "Wrong. The right answer is \"$correctDefinition\", " +
                                "but your definition is correct for \"${getKey(cardStorage, answer)}\"."
                    )
                    countWrong++
                    hardestCard[termToAns] = countWrong
                }
                else -> {
                    println("Wrong. The right answer is \"$correctDefinition\".".log(logStorage))
                    countWrong++
                    hardestCard[termToAns] = countWrong
                }
            }
        }
    }

    fun log() {
        println("File name:")
        val fileName = readLn(logStorage)

        val file = File(fileName)
        file.writeText(logStorage.joinToString("\n"))
        println("The log has been saved.")
    }


    fun hardest() {
        val maxValues = hardestCard.values.maxOrNull()
        if (maxValues == null) println("There are no cards with errors.".log(logStorage))
        val maxEntries = hardestCard.filterValues { it == maxValues }

        val result = when {
            maxEntries.size == 1 -> "The hardest card is \"${hardestCard.keys.first()}\". " +
                    "You have ${hardestCard.values.first()} errors answering it"
            maxEntries.size > 1 -> "The hardest cards are ${
                hardestCard.keys
                    .joinToString(separator = ", ", prefix = '"'.toString(), postfix = '"'.toString())
            }"
            else -> "There are no cards with errors."
        }
        println(result.log(logStorage))
    }

    fun reset() {
        hardestCard.clear()
        println("Card statistics have been reset.".log(logStorage))
    }
}

val deck = FlashCard()

fun main() {
    while (true) {
        println("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):".log(deck.logStorage))

        when (readLn(deck.logStorage)) {
            "add" -> deck.add()
            "remove" -> deck.remove()
            "import" -> deck.import()
            "export" -> deck.export()
            "ask" -> deck.ask()
            "exit" -> {
                println("Bye bye!".log(deck.logStorage))
                return
            }
            "log" -> deck.log()
            "hardest card" -> deck.hardest()
            "reset stats" -> deck.reset()
        }
    }
}

fun <K, V> getKey(map: MutableMap<K, V>, target: V) = map.filter { target == it.value }.keys.first()
fun readInt() = readLn(deck.logStorage).toInt()
fun readLn(list: MutableList<String>): String {
    val returnVal = readLine()!!
    list.add(returnVal)
    return returnVal
}

fun String.log(list: MutableList<String>): String {
    list.add(this)
    return this
}