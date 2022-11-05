package mati.vamps.power_ups

import mati.vamps.ProfileManager
import mati.vamps.events.EventManager
import mati.vamps.events.VEvent
import mati.vamps.items.ItemEffect
import mati.vamps.utils.Utils

class GoldManager: EventManager.VEventListener, ProfileManager.ProfileListener {

    private var gold = 0

    init {
        EventManager.subscribe(this)
        ProfileManager.addListener(this)
    }

    fun has(amount: Int) : Boolean { return gold >= amount }
    fun spend(amount: Int) {
        gold -= amount
    }

    override fun onVEvent(event: VEvent, params: String) {
        when(event) {
            VEvent.ITEM_EFFECT_ACTIVATED -> {
                when(Utils.json.fromJson(ItemEffect::class.java, params)) {
                    ItemEffect.COIN_1 -> gold += 1
                    ItemEffect.COIN_10 -> gold += 10
                }
            }
        }
    }

    override fun onProfileEvent(e: ProfileManager.Event) {
        when(e) {
            ProfileManager.Event.SAVE -> ProfileManager.setProperty("gold", gold as Object)
            ProfileManager.Event.LOAD -> gold = ProfileManager.getProperty("gold") as Int
            ProfileManager.Event.CLEAR -> ProfileManager.removeProperty("gold")
        }
        println("Gold: $gold")
    }
}