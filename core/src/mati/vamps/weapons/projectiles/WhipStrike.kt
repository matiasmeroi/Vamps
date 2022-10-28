package mati.vamps.weapons.projectiles

import com.badlogic.gdx.graphics.g2d.Batch
import mati.vamps.Vamps
import mati.vamps.enemies.Enemy

class WhipStrike: Projectile() {

    var timer: Int = 0

    override fun onInitialize() {
        timer = (info as ProjectileInfo).timeOnScreen
    }

    override fun isDealingDmg(): Boolean {
        return true
    }

    override fun toRemove(): Boolean {
        return timer <= 0
    }

    override fun onEnemyHit(enemy: Enemy) {

    }

    override fun onUpdate(areaMultiplier: Float) {
        timer--
    }

    override fun draw(areaMultiplier: Float, batch: Batch) {
        drawCenteredAndScale(batch, texture, areaMultiplier)
    }
}