package mati.vamps.weapons

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import mati.vamps.utils.Utils
import mati.vamps.players.Player
import mati.vamps.weapons.impl.*
import mati.vamps.weapons.projectiles.Projectile
import mati.vamps.weapons.projectiles.ProjectileFactory
import com.badlogic.gdx.utils.Array as GdxArray

abstract class Weapon (val projectileFactory: ProjectileFactory) {

    companion object {
        val TAG = Weapon::class.java.getSimpleName()

        fun createByType(type: WeaponType, projectileFactory: ProjectileFactory) : Weapon {
            when(type) {
                WeaponType.NONE -> error( "Invalid type")
                WeaponType.KNIVES -> return Knives(projectileFactory)
                WeaponType.GARLIC -> return Garlic(projectileFactory)
                WeaponType.WHIP -> return Whip(projectileFactory)
                WeaponType.HOLY_WATER -> return HolyWater(projectileFactory)
                WeaponType.MAGIC_WAND -> return MagicWand(projectileFactory)
            }
        }
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

    fun getNextLevelDescription() : String { return info.getNextLevelDescription() }

    fun getProjectileList() : GdxArray<Projectile> {
        val res = GdxArray<Projectile>()
        for(pr in projectiles)
            if(pr.isDealingDmg()) res.add(pr)
        return res
    }

    fun getDmg() : Float {
        return (info.minDmg + Utils.r.nextInt((info.maxDmg - info.minDmg).toInt())) * info.strength
    }

    fun getName(): String { return info.name }
    fun getType(): WeaponType { return info.type }

    fun getAreaMultiplier() : Float { return info.area }

    fun appliesKnockback(): Boolean { return info.appliesKnockback }
    fun getKnockback() : Float { return info.knockbackStrength }

    protected fun getPosForProjectileAroundPlayer(player: Player, projectileIndex: Int, total: Int, projectileSep: Float) : Vector2 {
        val dir = player.getDir()

        val offsetX = dir.y != 0f
        val offsetY = dir.x != 0f

        var x = player.x
        var y = player.y

        if(offsetX)
            x += (total / 2 - projectileIndex) * projectileSep
        if(offsetY)
            y += (total / 2 - projectileIndex) * projectileSep

        return Vector2(x, y)
    }

    protected fun getPosOnLeft(player: Player, projectileIndex: Int, total: Int, projectileSep: Int, playerSep:Int = 54) : Vector2 {
        return Vector2(player.x - playerSep, player.y - (total / 2 - projectileIndex) * projectileSep)
    }

    protected fun getPosOnRight(player: Player, projectileIndex: Int, total: Int, projectileSep: Int, playerSep:Int = 54) : Vector2 {
        return Vector2(player.x + playerSep, player.y - (total / 2 - projectileIndex) * projectileSep)
    }

}



























