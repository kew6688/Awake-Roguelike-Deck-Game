package com.cs446.awake.model

import com.google.gson.Gson

class ItemCard (cardName: String, img: String, usage: String, wood: Int = 0,
                 fire: Int = 0, earth: Int = 0, metal: Int = 0, water: Int = 0, electric: Int = 0,
                  wind : Int = 0, var actionCards: Deck, count: Int = 1):
    MergableCard(cardName, img, usage, count, wood, fire, earth, metal, water, electric, wind){

    // add the action cards of this item
    fun addToDeck() {
        println("adding " + cardName)
        for (i in (0 until actionCards.count())) {
            val curCard = actionCards.pop()
            println(i.toString() + ": adding " + curCard.cardName + curCard.count.toString())
            // generate a separate copy for each card count
            for (j in (0 until curCard.count * count)) {
                val copyCard = ActionCard(
                    curCard.cardName, curCard.img, curCard.usage, curCard.energyCost,
                    curCard.strengthCost, curCard.healthChange, curCard.Effect, 1
                )
                deck.addCard(copyCard)
            }
        }
        println("current deck size ${deck.count()}")
    }
    // create a clone (deep copy) of the data
    override fun clone(): ItemCard{
        val stringItem = Gson().toJson(this, ItemCard::class.java)
        return Gson().fromJson<ItemCard>(stringItem, ItemCard::class.java)
    }
    init {
        // initialize the usage if not given
        if (usage == ""){
            for (i in (0 until actionCards.count())){
                val action = actionCards.deck[i]
                this.usage += (action.cardName + " ${action.count} times\n")
            }
        }
    }
    }