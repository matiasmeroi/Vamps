package mati.vamps.enemies.behaviors

import com.badlogic.gdx.math.Vector2
import mati.vamps.enemies.Enemy

class GotoPosition(val target: Vector2, val speed: Float): Behavior() {
    override fun act(enemy: Enemy) {
        val dir = Vector2(target).sub(enemy.getPosition()).nor()
        dir.scl(speed)
        enemy.velocity().set(dir)
    }
}