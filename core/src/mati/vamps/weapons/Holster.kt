package mati.vamps.weapons

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.Array
import mati.vamps.players.Player
import mati.vamps.players.UpgradeType
import mati.vamps.players.WeaponUpgradeInfo
import mati.vamps.weapons.projectiles.ProjectileFactory

class Holster(val projectileFactory: ProjectileFactory) {

    private var weapons = Array<Weapon>()

    fun add(w: Weapon) {
        weapons.add(w)
    }

    fun add(w: WeaponType) {
        weapons.add(Weapon.createByType(w, projectileFactory))
    }

    fun update(player: Player) {
        for(w in weapons)
            w.update(player)
    }

    fun draw(batch: Batch) {
        for(w in weapons) w.draw(batch)
    }

    fun getWeaponList() : Array<Weapon> {
        return weapons
    }

    fun getWeaponUpgradesAvailable() : Array<WeaponUpgradeInfo> {
        val res = Array<WeaponUpgradeInfo>()

        val ownedTypes = Array<WeaponType>()

        for(w in weapons) {
            ownedTypes.add(w.getType())
            if(w.canLevelUp())
                res.add(WeaponUpgradeInfo(w.getType(), UpgradeType.LEVEL_UP_WEAPON, w.getNextLevelDescription()))
        }

        for(t in WeaponType.values()) {
            if(t != WeaponType.NONE && !(t in ownedTypes)) {
                res.add(WeaponUpgradeInfo(t, UpgradeType.ADD_WEAPON, t.desciption))
            }
        }

        return res
    }

    fun applyWeaponUpgrade(upg: WeaponUpgradeInfo) {
        when(upg.upgradeType) {
            UpgradeType.ADD_WEAPON ->  {
                weapons.add(Weapon.createByType(upg.weaponType, projectileFactory))
            }
            UpgradeType.LEVEL_UP_WEAPON -> {
                for(w in weapons) {
                    if(w.getType() == upg.weaponType) w.levelUp()
                }
            }
        }
    }

}