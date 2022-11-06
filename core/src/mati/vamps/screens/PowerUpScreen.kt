package mati.vamps.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.FitViewport
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTextButton
import mati.vamps.ProfileManager
import mati.vamps.Vamps
import mati.vamps.events.EventManager
import mati.vamps.events.VEvent
import mati.vamps.power_ups.PowerUps
import mati.vamps.ui.PowerUpUIEntry
import mati.vamps.utils.Utils

class PowerUpScreen: Screen {

    val stage = Stage(
        FitViewport(
            Gdx.graphics.width + 0f, Gdx.graphics.height + 0f,
            OrthographicCamera()
        )
    )

    private val table = Table()
    private val goldLabel = VisLabel("Gold: 8")
    private val refundButton = VisTextButton("Refund")
    private val backButton = VisTextButton("Back")


    init {

        refundButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                val gd = Vamps.puMgr().refund()
                Vamps.goldMgr().add(gd)
            }
        })

        backButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Vamps.changeScreen(Vamps.ScreenType.MAIN_MENU_SCEEN)
            }
        })

        table.setFillParent(true)
        table.defaults().pad(10f)

        for(pu in PowerUps.PowerUp.values()) {
            val entry = PowerUpUIEntry(pu)
            entry.onPlusClicked(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    EventManager.announceNot2Enemies(VEvent.BUY_POWER_UP, Utils.json.toJson(pu))
                }
            })
            table.row()
            table.add(entry)
        }

        table.row()
        table.add(refundButton)
        table.add(goldLabel)
        table.row()
        table.add(backButton)

        table.pack()

        stage.addActor(table)
    }

    override fun show() {
        Vamps.multiplexer().addProcessor(stage)
    }

    override fun render(delta: Float) {
        ScreenUtils.clear(Color.BLACK)

        goldLabel.setText("Gold: ${Vamps.goldMgr().total()}")

        stage.act()
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height)
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun hide() {
        ProfileManager.save()
        Vamps.multiplexer().removeProcessor(stage)
    }

    override fun dispose() {
        stage.dispose()
    }
}