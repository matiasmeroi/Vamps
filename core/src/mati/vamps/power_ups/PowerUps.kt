package mati.vamps.power_ups

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Interpolation.Pow
import mati.vamps.ProfileManager
import mati.vamps.players.Player
import mati.vamps.players.PlayerInfo
import com.badlogic.gdx.utils.ObjectMap
import mati.vamps.Vamps
import mati.vamps.events.EventManager
import mati.vamps.events.VEvent
import mati.vamps.utils.Utils
import java.lang.ProcessHandle.Info
import java.util.Hashtable

class PowerUps : ProfileManager.ProfileListener, EventManager.VEventListener {
    
    companion object {
        val TAG = PowerUps::class.java.getSimpleName()
    }

    enum class PowerUp(val prices: Array<Int>, val description: String, val applyFun: (PlayerInfo) -> Unit) {
        INCREASE_LIFE(arrayOf(100, 200, 300, 400), "Get +10% max life per level", { i: PlayerInfo -> i.maxHealth *= 1.1f}),
        INCREASE_ARMOR(arrayOf(100, 200, 300), "Get +10% armor per level", { i: PlayerInfo -> i.armor *= 1.1f}),
        INCREASE_MIGHT(arrayOf(200, 500), "Increase might by 10% per level", { i: PlayerInfo -> i.might *= 1.1f }),
        INCREASE_PICKUP(arrayOf(100, 200), "Increase pick-up radius by 25% per level", { i: PlayerInfo -> i.pickupRadius *= 1.5f })
    }

    private class InfoHolder {
        var info = Array(PowerUp.values().size) { i: Int -> 0 }

        fun getOwned(pu:PowerUp) : Int {
            return info[getIndexFor(pu)]
        }

        fun increase(pu: PowerUp) {
            info[getIndexFor(pu)]++
        }

        fun getPrice(pu: PowerUp) : Int {
            val idx = getIndexFor(pu)
            if(info[idx] >= pu.prices.size) return -1
            return pu.prices[info[idx]]
        }

        fun getMax(pu: PowerUp) : Int {
            return pu.prices.size
        }

        fun clear() {
            info = Array(PowerUp.values().size) { i: Int -> 0 }
        }

        private fun getIndexFor(pu: PowerUp) : Int {
            for(i in 0 until info.size) {
                if(PowerUp.values().get(i) == pu) return i
            }
            Gdx.app.error(TAG, "Power up not found")
            return -1
        }
    }

    private var purchasedInfo = InfoHolder()

    init {
        ProfileManager.addListener(this)
        EventManager.subscribe(this)
    }

    override fun onProfileEvent(e: ProfileManager.Event) {
        when(e) {
            ProfileManager.Event.SAVE -> ProfileManager.setProperty("purchased", purchasedInfo as Object)
            ProfileManager.Event.LOAD -> purchasedInfo = ProfileManager.getProperty("purchased") as InfoHolder
            ProfileManager.Event.CLEAR -> ProfileManager.removeProperty("purchased")
        }
    }

    fun applyBought() {
        for(pu in PowerUp.values()) {
            val amt = purchasedInfo.getOwned(pu)
            for(i in 0 until amt) {
                Gdx.app.log(TAG, "Applying power up: $pu")
                EventManager.announceNot2Enemies(VEvent.POWER_UP_ACTIVATED, Utils.json.toJson(pu))
            }
        }
    }

    fun getOwned(pu: PowerUp) : Int {
        return purchasedInfo.getOwned(pu)
    }

    fun getPrice(pu: PowerUp) : Int {
        return purchasedInfo.getPrice(pu)
    }

    fun refund() : Int {
        val gold = calcRefundPrice()
        purchasedInfo.clear()
        return gold
    }

    private fun calcRefundPrice() : Int {
        var c = 0
        for(pu in PowerUp.values()) {
            val owned = getOwned(pu)
            for(i in 0 until owned) {
                c += pu.prices[i]
            }
        }
        return c
    }

    private fun buy(pu: PowerUp) {
        val alreadyBought = purchasedInfo.getOwned(pu)

        if(alreadyBought >= purchasedInfo.getMax(pu)) return
        if(Vamps.goldMgr().has(pu.prices[alreadyBought])) {
            Vamps.goldMgr().spend(pu.prices[alreadyBought])
            purchasedInfo.increase(pu)
        } else {
            Gdx.app.log(TAG, "Not enough gold to buy $pu")
        }
    }

    override fun onVEvent(event: VEvent, params: String) {
        when(event)  {
            VEvent.BUY_POWER_UP -> {
                buy(Utils.json.fromJson(PowerUp::class.java, params))
            }
            VEvent.REFUND_POWER_UPS -> {

            }
        }
    }
}