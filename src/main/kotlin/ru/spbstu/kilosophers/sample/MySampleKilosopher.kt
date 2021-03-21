package ru.spbstu.kilosophers.sample

import ru.spbstu.kilosophers.AbstractKilosopher
import ru.spbstu.kilosophers.Action
import ru.spbstu.kilosophers.ActionKind.*
import ru.spbstu.kilosophers.Fork
import ru.spbstu.kilosophers.sample.MySampleKilosopher.State.*

class MySampleKilosopher(left: Fork, right: Fork, val index: Int) : AbstractKilosopher(left, right) {

    internal enum class State {
        WAITS_BOTH,
        WAITS_LEFT,
        WAITS_RIGHT,
        EATS,
        HOLDS_BOTH,
        HOLDS_RIGHT,
        THINKS
    }

    private var state = if (index % 2 != 0) WAITS_BOTH else THINK
    private var lastKilosopher = false

    override fun nextAction(): Action {
        return when (state) {
            WAITS_BOTH -> if (lastKilosopher) {
                lastKilosopher = false
                TAKE_LEFT(10)
            } else {
                lastKilosopher = true
                TAKE_RIGHT(10)
            }
            WAITS_RIGHT -> TAKE_RIGHT(10)
            WAITS_LEFT -> TAKE_LEFT(10)
            EATS -> EAT(50)
            HOLDS_BOTH -> DROP_LEFT(10)
            HOLDS_RIGHT -> DROP_RIGHT(10)
            THINKS -> THINK(100)
            else -> THINK(100) //never
        }
    }

    override fun handleResult(action: Action, result: Boolean) {
        state = when (action.kind) {
            TAKE_LEFT -> if (result) if (holdsRight) EATS else WAITS_RIGHT else WAITS_LEFT
            TAKE_RIGHT -> if (result) if (holdsLeft) EATS else WAITS_LEFT else WAITS_RIGHT
            EAT -> HOLDS_BOTH
            DROP_LEFT -> if (result) HOLDS_RIGHT else HOLDS_BOTH
            DROP_RIGHT -> if (result) THINKS else HOLDS_RIGHT
            THINK -> WAITS_BOTH
        }
    }

    override fun toString(): String {
        return "Kilosopher #$index"
    }
}