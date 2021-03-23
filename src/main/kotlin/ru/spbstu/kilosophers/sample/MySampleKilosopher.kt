package ru.spbstu.kilosophers.sample

import ru.spbstu.kilosophers.AbstractKilosopher
import ru.spbstu.kilosophers.Action
import ru.spbstu.kilosophers.ActionKind.*
import ru.spbstu.kilosophers.Fork
import ru.spbstu.kilosophers.sample.MySampleKilosopher.State.*
import kotlin.random.Random

class MySampleKilosopher(left: Fork, right: Fork, val index: Int, kilosophersCount: Int) : AbstractKilosopher(left, right) {

    internal enum class State {
        WAITS_BOTH,
        WAITS_LEFT,
        WAITS_RIGHT,
        EATS,
        HOLDS_BOTH,
        HOLDS_LEFT,
        HOLDS_RIGHT,
        THINKS
    }

    private var state = if (index % 2 != 0) WAITS_BOTH else THINKS
    private var startLeftFork = false
    private val countKilosophers = kilosophersCount

    override fun nextAction(): Action {
        return when (state) {
            WAITS_BOTH -> if (index % 2 == 0) {
                if (countKilosophers % 2 != 0) {
                    if (startLeftFork) {
                        startLeftFork = false
                        TAKE_LEFT(Random.nextInt(10, 50))
                    } else {
                        startLeftFork = true
                        TAKE_RIGHT(Random.nextInt(10, 50))
                    }
                } else TAKE_RIGHT(Random.nextInt(10, 50))
            } else TAKE_LEFT(Random.nextInt(10, 50))
            WAITS_RIGHT -> TAKE_RIGHT(Random.nextInt(10, 50))
            WAITS_LEFT -> TAKE_LEFT(Random.nextInt(10, 50))
            EATS -> EAT(Random.nextInt(51, 200))
            HOLDS_BOTH -> if (index % 2 == 0) DROP_RIGHT(Random.nextInt(10, 50)) else {
                if (countKilosophers % 2 != 0) {
                    if (startLeftFork) {
                        startLeftFork = false
                        DROP_RIGHT(Random.nextInt(10, 50))
                    } else {
                        startLeftFork = true
                        DROP_LEFT(Random.nextInt(10, 50))
                    }
                } else DROP_LEFT(Random.nextInt(10, 50))
            }
            HOLDS_RIGHT -> DROP_RIGHT(Random.nextInt(10, 50))
            HOLDS_LEFT -> DROP_LEFT(Random.nextInt(10, 50))
            THINKS -> THINK(Random.nextInt(51, 200))
        }
    }

    override fun handleResult(action: Action, result: Boolean) {
        state = when (action.kind) {
            TAKE_LEFT -> if (result) {if (holdsRight) EATS else WAITS_RIGHT} else WAITS_LEFT
            TAKE_RIGHT -> if (result) {if (holdsLeft) EATS else WAITS_LEFT} else WAITS_RIGHT
            EAT -> HOLDS_BOTH
            DROP_LEFT -> if (result) {if (!holdsRight) THINKS else HOLDS_RIGHT} else HOLDS_LEFT
            DROP_RIGHT -> if (result) {if (!holdsLeft) THINKS else HOLDS_LEFT} else HOLDS_RIGHT
            THINK -> WAITS_BOTH
        }
    }

    override fun toString(): String {
        return "Kilosopher #$index"
    }
}