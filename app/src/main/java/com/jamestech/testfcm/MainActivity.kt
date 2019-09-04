package com.jamestech.testfcm

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful.not()) {
                    Timber.d("getFCMInstance fail ${task.exception}")
                    return@OnCompleteListener
                }
                var token = task.result?.token
                Timber.d("token $token")
            })
    }
}
