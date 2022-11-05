package mati.vamps.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.ScreenUtils
import mati.vamps.ProfileManager
import mati.vamps.Vamps

class DeadScreen : Screen {
    override fun show() {
        ProfileManager.save()
    }

    override fun render(delta: Float) {
        ScreenUtils.clear(Color.RED)
        if(Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) Vamps.changeScreen(Vamps.ScreenType.MAIN_MENU_SCEEN)
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun hide() {
    }

    override fun dispose() {
    }
}