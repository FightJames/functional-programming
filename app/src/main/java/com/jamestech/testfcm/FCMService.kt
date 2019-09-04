package com.jamestech.testfcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class FCMService : FirebaseMessagingService() {
    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        Timber.d("Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
//        sendRegistrationToServer(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        Timber.d("Message data call")
        remoteMessage?.data?.isNotEmpty()?.let {
            Timber.d("Message data  ${remoteMessage?.data}")
        }
        remoteMessage?.notification?.let {
            Timber.d("Message body  ${it.body}")
        }
        super.onMessageReceived(remoteMessage)
    }
}