package mati.vamps.weapons.projectiles

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import mati.vamps.Vamps
import mati.vamps.enemies.Enemy
import mati.vamps.utils.Utils

class HolyWaterBottle(val playerPos: Vector2, val target: Vector2) : Projectile() {

    companion object {
        const val BOTTLE_SPEED = 15f
    }

    enum class State {
        THROWING, BURNING, OVER
    }

    private var state = State.THROWING
    private var bottlePosition = Vector2()
    private var bottleVelocity = Vector2()

    private var timer = 0

    init {
        var bottleInitialX = (target.x + Utils.r.nextGaussian() * 200).toFloat()
        bottlePosition.set(bottleInitialX, playerPos.y - Gdx.graphics.height / 2)
        val dir = Vector2().set(target).sub(bottlePosition).nor()
        bottleVelocity.set(dir).scl(BOTTLE_SPEED)

    }

    override fun onInitialize() {
        timer = (info as ProjectileInfo).timeOnScreen
    }

    override fun isDealingDmg(): Boolean {
        return state == State.BURNING
    }

    override fun toRemove(): Boolean {
        return state == State.OVER
    }

    override fun onEnemyHit(enemy: Enemy) {
    }

    override fun onUpdate(areaMultiplier: Float) {
        when(state) {
            State.THROWING -> {
                if(Vector2.dst(bottlePosition.x, bottlePosition.y, target.x, target.y) <= 30) {
                    state = State.BURNING
                } else {
                    bottlePosition.add(bottleVelocity)
                }
            }

            State.BURNING -> {
                timer--
                if(timer == 0) state = State.OVER
            }
            State.OVER -> {}
        }
    }

    override fun draw(areaMultiplier: Float, batch: Batch) {
        when(state) {
            State.THROWING -> {
                drawCenteredAndScale(batch, Vamps.atlas().findRegion("items/holy_water"), pos = bottlePosition, w = 22f, h = 22f)
            }
            State.BURNING -> {
                drawCenteredAndScale(batch, texture)
            }
            State.OVER -> {}
        }
    }
}