package mati.vamps.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

class KeyboardInputHandler : UIInputHandler {

    override fun poll() {}

    override fun up(): Boolean {
        return Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W)
    }

    override fun down(): Boolean {
        return Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S)
    }

    override fun left(): Boolean {
        return Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.A)
    }

    override fun right(): Boolean {
        return Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.D)
    }

    override fun enter(): Boolean {
        return Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
    }

    override fun escape(): Boolean {
        return Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
    }
}