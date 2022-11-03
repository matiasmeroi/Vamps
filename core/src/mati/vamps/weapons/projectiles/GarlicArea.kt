package mati.vamps.weapons.projectiles

import com.badlogic.gdx.graphics.g2d.Batch
import mati.vamps.enemies.Enemy

class GarlicArea : Projectile() {

    var dealingDmg = true

    var timer = 0

    override fun isDealingDmg(): Boolean {
        return dealingDmg
    }

    override fun toRemove(): Boolean {
        return false
    }

    override fun onEnemyHit(enemy: Enemy) {

    }

    override fun onUpdate(areaMultiplier: Float, speedMultiplier: Float) {
        timer++
        if((timer % 10) == 0) dealingDmg = !dealingDmg
    }

    override fun draw(areaMultiplier: Float, batch: Batch) {
        drawCenteredAndScale(batch, texture, areaMultiplier)
    }


}