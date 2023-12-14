package com.alvaroestrada.data.extensions

import com.alvaroestrada.data.errors.Either

fun <L, R> Either<L, R>.ifRight(block: (R) -> Unit): Either<L, R> {
    if (this is Either.Right) block(value)
    return this
}

fun <L, R> Either<L, R>.ifLeft(block: (L) -> Unit): Either<L, R> {
    if (this is Either.Left) block(value)
    return this
}