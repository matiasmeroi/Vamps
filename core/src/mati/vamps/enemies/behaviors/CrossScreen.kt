package mati.vamps.enemies.behaviors

import com.badlogic.gdx.math.Vector2
import mati.vamps.enemies.Enemy

class CrossScreen(val dir: Vector2) : Behavior() {

    private var removeTimer = 360

    override fun act(enemy: Enemy) {
        enemy.velocity().set(dir.x * Enemy.MOVE_SPEED *4, dir.y * Enemy.MOVE_SPEED *4 )

        removeTimer--
        if(removeTimer == 0) {
//            enemy.remove()
        }
    }
}