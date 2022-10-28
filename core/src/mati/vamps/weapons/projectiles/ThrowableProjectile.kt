package mati.vamps.weapons.projectiles

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import mati.vamps.utils.Utils
import mati.vamps.enemies.Enemy

class ThrowableProjectile(val dir: Vector2) : Projectile() {

    private var timer: Int = 0
    private var hitsLeft = 1
    override fun onInitialize() {
        timer = (info as ProjectileInfo).timeOnScreen

        velocity.set(dir.x * (info as ProjectileInfo).speed, dir.y * (info as ProjectileInfo).speed)
        if(dir.x != 0f && dir.y != 0f) velocity.scl(Utils.DIAG_SPEED_MULTIPLIER)
    }

    fun setMaxHits(h : Int) : ThrowableProjectile {
        hitsLeft = h
        return this
    }

    override fun isDealingDmg(): Boolean {
        return hitsLeft > 0
    }

    override fun toRemove(): Boolean {
        return timer <= 0 || hitsLeft <= 0
    }

    override fun onEnemyHit(enemy: Enemy) {
        timer = 0
        hitsLeft--
    }

    override fun onUpdate(areaMultiplier: Float) {
        timer--
        x += velocity.x
        y += velocity.y
    }

    override fun draw(areaMultiplier: Float, batch: Batch) {
        drawCenteredAndScale(batch, texture, areaMultiplier)
    }


}