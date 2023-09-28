package com.cs446.awake.model

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Array
import com.google.gson.Gson


// information about the player progress
// the current Enemy in battle
public var enemy : Enemy? = null
// the current Player in battle
public var player: Player? = null
// the deck of player
public var deck : Deck = Deck()
// the base HP of player
public var baseHP : Int = 30
// the base energy that the player has at beginning of battle
public var baseEnergy : Int = 5

// the storage of a player in village
public var storage: CardData = CardData(mutableListOf())
// the dungeon level that the player is at
public var dungeonLevel : Int = 1
// the succeeded battle of player
public var success : Int = 0
// the strength of the player. Now assume it to be never used up
public var strength : Int = 1000

// the materials that the player collects in dungeon
public var backPackMaterial : MaterialCardData = MaterialCardData(mutableListOf())
// the weapons that player bring into dungeon
public var backPackItem : ItemCardData = ItemCardData(mutableListOf())

// Additional storage for selected item for battle
public var battleItem : ItemCardData = ItemCardData(mutableListOf())
// Keep track of the dungeon map and whether facing boss
public var dungeonMap : DungeonMap? = null
public var activeBoss : Boolean = false

// reset all savable value
fun reset(){
    storage = CardData(mutableListOf(log, log, log, stick, stick))
    dungeonLevel = 1
    success = 0
}

// demo-only, put in VillageScreen initialize() after readJson()
fun demo(){
    storage.append(itemInfo)
    storage.append(materialInfo)
    dungeonLevel = 4
    success = 100
}

// record a success battle
fun succeed(){
    success ++
}

// get current maxHP for player
fun getHP(): Int {
    return baseHP + 5 * (success / 3)
}

// get current HP for player
fun getEnergy(): Int {
    return baseEnergy + (success / 3)
}

// save the progress of game
fun dumpJson (){// create json from class

    var json = Gson().toJson(storage, CardData::class.java)
    var handle = Gdx.files.local("storage")
    handle.writeString(json, false)

    json = Gson().toJson(dungeonLevel, Int::class.java)
    handle = Gdx.files.local("dungeonLevel")
    handle.writeString(json, false)

    json = Gson().toJson(success, Int::class.java)
    handle = Gdx.files.local("success")
    handle.writeString(json, false)
}

// load the progress of the game
fun readJson (){
    var exists = Gdx.files.local("storage").exists()
    if (exists){
        storage = CardData(kotlin.collections.mutableListOf())
        val handle=Gdx.files.local("storage")
        var json = handle.readString()
        var loaded = Gson().fromJson(json, CardData::class.java)
        // change each MergableCard to either MaterialCard or ItemCard
        for (card in loaded.getStored()){
            val loadedItem = itemInfo.find(card.cardName)
            if (loadedItem != null){
                loadedItem.count = card.count
                storage.add(loadedItem)
                continue
            }
            val loadedMaterial = materialInfo.find(card.cardName)
            if (loadedMaterial != null){
                loadedMaterial.count = card.count
                storage.add(loadedMaterial)
                continue
            }
            println("find unrecognized card: " + card.cardName)
            storage.add(card)
        }
    }

    exists = Gdx.files.local("dungeonLevel").exists()
    if (exists){
        val handle=Gdx.files.local("dungeonLevel")
        var json = handle.readString()
        dungeonLevel = Gson().fromJson(json, Int::class.java)
    }

    exists = Gdx.files.local("success").exists()
    if (exists){
        val handle=Gdx.files.local("success")
        var json = handle.readString()
        success = Gson().fromJson(json, Int::class.java)
    }


    println("restored")
}

// constants for dungeon moves
public const val INVALIDMOVE = 0
public const val EMPTY = 1
public const val COLLECT = 2
public const val BATTLE = 3
public const val NEXTLEVEL = 4

// constants for state names
public const val Burn = "Burn"
public const val Freeze = "Freeze"
public const val Poison = "Poison"
public const val Paralysis = "Paralysis"
public const val Sleep = "Sleep"

// Info
// ActionCards
val stickStrike : ActionCard = ActionCard("stick strike", "playerAction/itemAction1.png",
    "", 1, 0, -5, Array<State>(), 5)
val stoneSwordChop : ActionCard = ActionCard("stone sword chop", "playerAction/itemAction2.png",
    "", 2, 0, -10, Array<State>(), 3)
val stoneSwordStab : ActionCard = ActionCard("stone sword Stab", "playerAction/itemAction3.png",
    "", 1, 0, -5, Array<State>(), 2)
val stoneAxChop : ActionCard = ActionCard("stone axe chop", "playerAction/itemAction4.png",
    "", 2, 0, -12, Array<State>(), 3)
val stoneAxStrike : ActionCard = ActionCard("stone axe knock", "playerAction/itemAction5.png",
    "", 3, 0, -8,
    Array<State>(arrayOf(State( Paralysis, 1))), 2)
val archery : ActionCard = ActionCard("archery", "playerAction/itemAction6.png",
    "", 2, 0, -12, Array<State>(), 7)
val ironAxChop : ActionCard = ActionCard("iron axe chop", "playerAction/itemAction9.png",
    "", 2, 0, -20, Array<State>(), 4)
val ironAxStrike : ActionCard = ActionCard("iron axe knock", "playerAction/itemAction10.png",
    "", 3, 0, -15,
    Array<State>(arrayOf(State(Paralysis, 2))), 2)
val ironAxHardStrike : ActionCard = ActionCard("iron axe hard strike", "playerAction/itemAction11.png",
    "", 7, 0, -30,
    Array<State>(arrayOf(State(Burn, 2))), 1)
val ironSwordChop : ActionCard = ActionCard("iron sword chop", "playerAction/itemAction12.png",
    "", 2, 0, -15, Array<State>(), 3)
val ironSwordStab : ActionCard = ActionCard("iron sword stab", "playerAction/itemAction13.png",
    "", 1, 0, -10, Array<State>(), 2)
val ironSwordStrike : ActionCard = ActionCard("iron sword knock", "playerAction/itemAction14.png",
    "", 4, 0, -15,
    Array<State>(arrayOf(State(Paralysis, 1))), 2)
val ironHammerStrike : ActionCard = ActionCard("iron hammer knock", "playerAction/itemAction18.png",
    "", 2, 0, -12,
    Array<State>(arrayOf(State(Paralysis, 2))), 5)
val ironHammerHardStrike : ActionCard = ActionCard("iron hammer hard knock", "playerAction/itemAction19.png",
    "", 8, 0, -40,
    Array<State>(arrayOf(State(Paralysis, 2))), 2)
val boneSwordChop : ActionCard = ActionCard("bone sword chop", "playerAction/itemAction20.png",
    "", 2, 0, -12, Array<State>(), 4)
val boneSwordStab : ActionCard = ActionCard("bone sword stab", "playerAction/itemAction21.png",
    "", 1, 0, -8,
    Array<State>(arrayOf(State(Poison, 2))), 3)
val fireSwordChop : ActionCard = ActionCard("fire sword chop", "playerAction/itemAction22.png",
    "", 2, 0, -30,
    Array<State>(arrayOf(State(Burn, 1))), 7)
val fireSwordHardStrike : ActionCard = ActionCard("fire sword hard strike", "playerAction/itemAction23.png",
    "", 7, 0, -50,
    Array<State>(arrayOf(State(Burn, 2))), 2)
val electricAxChop : ActionCard = ActionCard("electric ax chop", "playerAction/itemAction27.png",
    "", 2, 0, -35, Array<State>(), 6)
val electricAxHardStrike : ActionCard = ActionCard("electric axe hard strike", "playerAction/itemAction28.png",
    "", 7, 0, -30,
    Array<State>(arrayOf(State(Paralysis, 2))), 3)
val poisonedArchery : ActionCard = ActionCard("poisoned archery", "playerAction/itemAction29.png",
    "", 2, 0, -24,
    Array<State>(arrayOf(State(Poison, 1))), 7)
val malletStrike : ActionCard = ActionCard("mallet wave", "playerAction/itemAction30.png",
    "", 3, 0, -15, Array<State>(), 5)
val malletPerform : ActionCard = ActionCard("mallet perform", "playerAction/itemAction31.png",
    "", 5, 0, -10,
    Array<State>(arrayOf(State(Sleep, 1))), 4)
val earthShieldShield : ActionCard = ActionCard("earth shield block", "playerAction/itemAction32.png",
    "", 3, 0, 30, Array<State>(), 5)
val earthShieldDash : ActionCard = ActionCard("earth shield block", "playerAction/itemAction33.png",
    "", 4, 0, -30, Array<State>(), 4)
val heal : ActionCard = ActionCard("potion heal", "playerAction/itemAction34.png",
    "", 0, 0, 20, Array<State>(), 1)

// Monster Actions
val strike11 : ActionCard = ActionCard("strike", "Monster_action/lv1action3.png",
    "", 0, 0, -3, Array<State>(arrayOf()), 20)
val strike12 : ActionCard = ActionCard("strike", "Monster_action/lv1action1.png",
    "", 0, 0, -5, Array<State>(arrayOf()), 20)
val stab12 : ActionCard = ActionCard("stab", "Monster_action/lv1action2.png",
    "", 0, 0, -7, Array<State>(arrayOf()), 10)
val dash21 : ActionCard = ActionCard("dash", "Monster_action/lv2action4.png",
    "", 0, 0, -5, Array<State>(arrayOf()), 20)
val dash22 : ActionCard = ActionCard("dash", "Monster_action/lv2action8.png",
    "", 0, 0, -5,
    Array<State>(arrayOf(State(Poison, 1))), 15)
val spew22 : ActionCard = ActionCard("spew", "Monster_action/lv2action6.png",
    "", 0, 0, -10,
    Array<State>(arrayOf(State(Poison, 2))), 5)
val hammer23 : ActionCard = ActionCard("hammer", "Monster_action/lv2action5.png",
    "", 0, 0, -10, Array<State>(arrayOf()), 15)
val hardStrike23 : ActionCard = ActionCard("hardStrike", "Monster_action/lv2action7.png",
    "", 0, 0, -20,
    Array<State>(arrayOf()), 15)
val ignite31 : ActionCard = ActionCard("ignite", "Monster_action/lv3action9.png",
    "", 0, 0, -7,
    Array<State>(arrayOf(State(Burn, 1))), 10)
val strike31 : ActionCard = ActionCard("strike", "Monster_action/lv3action10.png",
    "", 0, 0, -10, Array<State>(arrayOf()), 10)
val strike32 : ActionCard = ActionCard("strike", "Monster_action/lv3action11.png",
    "", 0, 0, -10, Array<State>(arrayOf()), 15)
val hardStrike32 : ActionCard = ActionCard("hardStrike", "Monster_action/lv3action12.png",
    "", 0, 0, -12,
    Array<State>(arrayOf(State(Burn, 2))), 5)
val shield33 : ActionCard = ActionCard("shield", "Monster_action/lv3action15.png",
    "", 0, 0, 70, Array<State>(arrayOf()), 12)
val dash33 : ActionCard = ActionCard("dash", "Monster_action/lv3action14.png",
    "", 0, 0, -15,
    Array<State>(arrayOf(State(Burn, 3), State(Paralysis, 1))), 18)
val shield41 : ActionCard = ActionCard("shield", "Monster_action/lv4action15.png",
    "", 0, 0, 50, Array<State>(arrayOf()), 10)
val spell41 : ActionCard = ActionCard("spell", "Monster_action/lv4action16.png",
    "", 0, 0, -10, Array<State>(arrayOf()), 10)
val freeze42 : ActionCard = ActionCard("freeze", "Monster_action/lv4action20.png",
    "", 0, 0, -15,
    Array<State>(arrayOf(State(Freeze, 2))), 10)
val sleep42 : ActionCard = ActionCard("sleep spell", "Monster_action/lv4action18.png",
    "", 0, 0, -15,
    Array<State>(arrayOf(State(Sleep, 1))), 10)
val stab43 : ActionCard = ActionCard("stab", "Monster_action/lv4action19.png",
    "", 0, 0, -17,
    Array<State>(arrayOf(State(Poison, 1))), 20)
val hardStrike43 : ActionCard = ActionCard("hardStrike", "Monster_action/lv4action17.png",
    "", 0, 0, -40,
    Array<State>(arrayOf(State(Freeze, 3))), 10)


// Items
val stick : ItemCard = ItemCard("stick", "material/stick.png", "",
    10, actionCards = Deck(mutableListOf(stickStrike)))
val stoneSword : ItemCard = ItemCard("stone sword", "material/stoneSword.png", "",
    5, earth = 15, actionCards = Deck(mutableListOf(stoneSwordStab, stoneSwordChop)))
val stoneAx : ItemCard = ItemCard("stone axe", "material/stoneAx.png", "",
    10, earth = 10, actionCards = Deck(mutableListOf(stoneAxChop, stoneAxStrike)))
val bow : ItemCard = ItemCard("bow", "material/bow.png", "a simple weapon",
    10, 10, metal = 10, wind = 20,
    actionCards = Deck(mutableListOf(archery)))
val ironSword : ItemCard = ItemCard("iron sword", "material/ironSword.png", "",
    10, 10, metal = 20,
    actionCards = Deck(mutableListOf(ironSwordStab, ironSwordChop, ironSwordStrike)))
val ironAx : ItemCard = ItemCard("iron ax", "material/ironAxe.png", "",
    5, 10, metal = 30,
    actionCards = Deck(mutableListOf(ironAxChop, ironAxStrike, ironAxHardStrike)))
val ironHammer : ItemCard = ItemCard("iron hammer", "material/ironHammer.png", "",
    5, earth = 10, metal = 30,
    actionCards = Deck(mutableListOf(ironHammerStrike, ironHammerHardStrike)))
val boneSword : ItemCard = ItemCard("bone sword", "material/boneSword.png", "",
    15, 10, 15,
    actionCards = Deck(mutableListOf(boneSwordChop, boneSwordStab)))
val fireSword : ItemCard = ItemCard("fire sword", "material/fireSword.png", "",
    fire = 70, metal = 25, electric = 2,
    actionCards = Deck(mutableListOf(fireSwordChop, fireSwordHardStrike)))
val electricAx : ItemCard = ItemCard("electric axe", "material/lightningAxe.png", "",
    earth = 10, metal = 20, electric = 60,
    actionCards = Deck(mutableListOf(electricAxChop, electricAxHardStrike)))
val poisonedArrow : ItemCard = ItemCard("poisoned arrow", "material/poisonArrow.png", "",
    10,  metal = 25, wind = 30,
    actionCards = Deck(mutableListOf(poisonedArchery)))
val mallet : ItemCard = ItemCard("mallet", "material/stickHammer.png", "",
    10,  water = 20, electric = 4,
    actionCards = Deck(mutableListOf(malletPerform, malletStrike)))
val earthShield : ItemCard = ItemCard("earth shield", "material/earthShield.png", "",
    earth = 70,  metal = 25, water = 5,
    actionCards = Deck(mutableListOf(earthShieldDash, earthShieldShield)))
val potion : ItemCard = ItemCard("potion", "material/potion.png", "",
    10,  water = 10, actionCards = Deck(mutableListOf(heal)))

public var itemInfo: ItemCardData = ItemCardData(mutableListOf
    (stick, stoneSword, stoneAx, bow, ironSword, ironAx, ironHammer, boneSword,
    fireSword, electricAx, poisonedArrow, mallet, earthShield, potion))

// materials
val stone : MaterialCard = MaterialCard("stone","material/stone.png", "a stone",
    earth = 5, metal = 2, water = -2, level = 1)
val log : MaterialCard = MaterialCard("log", "material/log.png", "a log",
    wood = 5, fire = 2, earth = -2, level = 1)

val ironOre : MaterialCard = MaterialCard("iron ore", "material/iron.png", "an iron ore",
    wood = -5, fire = 5, earth = 2, metal = 10, electric = 2, wind = -2, level = 2)
val goldOre : MaterialCard = MaterialCard("gold ore", "material/gold.png", "a gold",
    wood = -5, fire = 15, earth = 5, metal = 15, level = 3)
val woodGem : MaterialCard = MaterialCard("wood gem", "material/woodDiamond.png", "a wood Gem",
    wood = 50, fire = 5, earth = -20, level = 3)
val fireGem : MaterialCard = MaterialCard("fire gem", "material/fireDiamond.png", "a fire Gem",
    fire = 50, earth = 5, metal = -20, level = 3)
val earthGem : MaterialCard = MaterialCard("earth gem", "material/earthDiamond.png", "a earth Gem",
    earth = 50, metal = 5, water = -20, level = 4)
val metalGem : MaterialCard = MaterialCard("metal gem", "material/metalDiamond.png", "a metal Gem",
    metal = 50, water = 5, wood = -20, level = 4)
val waterGem : MaterialCard = MaterialCard("water gem", "material/waterDiamond.png", "a water Gem",
    wood = 5, fire = -5, water = 50, level = 4)
val electricGem : MaterialCard = MaterialCard("electric gem", "material/lightningDiamond.png", "a electric Gem",
    wood = -10, earth = -10, water = -10, electric = 50, wind = 5, level = 4)
val windGem : MaterialCard = MaterialCard("wind gem", "material/windDiamond.png", "a wind Gem",
    fire = -10, metal = -10, electric = 5, wind = 50, level = 3)
val bone : MaterialCard = MaterialCard("bone", "material/bone.png", "a bone",
    5, -2, 5, -2, -2, -2, -2,  level = 2)
val feather : MaterialCard = MaterialCard("feather", "material/feather.png", "a feather",
    fire = 5, electric = -2, wind = 10, level = 2)
val herb : MaterialCard = MaterialCard("herb", "material/herb.png", "could be used for potion",
    5, water = 5, level = 1)
    
public var materialInfo : MaterialCardData =
    MaterialCardData(mutableListOf(stone, log, ironOre, goldOre, woodGem, fireGem, earthGem, metalGem,
    waterGem, electricGem, windGem, bone, feather, herb))



val m11 = Monster(Array<String?>(arrayOf("Monster_lv1/monster1.png")), 1, 20,
    "Enemy", mapOf<MaterialCard,Int>(log to 2), Deck(mutableListOf(strike11)), "Monster_lv1/monster1_bar.png")
val m12 = Monster(Array<String?>(arrayOf("Monster_lv1/monster2.png")), 1, 30,
    "Enemy", mapOf<MaterialCard,Int>(stone to 2), Deck(mutableListOf(strike12, stab12)), "Monster_lv1/monster2_bar.png")
val m21 = Monster(Array<String?>(arrayOf("Monster_lv2/monster1.png")), 2, 50,
    "Enemy", mapOf<MaterialCard,Int>(log to 4), Deck(mutableListOf(dash21)), "Monster_lv2/monster1_bar.png")
val m22 = Monster(Array<String?>(arrayOf("Monster_lv2/monster2.png")), 2, 80,
    "Enemy", mapOf<MaterialCard,Int>(herb to 2), Deck(mutableListOf(dash22, spew22)), "Monster_lv2/monster2_bar.png")
val m23 = Monster(Array<String?>(arrayOf("Monster_lv2/monster3.png")), 2, 100,
    "Enemy", mapOf<MaterialCard,Int>(feather to 2, ironOre to 2), Deck(mutableListOf(hammer23, hardStrike23)), "Monster_lv2/monster3_bar.png")
val m31 = Monster(Array<String?>(arrayOf("Monster_lv3/monster1.png")), 3, 120,
    "Enemy", mapOf<MaterialCard,Int>(bone to 2, ironOre to 1), Deck(mutableListOf(ignite31, strike31)), "Monster_lv3/monster1_bar.png")
val m32 = Monster(Array<String?>(arrayOf("Monster_lv3/monster2.png")), 3, 150,
"Enemy", mapOf<MaterialCard,Int>(bone to 3, ironOre to 1, goldOre to 1),
    Deck(mutableListOf(strike32, hardStrike32)), "Monster_lv3/monster2_bar.png")
val m33 = Monster(Array<String?>(arrayOf("Monster_lv3/monster3.png")), 3, 180,
    "Enemy", mapOf<MaterialCard,Int>(bone to 2, fireGem to 1, goldOre to 2, stone to 2),
    Deck(mutableListOf(shield33, dash33)), "Monster_lv3/monster3_bar.png")
val m41 = Monster(Array<String?>(arrayOf("Monster_lv4/monster1.png")), 4, 160,
    "Enemy", mapOf<MaterialCard,Int>(waterGem to 1, herb to 3),
    Deck(mutableListOf(shield41, spell41)), "Monster_lv4/monster1_bar.png")
val m42 = Monster(Array<String?>(arrayOf("Monster_lv4/monster2.png")), 4, 200,
    "Enemy", mapOf<MaterialCard,Int>(waterGem to 1, herb to 5, earthGem to 1),
    Deck(mutableListOf(freeze42, sleep42)), "Monster_lv4/monster2_bar.png")
val m43 = Monster(Array<String?>(arrayOf("Monster_lv4/monster3.png")), 5, 250,
    "Enemy", mapOf<MaterialCard,Int>(waterGem to 1, herb to 5, electricGem to 1, goldOre to 2, ironOre to 1),
    Deck(mutableListOf(stab43, hardStrike43)), "Monster_lv4/monster3_bar.png")

public var monsterInfo : MonsterData = MonsterData(mutableListOf(m11, m12, m21, m22, m23, m31, m32, m33, m41, m42, m43))

