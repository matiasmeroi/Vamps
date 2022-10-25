package mati.vamps.weapons

import kotlin.math.max

class CooldownTimer(val listener: Weapon) {

    companion object {
        const val MAX_TIMER = 100f
    }

    protected var timer = 0f

    fun update(cooldown: Float) {
        timer -= cooldown

        if(timer <= 0) {
            listener.onTimerUp()
            timer = MAX_TIMER
        }
    }

}