package mati.vamps.weapons

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import mati.vamps.players.Player
import mati.vamps.weapons.projectiles.Projectile
import mati.vamps.weapons.projectiles.ProjectileFactory

class Knives(factory: ProjectileFactory): Weapon(factory) {

    private var newProjectile: Boolean = false

    override fun initialize() {
        info.type = WeaponType.KNIVES
        info.name = "Knives"
        info.minDmg = 1f
        info.maxDmg = 10f
    }

    override fun onTimerUp() {
        newProjectile = true
    }

    override fun onUpdate(player: Player) {
        if(newProjectile) {
            val pr = projectileFactory.create(Projectile.Type.KNIFE, player.getDir())
            pr.setPosition(player.x, player.y)
            player.stage.addActor(pr)
            addProjectile(pr)
            newProjectile = false
        }
    }

    override fun onDraw(batch: Batch) {
    }


}