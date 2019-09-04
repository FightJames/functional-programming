package com.jamestech.testfcm

import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import org.junit.Ignore
import org.junit.Test
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.lang.annotation.Documented
import java.lang.annotation.ElementType
import java.lang.annotation.RetentionPolicy
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class LearnRx {

    fun scheduler() {
        var scheduler = Schedulers.io()
        println(
            scheduler.now(TimeUnit.DAYS)
        )

        println(
            scheduler.now(TimeUnit.MILLISECONDS)
        )
        var worker = scheduler.createWorker()
        println(worker.now(TimeUnit.DAYS))
        var worker2 = scheduler.createWorker()
        worker.schedule {
            println(Thread.currentThread().name)
        }
        worker2.schedule {
            println(Thread.currentThread().name)
        }

    }


    fun testPar() {
        var datas = Observable.just("hello", "james", "love", "android")
        datas.flatMap {
            Observable.create<String> { emitter ->
                emitter.onNext(it)
            }.subscribeOn(Schedulers.io())
        }.observeOn(Schedulers.trampoline())
            .doOnNext {
                println(Thread.currentThread().name + "  $it")
            }.subscribe()

        datas = Observable.just("hello", "", "love", "android")
        datas.map {
            if (it.isNullOrEmpty()) {
                throw  Exception("Data is String")
            } else {
                return@map it
            }
        }.subscribe({
            println("success message ${it}")
        }, {
            println("error message ${it.message}")
        })
    }


    fun testBlock() {
        var observer = object : Observer<String> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: String) {
                println(t)
            }

            override fun onError(e: Throwable) {
            }
        }
        var list = arrayListOf<String>("hello", "James", "passion", "android", "Kotlin Spring")

        var datas = Observable.create<String> {
            it.onNext("hello")
            it.onNext("James")
            it.onNext("Passion")
            Thread.sleep(2000)
            it.onNext("android")
            it.onNext("Kotlin Spring")
        }.share()

        datas.filter { it.length > 5 }
            .subscribe(observer)
        println("=============")
        datas.doOnNext {
            println("do on next " + it)
        }.subscribe()
    }


    fun testCoordiate() {
        var intS = Single.create<Int> {
            it.onSuccess(12345)
        }
        var k = Single.create<String> {
            it.onSuccess("hello")
        }.flatMap {
            println(it)
            intS
        }
        k.doOnSuccess {
            println("it $it")
        }.subscribe()
    }


    fun testCompleteableFuture() {
        val cf = CompletableFuture.supplyAsync<String> {
            Thread.sleep(5000)
            return@supplyAsync "JAMES IS HERE"
        }

        println(cf.getNow("hello"))
        println(cf.complete("James"))
        println(cf.get())
    }


    fun testBuffer() {
        var names = Observable.just("James", "Green", "Rinaly", "Combail", "Voice")
        var absoluteDelays = Observable.just(
            0.1, 0.6, 0.9, 1.1,
            3.3, 3.4, 3.5, 3.6, 4.4, 4.8
        ).map {
            it * 1000
        }
        var test = Observable.zip(
            names,
            absoluteDelays,
            object : BiFunction<String, Double, Observable<String>> {
                override fun apply(t: String, u: Double): Observable<String> {
                    return Observable.just(t).delay(u.toLong(), TimeUnit.MILLISECONDS)
                }
            })
            .flatMap { it }
        test.buffer(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.trampoline())
            .observeOn(Schedulers.trampoline())
            .subscribe({
                System.out.println(it)
            })
    }

    fun testBackPress() {
        var obs = Flowable.create(object : FlowableOnSubscribe<String> {
            override fun subscribe(emitter: FlowableEmitter<String>) {
                for (i in 0..100)
                    emitter.onNext("James +$i")
            }
        }, BackpressureStrategy.BUFFER)

        obs.subscribe(object : Subscriber<String> {
            lateinit var sb: Subscription
            override fun onComplete() {
            }

            override fun onSubscribe(s: Subscription) {
                sb = s
                s.request(3)
            }

            override fun onNext(t: String) {
                println(t)
//                sb.request(10)
            }

            override fun onError(t: Throwable) {
            }
        })
    }


    fun testTimeOutRetry() {
        var testTimeOut = Observable.create<String> {
            it.onNext("Hello")
            Thread.sleep(1100)
            it.onNext("I'm")
            it.onNext("James.")
        }
        testTimeOut
            .observeOn(Schedulers.trampoline())
            .subscribeOn(Schedulers.io())
            .timeout(1, TimeUnit.SECONDS)
            .retry()
            .doOnNext {
                println(it)
            }
    }


    fun unitTestRx() {
        var testSch = TestScheduler()
//        testSch.advanceTimeBy(10, TimeUnit.MILLISECONDS)
        Observable.create<String> {
            it.onNext("he")
            it.onNext("she")
            it.onNext("it")
        }
            .test()
            .assertOf { it.assertValueAt(0, "he") }
    }

    @Test
    fun testDataClass() {
//        var findJobVacanciesData = FindJobVacanciesData(mId = "12")
//        var old = findJobVacanciesData.copy()
//        findJobVacanciesData.mId = "1456"
//        println(old)
//        println(findJobVacanciesData)
        var tes = "test"
        var t = "te"
        println(tes.startsWith(t))
        println(tes.substring(0, tes.length - 1))

        var inside = arrayListOf("s", "s1", "s2")

        var list = arrayListOf(inside, inside, inside)
//        list.flatMap { it }.forEach {
//            println(it)
//        }
        var k = ""
        var g = "sljf"
        var h = HashSet<Int>()
        when {
            k.isNullOrEmpty() -> {
                println("when pass")
            }
            k.isNullOrEmpty().not() -> {
                println("when pass not")
            }
        }

    }

}

