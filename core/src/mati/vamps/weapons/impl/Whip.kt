package mati.vamps.weapons.impl

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import mati.vamps.players.Player
import mati.vamps.weapons.LevelUpEffect
import mati.vamps.weapons.SeparationTimer
import mati.vamps.weapons.Weapon
import mati.vamps.weapons.WeaponType
import mati.vamps.weapons.projectiles.Projectile
import mati.vamps.weapons.projectiles.ProjectileFactory

class Whip(factory: ProjectileFactory): Weapon(factory), SeparationTimer.Listener {

    private var sepTimer = SeparationTimer(this, 5, info.numProjectiles)

    private var player: Player? = null

    override fun initialize() {
        info.type = WeaponType.WHIP
        info.name = "Whip"

        info.minDmg = 1f
        info.maxDmg = 10f

        info.levelUpEffects.put(2, arrayOf(LevelUpEffect.ADD_PROJECTILE))
        info.levelUpEffects.put(3, arrayOf(LevelUpEffect.ADD_PROJECTILE))
        info.levelUpEffects.put(4, arrayOf(LevelUpEffect.ADD_PROJECTILE))
        info.levelUpEffects.put(5, arrayOf(LevelUpEffect.ADD_PROJECTILE))
    }

    override fun onTimerUp() {
        sepTimer.start()
    }

    override fun onUpdate(player: Player) {
        if(this.player == null) this.player = player
        sepTimer.total = info.numProjectiles
        sepTimer.update()
    }

    override fun onDraw(batch: Batch) {}

    override fun onSeparationTimerUp(idx: Int) {
        if(player == null) return

        var dirX = if (player!!.getDir().x >= 0)  1 else -1
        val new = projectileFactory.create(Projectile.Type.WHIP, Vector2(dirX + 0f, 0f))

        val px = if((player!!.getDir().x >= 0 && idx % 2 == 0) || (player!!.getDir().x == -1f && idx % 2 != 0))
            getPosOnRight(player!!, idx, info.numProjectiles, 15, playerSep = 110)
        else getPosOnLeft(player!!, idx, info.numProjectiles, 15,  playerSep = 110)

        new.setPosition(px.x, px.y)
        addProjectile(new)
    }
}