package mati.vamps.players

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Event
import mati.vamps.Entity
import mati.vamps.Utils
import mati.vamps.Vamps
import mati.vamps.enemies.EnemyInfo
import mati.vamps.events.EventManager
import mati.vamps.events.EventManager.PARAM_SEP
import mati.vamps.events.VEvent
import java.util.IllegalFormatWidthException

class Player : Entity(), EventManager.VEventListener {

    companion object {
        const val MOVE_SPEED = 3f
        const val DIAG_SPEED_MULTIPLIER = 0.707f
    }

    private var health: Float = 0f
    private var healthBarWidth: Int = 0
    private var healthBarHeight: Int = 0

    override fun initialize(i: Info) {
        super.initialize(i)
        i as PlayerInfo
        health = i.maxHealth
        healthBarWidth = Vamps.atlas().findRegion("players/health_bar_black").regionWidth
        healthBarHeight = Vamps.atlas().findRegion("players/health_bar_black").regionHeight
        EventManager.subscribe(this)
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

        dx *= (info as PlayerInfo).moveSpeed * MOVE_SPEED
        dy *= (info as PlayerInfo).moveSpeed * MOVE_SPEED

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
        if((Gdx.graphics.frameId.toInt() % 20) == 0) {
            val jx = Utils.json.toJson(x)
            val jy = Utils.json.toJson(y)
            EventManager.announce(VEvent.PLAYER_POSITION, jx+ PARAM_SEP+jy)
        }
    }

    fun drawHealthBar(batch: Batch?) {
        val yoff = height / 2 + healthBarHeight + 3

        batch!!.draw(Vamps.atlas().findRegion("players/health_bar_black"), x - healthBarWidth / 2, y - yoff)

        val healthWidth =  healthBarWidth * (health / (info as PlayerInfo).maxHealth)
        batch!!.draw(Vamps.atlas().findRegion("players/health_bar_red"), x - healthBarWidth / 2, y - yoff, healthWidth, healthBarHeight+0f)
    }


    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        drawCentered(batch, texture)
        drawHealthBar(batch)
    }

    override fun onVEvent(event: VEvent, params: String) {
        when(event) {
            VEvent.PLAYER_ENEMY_COLLISION -> {
                val dmgPerFrame = Utils.json.fromJson(Float::class.java, params)
                health -= dmgPerFrame
            }
        }
    }
}