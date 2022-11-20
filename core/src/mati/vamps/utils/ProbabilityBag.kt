package mati.vamps.utils
import com.badlogic.gdx.utils.Array as GdxArray
class ProbabilityBag<T> {

    private data class Elem<T>(val obj: T, val num: Int)

    private val array = GdxArray<Elem<T>>()

    private var total = 0

    fun add(elem: T, num: Int) {
        array.add(Elem(elem, num))
        total += num
    }

    fun get() : T {
        val r = Utils.r.nextInt(total)
        var accum = 0
        for(elem in array) {
            accum += elem.num
            if(r < accum) return elem.obj
        }
        return array.last().obj
    }

}