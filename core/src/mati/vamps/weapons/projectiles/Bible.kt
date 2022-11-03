package mati.vamps.weapons.projectiles

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import mati.vamps.enemies.Enemy

class Bible(var angle: Float, var radius: Float): Projectile() {

    var onScreenTimer = 0
    var deltaAng = 1f

    override fun onInitialize() {
        super.onInitialize()
        onScreenTimer = (info as ProjectileInfo).timeOnScreen
        deltaAng = (info as ProjectileInfo).speed
    }

    override fun isDealingDmg(): Boolean {
        return true
    }

    override fun toRemove(): Boolean {
        return  onScreenTimer <= 0
    }

    override fun onEnemyHit(enemy: Enemy) {
    }

    override fun onUpdate(areaMultiplier: Float, speedMultiplier: Float) {
        angle += (info as ProjectileInfo).speed
        onScreenTimer--
    }

    fun fixPosition(playerPos: Vector2) {
        this.setPosition(playerPos.x + MathUtils.cos(angle) * radius, playerPos.y + MathUtils.sin(angle) * radius)
    }

    override fun draw(areaMultiplier: Float, batch: Batch) {
        drawCenteredAndScale(batch, texture, areaMultiplier)
    }
}