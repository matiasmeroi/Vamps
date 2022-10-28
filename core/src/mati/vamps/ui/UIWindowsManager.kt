package mati.vamps.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.kotcrab.vis.ui.widget.VisWindow
import mati.vamps.players.Player
import mati.vamps.weapons.Holster

class UIWindowsManager(val stage: Stage) {

    interface UIWindow {
        fun update()
    }

    val upgradeSelectionUI = UpgradeSelectionUI()
    private val allWindows = arrayOf(upgradeSelectionUI)

    fun initialize() {
        upgradeSelectionUI.isMovable = false
        upgradeSelectionUI.isModal = true
        upgradeSelectionUI.isVisible = false

        stage.addActor(upgradeSelectionUI)
    }

    fun update() {
        for(w in allWindows) if(w.isVisible) w.update()
    }

    fun isWindowOpen() : Boolean {
        for(w in allWindows) if(w.isVisible) return true
        return false
    }

    fun showUpgradeWindow(holster: Holster) {
        upgradeSelectionUI.createOptions(holster)
        upgradeSelectionUI.toFront()
        upgradeSelectionUI.isVisible = true

        centerWindow(upgradeSelectionUI)
    }

    fun centerWindow(window: VisWindow) {
        window.setPosition(Gdx.graphics.width / 2  - window.width / 2, Gdx.graphics.height / 2  - window.height / 2)
    }


}