package mati.vamps.weapons

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import mati.vamps.Utils
import mati.vamps.players.Player
import mati.vamps.weapons.projectiles.Projectile
import mati.vamps.weapons.projectiles.ProjectileFactory
import com.badlogic.gdx.utils.Array as GdxArray

abstract class Weapon(val projectileFactory: ProjectileFactory) {

    companion object {
        val TAG = Weapon::class.java.getSimpleName()
    }

    protected var info: WeaponInfo = WeaponInfo()
    protected var cooldownTimer = CooldownTimer(this)
    protected var projectiles = GdxArray<Projectile>()

    init {
        initialize()
    }
    abstract fun initialize()

    fun addProjectile(pr: Projectile) {
        projectiles.add(pr)
    }

    fun update(player: Player) {
        val iter = projectiles.iterator()
        while(iter.hasNext()) {
            val pr = iter.next()
            if(pr.toRemove()) {
                pr.remove() // remove from stage
                iter.remove() // remove from list
            }
        }

        for(pr in projectiles) {
            pr.update(info.area)
        }

        cooldownTimer.update(info.coolDown)

        onUpdate(player)
    }

    abstract fun onTimerUp()
    abstract fun onUpdate(player: Player)

    fun draw(batch: Batch) {
        for(pr in projectiles) {
            pr.draw(info.area, batch)
        }
    }
    abstract fun onDraw(batch: Batch)

    fun canLevelUp() : Boolean {
        return (info.level+1) in info.levelUpEffects.keys()
    }

    fun levelUp()  {
        info.levelUp()
        onLevelUP()
    }
    open fun onLevelUP() {}

    fun getProjectileList() : GdxArray<Projectile> {
        val res = GdxArray<Projectile>()
        for(pr in projectiles)
            if(pr.isDealingDmg()) res.add(pr)
        return res
    }

    fun getDmg() : Float {
        return (info.minDmg + Utils.r.nextInt((info.maxDmg - info.minDmg).toInt())) * info.strength
    }

    fun getAreaMultiplier() : Float { return info.area }


}



























