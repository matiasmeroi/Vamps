package mati.vamps.weapons

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.ObjectMap


class WeaponInfo {
    companion object {
        val TAG = WeaponInfo::class.java.getSimpleName()

        const val DEF_KNOCKBACK = 0.4f
    }

    var type: WeaponType = WeaponType.NONE
    var name = "NoName"

    var level: Int = 1
    var numProjectiles: Int = 1

    var appliesKnockback: Boolean = true
    var knockbackStrength: Float = DEF_KNOCKBACK

    var strength = 1f
    var area = 1f
    var coolDown = 1f

    var minDmg = 1f
    var maxDmg = 1f

    val levelUpEffects = ObjectMap<Int, Array<LevelUpEffect>>()

    fun levelUp() {
        level++
        val effects = levelUpEffects.get(level)
        for(e in effects) applyEffect(e)
    }

    private fun applyEffect(effect: LevelUpEffect) {
        Gdx.app.log(TAG, "Applying effect <${effect.description}> to $name")
        when(effect) {
            LevelUpEffect.INCREASE_STRENGTH_10 ->
                strength *= 1.1f
            LevelUpEffect.INCREASE_STRENGTH_20 ->
                strength *= 1.2f
            LevelUpEffect.INCREASE_STRENGTH_30 ->
                strength *= 1.3f
            LevelUpEffect.ADD_PROJECTILE ->
                numProjectiles++
            LevelUpEffect.INCREASE_AREA_10 ->
                area *= 1.1f
            LevelUpEffect.INCREASE_AREA_20 ->
                area *= 1.2f
            LevelUpEffect.INCREASE_AREA_40 ->
                area *= 1.4f
            LevelUpEffect.REDUCE_COOLDOWN_8 ->
                coolDown *= 0.92f
        }
    }

    fun getNextLevelDescription() : String {
        val nextLevel = level + 1
        if(!(nextLevel in levelUpEffects.keys())) return "Can`t reach level $nextLevel"

        var res = ""
        val effects = levelUpEffects.get(nextLevel)

        res += effects[0].description

        for(i in 1 until effects.size) {
            res += " $ ${effects[i].description}"
        }

        return res
    }

}