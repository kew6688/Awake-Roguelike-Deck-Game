package com.cs446.awake.model
import com.badlogic.gdx.utils.Array

// return a test deck
fun getTestDeck(): Deck {
    val testDeck = Deck()
    var i = 1
    while (i <= 5){
        // make 5 - i attack cards with damage i
        var j = i
        while (j < 5) {
            val attackCard = ActionCard(("AttackCard$i$j"),"playerAction/itemAction2.png", "Deals ${i*10} damage after costing ${j/2} strength\n", 0, j/2, -i*10, Array<State>(0))
            testDeck.addCard(attackCard)
            j ++
        }
        // make a card that restores i health

        val restoreCard = ActionCard("RestoreCard$i", "playerAction/itemAction34.png", "Restores $i health after costing $i energy\n", i,0, i, Array<State>(0))
        testDeck.addCard(restoreCard)
        i ++
    }
    // make some state cards
    i = 1
    while (i < 4){
        val stateCard1 = ActionCard("burner$i", "playerAction/itemAction22.png", "burns target for $i rounds\n",
            i, 0, 0, Array<State>(arrayOf(State("Burn", i))))
        testDeck.addCard(stateCard1)
        val stateCard2 = ActionCard("poison", "playerAction/itemAction29.png", "poisons target for $i rounds\n",
            i-1, 0, 0, Array<State>(arrayOf(State("Poison", i), State("Sleep", i))))
        testDeck.addCard(stateCard2)
        i ++
    }
    testDeck.shuffle()
    return testDeck
}
