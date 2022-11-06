package mati.vamps.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
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

class DeadScreen : Screen {

    val stage = Stage(
        FitViewport(
            Gdx.graphics.width + 0f, Gdx.graphics.height + 0f,
            OrthographicCamera()
        )
    )

    private val table = Table()
    private val timeLabel = VisLabel("")
    private val goldLabel = VisLabel("")
    private val okButton = VisTextButton("Ok")

    init {
        okButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                EventManager.announceNot2Enemies(VEvent.RESET_GAME, "")
                Vamps.changeScreen(Vamps.ScreenType.MAIN_MENU_SCEEN)
            }
        })

        table.setFillParent(true)
        table.defaults().pad(30f)

        table.add(timeLabel)
        table.row()
        table.add(goldLabel)
        table.row()
        table.add(okButton)

        stage.addActor(table)
    }

    override fun show() {
        Vamps.multiplexer().addProcessor(stage)
        ProfileManager.save()
        EventManager.announceNot2Enemies(VEvent.RESET_GAME, "")
    }

    override fun render(delta: Float) {
        ScreenUtils.clear(Color.BLACK)

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
        Vamps.multiplexer().removeProcessor(stage)
    }

    override fun dispose() {
        stage.dispose()
    }
}