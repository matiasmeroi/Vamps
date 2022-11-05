package mati.vamps.weapons.impl

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import mati.vamps.players.Player
import mati.vamps.weapons.LevelUpEffect
import mati.vamps.weapons.Weapon
import mati.vamps.weapons.WeaponType
import mati.vamps.weapons.projectiles.Bible
import mati.vamps.weapons.projectiles.Projectile
import mati.vamps.weapons.projectiles.ProjectileFactory

class UnholyBible(fact: ProjectileFactory) : Weapon(fact) {

    private var radius : Float = 100f

    override fun initialize() {
        this.cooldownTimer.setMaxTimer(650f)

        info.type = WeaponType.UNHOLY_BIBLE
        info.name = "Unholy Bible"
        info.icon = "items/bible"

        info.levelUpEffects.put(2, arrayOf(LevelUpEffect.ADD_PROJECTILE))
        info.levelUpEffects.put(3, arrayOf(LevelUpEffect.ADD_PROJECTILE))
        info.levelUpEffects.put(4, arrayOf(LevelUpEffect.INCREASE_SPEED_20))
        info.levelUpEffects.put(5, arrayOf(LevelUpEffect.INCREASE_AREA_20))
        info.levelUpEffects.put(6, arrayOf(LevelUpEffect.INCREASE_SPEED_20))
    }

    override fun onTimerUp() {
        val projectileSep = MathUtils.PI2 / info.numProjectiles
        for(i in 0 until info.numProjectiles) {
            val p = projectileFactory.create(Projectile.Type.BIBLE, ang = projectileSep * i, dist = radius)
            addProjectile(p)
        }
    }

    override fun onUpdate(player: Player) {
        for(p in projectiles) {
            p as Bible
            p.fixPosition(player.getPosition())
        }
    }

    override fun onDraw(batch: Batch) {

    }
}