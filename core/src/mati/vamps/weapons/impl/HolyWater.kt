package mati.vamps.weapons.impl

import com.badlogic.gdx.graphics.g2d.Batch
import mati.vamps.players.Player
import mati.vamps.weapons.LevelUpEffect
import mati.vamps.weapons.SeparationTimer
import mati.vamps.weapons.Weapon
import mati.vamps.weapons.WeaponType
import mati.vamps.weapons.projectiles.Projectile
import mati.vamps.weapons.projectiles.ProjectileFactory

class HolyWater(factory: ProjectileFactory) : Weapon(factory), SeparationTimer.Listener {

    private var sepTimer = SeparationTimer(this, 5, info.numProjectiles)

    override fun initialize() {
        info.name = "Holy Water"
        info.type = WeaponType.HOLY_WATER
        info.icon = "items/holy_water"

        info.appliesKnockback = false

        info.coolDown = 0.3f
        info.levelUpEffects.put(1, arrayOf(LevelUpEffect.REDUCE_COOLDOWN_20))
        info.levelUpEffects.put(2, arrayOf(LevelUpEffect.INCREASE_AREA_20, LevelUpEffect.ADD_PROJECTILE))
        info.levelUpEffects.put(3, arrayOf(LevelUpEffect.ADD_PROJECTILE))
        info.levelUpEffects.put(4, arrayOf(LevelUpEffect.INCREASE_STRENGTH_30))
        info.levelUpEffects.put(5, arrayOf(LevelUpEffect.INCREASE_AREA_20))

    }

    override fun onTimerUp() {
        sepTimer.start()
    }

    override fun onUpdate(player: Player) {
        sepTimer.total = info.numProjectiles
        sepTimer.update()
    }

    override fun onDraw(batch: Batch) {
    }

    override fun onSeparationTimerUp(idx: Int) {
        val new = projectileFactory.create(Projectile.Type.HOLY_WATER)
        addProjectile(new)
    }
}