package mati.vamps.enemies.behaviors

import mati.vamps.enemies.Enemy
import mati.vamps.events.VEvent

abstract class Behavior {
    abstract fun act(enemy: Enemy)

    open fun onEvent(enemy: Enemy, event: VEvent, params: String) {}
}