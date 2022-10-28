package mati.vamps.weapons

class SeparationTimer(val listener: Listener, var separation: Int, var total: Int) {

    interface Listener {
        fun onSeparationTimerUp(idx: Int)
    }

    private var running = false
    private var timer = 0
    private var currentCount = 0

    fun start() {
        running = true
        timer = 0
        currentCount = 0
    }

    fun update() {
        if(!running) return

        if(currentCount < total) timer++
        if(timer % separation == 0) {
            listener.onSeparationTimerUp(currentCount)
            currentCount++
            if(currentCount == total) running = false
        }
    }

}