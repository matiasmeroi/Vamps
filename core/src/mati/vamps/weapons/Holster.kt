package mati.vamps.weapons

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.Array
import mati.vamps.Upgrade
import mati.vamps.UpgradeType
import mati.vamps.WeaponUpgradeInfo
import mati.vamps.WeaponUpgradeType
import mati.vamps.players.Player
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

    fun getWeaponUpgradesAvailable() : Array<Upgrade> {
        val res = Array<Upgrade>()

        val ownedTypes = Array<WeaponType>()

        for(w in weapons) {
            ownedTypes.add(w.getType())
            if(w.canLevelUp())
                res.add(WeaponUpgradeInfo(w.getType(), WeaponUpgradeType.LEVEL_UP_WEAPON,
                    "Level up ${w.getName()}: ${w.getNextLevelDescription()}"))
        }

        for(t in WeaponType.values()) {
            if(t != WeaponType.NONE && !(t in ownedTypes)) {
                res.add(WeaponUpgradeInfo(t, WeaponUpgradeType.ADD_WEAPON,
                    "Add Weapon ${t.name}: ${t.description}"))
            }
        }

        return res
    }

    fun applyWeaponUpgrade(upg: WeaponUpgradeInfo) {
        when(upg.upgradeType) {
            WeaponUpgradeType.ADD_WEAPON ->  {
                weapons.add(Weapon.createByType(upg.weaponType, projectileFactory))
            }
            WeaponUpgradeType.LEVEL_UP_WEAPON -> {
                for(w in weapons) {
                    if(w.getType() == upg.weaponType) w.levelUp()
                }
            }
        }
    }

}