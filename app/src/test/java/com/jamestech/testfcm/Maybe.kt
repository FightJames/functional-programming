package com.jamestech.testfcm

sealed class Maybe<out T> {
    object `Nothing#` : Maybe<Nothing>() {
        override fun toString(): String = "Nothing#"
    }

    data class Just<out T>(val value: T) : Maybe<T>()
}

fun <T, R> Maybe<T>.fmap(action: ((T) -> (R))): Maybe<R> = when (this) {
    is Maybe.`Nothing#` -> Maybe.`Nothing#`
    is Maybe.Just -> Maybe.Just(action(this.value))
}

fun <T, U, R> ((T) -> (U)).fmap(action: ((U) -> (R))) = { t: T -> action(this(t)) }

infix fun <T, R> Maybe<T>.`))=`(f: ((T) -> Maybe<R>)): Maybe<R> = when (this) {
    Maybe.`Nothing#` -> Maybe.`Nothing#`
    is Maybe.Just -> f(this.value)
}

infix fun <T, R> ((T) -> R).`($)`(maybe: Maybe<T>) = maybe.fmap(this)

infix fun <T, R> Maybe<(T) -> R>.`(*)`(maybe: Maybe<T>): Maybe<R> = when (this) {
    is Maybe.`Nothing#` -> Maybe.`Nothing#`
    is Maybe.Just -> this.value `($)` maybe
}

infix fun <T, R> Iterable<(T) -> R>.`(*)`(iterable: Iterable<T>) = this.flatMap { iterable.map(it) }

