package mati.vamps.players

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Actor
import mati.vamps.Entity
import mati.vamps.Utils
import mati.vamps.Vamps
import mati.vamps.events.EventManager
import mati.vamps.events.EventManager.PARAM_SEP
import mati.vamps.events.VEvent

class Player : Entity() {

    companion object {
        const val MOVE_SPEED = 3f
        const val DIAG_SPEED_MULTIPLIER = 0.707f
    }

    private var health: Float = 0f
    private lateinit var info: PlayerInfo
    private lateinit var texture: TextureRegion

    fun initialize(i: PlayerInfo) {
        this.info = i
        texture = Vamps.atlas().findRegion("players/${info.textureName}")

        if(info._w == 0f || info._h == 0f) {
            setSize(texture.regionWidth + 0f, texture.regionHeight + 0f)
        } else {
            setSize(info._w, info._h)
        }

        if(info._cw == 0f || info._ch == 0f) {
            setCollisionSize(width - 5, height - 5)
        } else {
            setCollisionSize(info._cw, info._ch)
        }
        health = info.maxHealth
    }

    fun handleInput() {
        var dx = 0f
        var dy = 0f
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            dx = 1f
        }

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            dx = -1f
        }

        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            dy= 1f
        }

        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            dy = -1f
        }

        dx *= info.moveSpeed * MOVE_SPEED
        dy *= info.moveSpeed * MOVE_SPEED

        if(dx != 0f && dy != 0f) {
            dx *= DIAG_SPEED_MULTIPLIER
            dy *= DIAG_SPEED_MULTIPLIER
        }

       setPosition(x + dx, y + dy)

        if(dx != 0f || dy != 0f) {
            EventManager.announce(VEvent.PLAYER_MOVED_BY, "${Utils.json.toJson(dx)}$PARAM_SEP${Utils.json.toJson(dy)}")
        }


    }

    override fun act(delta: Float) {
        super.act(delta)

    }


    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        drawCentered(batch, texture)
    }
}