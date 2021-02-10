class Card(private val size: Int) {
    private var cardStorage: MutableMap<String, String> = mutableMapOf()

    fun createCards() {
        var numCards = 0
        var term: String
        var definition: String

        while (numCards < size) {
            numCards += 1

            println("Card #$numCards:")
            inner1@ while (true) {
                term = readLn()

                if (!cardStorage.containsKey(term))
                    break@inner1
                else
                    println("The term \"$term\" already exists. Try again:")
            }

            println("The definition for card #$numCards:")
            inner2@ while (true) {
                definition = readLn()

                if (!cardStorage.containsValue(definition))
                    break@inner2
                else
                    println("The definition \"$definition\" already exists. Try again:")
            }
            cardStorage[term] = definition
        }
        if (numCards == size) answerCards()
    }

    private fun answerCards() {
        cardStorage.forEach { (term, definition) ->
            println("Print the definition of \"$term\":")
            val answer = readLn()

            when {
                answer == definition -> println("Correct!")
                cardStorage.containsValue(answer) -> {
                    println("Wrong. The right answer is \"$definition\", " +
                            "but your definition is correct for \"${getKey(cardStorage, answer)}\"."
                    )
                }
                else -> println("Wrong. The right answer is \"$definition\".")
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

fun main(args: Array<String>) {
    println("Input the number of cards:")
    Card(readInt()).createCards()
}

fun <K, V> getKey(map: MutableMap<K, V>, target: V) = map.filter { target == it.value }.keys.first()
fun readInt() = readLn().toInt()
fun readLn() = readLine()!!