package mati.vamps.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.kotcrab.vis.ui.widget.VisList
import com.kotcrab.vis.ui.widget.VisWindow
import mati.vamps.utils.Utils
import mati.vamps.players.WeaponUpgradeInfo
import mati.vamps.weapons.Holster

import com.badlogic.gdx.utils.Array as GdxArray

class UpgradeSelectionUI : VisWindow("Select upgrade"), UIWindowsManager.UIWindow {

    interface Listener {
        fun onUpgradeSelected(selected: WeaponUpgradeInfo)
    }

    companion object {
        const val MAX_OPTIONS = 3
    }

    lateinit var listener: Listener
    private val list = VisList<String>()

    private var options = GdxArray<WeaponUpgradeInfo>()

    init {
        this.add(list)
    }

    override fun update() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) list.selectedIndex = (list.selectedIndex+1) % list.items.size
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            list.selectedIndex =
                if(list.selectedIndex > 0 )
                    (list.selectedIndex - 1) % list.items.size
                else list.items.size - 1

        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            this.isVisible = false
            if(options.size > 0) listener.onUpgradeSelected(options.get(list.selectedIndex))
        }
    }

    fun createOptions(holster: Holster) : Boolean {
        val upgradesAvailable = holster.getWeaponUpgradesAvailable()

        if(upgradesAvailable.size == 0) return false

        val givenOptions = GdxArray<String>()
        options.clear()

        var added = 0
        while(!upgradesAvailable.isEmpty && added < MAX_OPTIONS) {
            val idx = Utils.r.nextInt(upgradesAvailable.size)
            val uv = upgradesAvailable[idx]
            givenOptions.add("${uv.upgradeType} -> ${uv.weaponType} -> ${uv.description}")
            options.add(uv)
            upgradesAvailable.removeIndex(idx)
            added++
        }


        list.setItems(givenOptions)
        list.pack()

        this.pack()

        return true
    }

}