package mati.vamps.enemies.behaviors

import mati.vamps.enemies.Enemy

class DoNothing : Behavior() {
    override fun act(enemy: Enemy) {
        enemy.velocity().set(0f, 0f)
    }
}