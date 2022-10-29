package mati.vamps.weapons.impl

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.Batch
import mati.vamps.players.Player
import mati.vamps.weapons.LevelUpEffect
import mati.vamps.weapons.Weapon
import mati.vamps.weapons.WeaponType
import mati.vamps.weapons.projectiles.Projectile
import mati.vamps.weapons.projectiles.ProjectileFactory

class Knives(factory: ProjectileFactory): Weapon(factory) {

    private val addTimeSep = 5
    private var addNewProjectileTimer = 0
    private var projectileIndex = 0


    override fun initialize() {
        info.type = WeaponType.KNIVES
        info.name = "Knives"
        info.minDmg = 1f
        info.maxDmg = 10f

        info.levelUpEffects.put(2, arrayOf(LevelUpEffect.ADD_PROJECTILE))
        info.levelUpEffects.put(3, arrayOf(LevelUpEffect.INCREASE_AREA_10))
        info.levelUpEffects.put(4, arrayOf(LevelUpEffect.INCREASE_STRENGTH_10))
        info.levelUpEffects.put(5, arrayOf(LevelUpEffect.ADD_PROJECTILE))
    }

    override fun onTimerUp() {
        projectileIndex = 0
    }

    override fun onUpdate(player: Player) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.L)&& canLevelUp()) levelUp()

        if(projectileIndex < info.numProjectiles && addNewProjectileTimer <= 0) {
                val pr = projectileFactory.create(Projectile.Type.KNIFE, player.getDir())
                val pos = getPosForProjectileAroundPlayer(player, projectileIndex, info.numProjectiles, info.area * pr.width / 2)
                addProjectile(pr)
                addNewProjectileTimer = addTimeSep
                projectileIndex++
        }

        if(addNewProjectileTimer > 0) addNewProjectileTimer --
    }

    override fun onDraw(batch: Batch) {
    }


}