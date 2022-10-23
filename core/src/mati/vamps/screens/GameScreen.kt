package mati.vamps.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.ScalingViewport
import mati.vamps.Vamps
import mati.vamps.players.Player

class GameScreen : Screen {

    val mainStage = Stage(
        FitViewport(Gdx.graphics.width + 0f, Gdx.graphics.height + 0f,
            OrthographicCamera()
        )

    )

    val sr = ShapeRenderer()
    init {
        mainStage.camera.position.set(mainStage.camera.viewportWidth/2,mainStage.camera.viewportHeight/2,0f);

        mainStage.addActor(Player())
    }

    override fun show() {

    }

    override fun render(delta: Float) {
        ScreenUtils.clear(0f, 0f, 0f, 1f)

        sr.begin(ShapeRenderer.ShapeType.Filled)
        sr.setColor(1f, 1f, 1f, 1f)
        sr.rect(0f, 0f, mainStage.viewport.worldWidth, mainStage.viewport.worldHeight)
        sr.end()

        mainStage.getBatch().setProjectionMatrix(mainStage.getCamera().combined);
        mainStage.getCamera().update();

        mainStage.draw()

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }
    }

    override fun resize(width: Int, height: Int) {
        mainStage.viewport.update(width, height)
        mainStage.camera.position.set(mainStage.camera.viewportWidth / 2,mainStage.camera.viewportHeight / 2,0f);

    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun hide() {

    }

    override fun dispose() {
        mainStage.dispose()
    }
}