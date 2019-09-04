package com.jamestech.testfcm

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.regex.Pattern

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun Functor() {
        var functorResult = Maybe.Just(2).fmap { it + 3 }
        var functorResultNothing = Maybe.`Nothing#`.fmap { x: Int -> x + 3 }
        println(functorResult)
        println(functorResultNothing)

        var maybeWithFun = { x: String -> x + 3 } `($)` Maybe.Just("hello")
        println(maybeWithFun)

        var functionFunctor =
            { x: Int -> (x + 50).toDouble() }.fmap<Int, Double, String> { i: Double -> i.toString() }
        println(functionFunctor.invoke(10))
    }

    @Test
    fun iterableFunctor() {
        var iter = arrayListOf(1, 10, 19, 89)
        iter.map { it + 90 }.forEach { print("$it ") }
    }

    @Test
    fun Applicative() {
        var applicativeResult = Maybe.Just({ x: Int -> x + 100 }) `(*)` Maybe.Just(90)
        var applicativeNothingResult = Maybe.Just({ x: Int -> x + 100 }) `(*)` Maybe.`Nothing#`
        println(applicativeResult)
        println(applicativeNothingResult)

        var applicativeSingle = { y: Int -> { x: Int -> x + y } } `($)` Maybe.Just(5)
        var resultApplicativeSingle = applicativeSingle `(*)` Maybe.Just(7)
        println(resultApplicativeSingle)
    }

    @Test
    fun ApplicativeList() {
        var result = listOf<(Int) -> Int>({ it * 2 }, { it + 3 }) `(*)` listOf(1, 2, 3)
        result.forEach {
            print("$it ")
        }
    }

    @Test
    fun Monad() {
        var test = Maybe.Just({ input: Int -> input + 3 })
        var m = Maybe.Just(100) `))=` { input ->
            Maybe.Just<((Int) -> Int)>({ i: Int -> input + i })
        } `))=` {
            Maybe.Just(it.invoke(20))
        }
        print(m)
    }

    fun <T> getPostTitle(input: T) = input
}
