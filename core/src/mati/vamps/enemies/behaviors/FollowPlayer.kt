package mati.vamps.enemies.behaviors

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import mati.vamps.enemies.Enemy
import mati.vamps.enemies.EnemyInfo
import mati.vamps.events.EventManager
import mati.vamps.events.VEvent
import mati.vamps.utils.Utils

class FollowPlayer : Behavior() {

    private var lastPlayerInfo: Vector2 = Vector2(0f, 0f)

    override fun act(enemy: Enemy) {

    }

    override fun onEvent(enemy: Enemy, event: VEvent, params: String) {
        when(event) {
            VEvent.PLAYER_POSITION -> {
                val p = params.split(EventManager.PARAM_SEP)
                val px = Utils.json.fromJson(Float::class.java, p[0])
                val py = Utils.json.fromJson(Float::class.java, p[1])
                val moveSpeed = enemy.getEnemyInfo().moveSpeed
                val angle = MathUtils.atan2(py - enemy.y, px - enemy.x)
                if(enemy.applyingKnockBack()) {
                    enemy.velocity().add(
                        MathUtils.cos(angle) * moveSpeed,
                        MathUtils.sin(angle) * moveSpeed)
                    enemy.stopKnockBack()
                }
                else enemy.velocity().set(
                    MathUtils.cos(angle) * moveSpeed,
                    MathUtils.sin(angle) * moveSpeed)
                lastPlayerInfo.set(px, py)
            }
        }
    }
}