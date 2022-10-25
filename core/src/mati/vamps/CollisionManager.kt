package mati.vamps

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Array
import mati.vamps.enemies.Enemy
import mati.vamps.events.EventManager
import mati.vamps.events.EventManager.PARAM_SEP
import mati.vamps.events.VEvent
import mati.vamps.players.Player
import com.badlogic.gdx.utils.Array as GdxArray

class CollisionManager {

    companion object {
        const val ENEMY_SEPARATION_ACC = 0.5f
    }

    fun run(player: Player, enemyList: GdxArray<Enemy>) {
        separateEnemies(enemyList)
        player2enemy(player, enemyList)
        weapon2enemy(player, enemyList)
    }


    fun player2enemy(player: Player, enemyList: GdxArray<Enemy>) {
        val v = Vector2()
       for(enemy in enemyList) {
           v.set(player.x, player.y)
           v.sub(enemy.getPosition())

           if(v.len2() < 100 && player.getRect().overlaps(enemy.getRect())) {
               enemy.stop()
               EventManager.announce(VEvent.PLAYER_ENEMY_COLLISION, Utils.json.toJson(enemy.getDmgPerFrame()))
           }


       }
    }


    fun separateEnemies(enemyList: GdxArray<Enemy>) {
        var nActors = enemyList.size
        var iters = (nActors * 0.9).toInt()

        while(iters > 0) {
            val idx1 = Utils.r.nextInt(nActors)
            var idx2 = idx1

            while(idx1 == idx2) idx2 = Utils.r.nextInt(nActors)

            val e1 = enemyList.get(idx1)
            val e2 = enemyList.get(idx2)

            val diff = e1.getPosition().sub(e2.getPosition())

            if(diff.len() > 50)  {
                iters--
                continue
            }

            diff.nor().scl(ENEMY_SEPARATION_ACC)

            e1.addToAcceleration(diff.x, diff.y)
            e2.addToAcceleration(-diff.x, -diff.y)

            iters--
        }
    }

    fun weapon2enemy(player: Player, enemyList: Array<Enemy>) {
        val j = Utils.json
        val weapons = player.getWeaponList()
        val enemyIter = enemyList.iterator()

        for(w in weapons) {

            for(pr in w.getProjectileList()) {
                enemyIter.reset()
                while(enemyIter.hasNext()) {
                    val enemy = enemyIter.next()

                    if(pr.getColRect(w.getAreaMultiplier()).overlaps(enemy.getColRect())) {

                        pr.onEnemyHit(enemy)
                        val dmg = w.getDmg()
                        enemy.dealDmg(dmg)

                        EventManager.announce(VEvent.ENEMY_HIT,
                            j.toJson(enemy.x) + PARAM_SEP + j.toJson(enemy.y) + PARAM_SEP + j.toJson(dmg))

                        if(enemy.isDead()) {
                            enemyIter.remove()
                        }

                    }

                }

            }

        }
    }

}