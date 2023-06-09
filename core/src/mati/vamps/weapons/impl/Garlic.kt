package mati.vamps.weapons.impl

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import mati.vamps.players.Player
import mati.vamps.weapons.LevelUpEffect
import mati.vamps.weapons.Weapon
import mati.vamps.weapons.WeaponType
import mati.vamps.weapons.projectiles.GarlicArea
import mati.vamps.weapons.projectiles.Projectile
import mati.vamps.weapons.projectiles.ProjectileFactory

class Garlic(factory: ProjectileFactory) : Weapon(factory) {

    var projectile: Projectile? = null

    override fun initialize() {
        info.name = "Garlic"
        info.type = WeaponType.GARLIC
        info.icon = "items/garlic"

        info.minDmg = 1f
        info.maxDmg = 5f

        info.appliesKnockback = false

        info.levelUpEffects.put(2, arrayOf(LevelUpEffect.INCREASE_AREA_20))
        info.levelUpEffects.put(3, arrayOf(LevelUpEffect.INCREASE_STRENGTH_10))
        info.levelUpEffects.put(4, arrayOf(LevelUpEffect.INCREASE_AREA_20))
        info.levelUpEffects.put(5, arrayOf(LevelUpEffect.INCREASE_STRENGTH_30))

    }

    override fun onTimerUp() {
    }


    override fun onUpdate(player: Player) {
        if(projectile == null) {
            projectile = projectileFactory.create(Projectile.Type.GARLIC)
            this.addProjectile(projectile!!)
        }

        projectile!!.setPosition(player.x, player.y)

        if(canLevelUp() && Gdx.input.isKeyJustPressed(Input.Keys.I)) levelUp()
    }

    override fun onDraw(batch: Batch) {
    }

}