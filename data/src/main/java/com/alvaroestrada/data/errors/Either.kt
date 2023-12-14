package com.alvaroestrada.data.errors

sealed class Either<out L, out R> {
    data class Left<out L>(val value: L) : Either<L, Nothing>()
    data class Right<out R>(val value: R) : Either<Nothing, R>()

    fun <T> fold(fnL: (L) -> T, fnR: (R) -> T): T =
        when (this) {
            is Left -> fnL(value)
            is Right -> fnR(value)
        }
}