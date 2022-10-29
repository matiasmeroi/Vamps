package mati.vamps.weapons.impl

import com.badlogic.gdx.graphics.g2d.Batch
import mati.vamps.players.Player
import mati.vamps.weapons.LevelUpEffect
import mati.vamps.weapons.SeparationTimer
import mati.vamps.weapons.Weapon
import mati.vamps.weapons.WeaponType
import mati.vamps.weapons.projectiles.Projectile
import mati.vamps.weapons.projectiles.ProjectileFactory

class MagicWand(factory: ProjectileFactory) : Weapon(factory), SeparationTimer.Listener {

    private var sepTimer = SeparationTimer(this, 5, info.numProjectiles)

    override fun initialize() {
        info.type = WeaponType.MAGIC_WAND
        info.name = "Magic Wand"

        info.minDmg = 2f
        info.maxDmg = 10f

        info.levelUpEffects.put(2, arrayOf(LevelUpEffect.ADD_PROJECTILE))
        info.levelUpEffects.put(3, arrayOf(LevelUpEffect.INCREASE_AREA_40))
        info.levelUpEffects.put(4, arrayOf(LevelUpEffect.REDUCE_COOLDOWN_20))
        info.levelUpEffects.put(5, arrayOf(LevelUpEffect.ADD_PROJECTILE))
        info.levelUpEffects.put(6, arrayOf(LevelUpEffect.INCREASE_AREA_40))
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
        val proj = projectileFactory.create(Projectile.Type.MAGIC_BULLET)
        addProjectile(proj)
    }
}