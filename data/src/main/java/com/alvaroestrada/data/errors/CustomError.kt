package com.alvaroestrada.data.errors

sealed class CustomError {
    data object NetworkError : CustomError()
    data object DatabaseError : CustomError()
}