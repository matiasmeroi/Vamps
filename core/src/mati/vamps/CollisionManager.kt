package mati.vamps

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import mati.vamps.enemies.Enemy
import mati.vamps.events.EventManager
import mati.vamps.events.EventManager.PARAM_SEP
import mati.vamps.events.VEvent
import mati.vamps.items.Item
import mati.vamps.players.Player
import mati.vamps.utils.Utils
import mati.vamps.weapons.Holster
import com.badlogic.gdx.utils.Array as GdxArray

class CollisionManager {

    companion object {
        const val ENEMY_SEPARATION_ACC = 0.7f
    }

    fun run(player: Player, enemyList: GdxArray<Enemy>, itemList: GdxArray<Item>, holster: Holster) {
        separateEnemies(enemyList)
        player2enemy(player, enemyList)
        weapon2enemy(player, holster, enemyList)
        player2item(player, itemList)
    }


    private fun player2enemy(player: Player, enemyList: GdxArray<Enemy>) {
        val v = Vector2()
       for(enemy in enemyList) {
           v.set(player.x, player.y)
           v.sub(enemy.getPosition())

           if(enemy.getDmgPerFrame() == 0f) continue

           if(player.getColRect().overlaps(enemy.getColRect())) {
               enemy.stop()
               EventManager.announceNot2Enemies(VEvent.PLAYER_ENEMY_COLLISION, Utils.json.toJson(enemy.getDmgPerFrame()))
           }


       }
    }

    private fun separateEnemies(enemyList: GdxArray<Enemy>) {
        var nActors = enemyList.size
        var iters = (nActors * 0.9).toInt()

        while(iters > 0) {
            val idx1 = Utils.r.nextInt(nActors)
            var idx2 = idx1

            while(idx1 == idx2) idx2 = Utils.r.nextInt(nActors)

            val e1 = enemyList.get(idx1)
            val e2 = enemyList.get(idx2)

            val diff = e1.getPosition().sub(e2.getPosition())

            if(diff.len() >60)  {
                iters--
                continue
            }

            diff.nor().scl(ENEMY_SEPARATION_ACC)

            e1.addToAcceleration(diff.x, diff.y)
            e2.addToAcceleration(-diff.x, -diff.y)

            iters--
        }
    }

    private fun weapon2enemy(player: Player, holster: Holster, enemyList: Array<Enemy>) {
        val j = Utils.json
        val weapons = holster.getWeaponList()
        val enemyIter = enemyList.iterator()

        for(w in weapons) {

            for(pr in w.getProjectileList()) {
                enemyIter.reset()
                while(enemyIter.hasNext()) {
                    val enemy = enemyIter.next()

                    if(pr.getColRect(w.getAreaMultiplier()).overlaps(enemy.getColRect())
                        && !pr.isEnityOnTimeOut(enemy)) {

                        pr.onEnemyHit(enemy)
                        if(w.appliesKnockback()) enemy.applyKnockback(w.getKnockback())
                        val dmg = w.getDmg(player)
                        enemy.dealDmg(dmg)

                        EventManager.announceNot2Enemies(VEvent.ENEMY_HIT,
                            j.toJson(enemy.x) + PARAM_SEP + j.toJson(enemy.y) + PARAM_SEP + j.toJson(dmg))

                        pr.timeOutEntity(enemy)

                        if(enemy.isDead()) {
                            if(!enemy.hasOnKillEvent())
                                EventManager.announceNot2Enemies(VEvent.ENEMY_KILLED, j.toJson(enemy.x) + PARAM_SEP + j.toJson(enemy.y) + PARAM_SEP + j.toJson(enemy.getEnemyInfo())+ PARAM_SEP +j.toJson(enemy.entityId))
                            enemyIter.remove()
                        }

                    }

                }

            }

        }
    }

    private fun player2item(player: Player, itemList: GdxArray<Item>) {
        val iter = itemList.iterator()
        while(iter.hasNext()) {
            val item = iter.next()

            if(player.getItemPickUpRect().overlaps(item.getColRect())) {
                EventManager.announceNot2Enemies(VEvent.ITEM_EFFECT_ACTIVATED, Utils.json.toJson(item.onPickUpEffect()))
                item.remove()
                iter.remove()
            }
        }
    }

}