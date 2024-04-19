package hoyo.verse.stockimpact.util

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMMessageHandler : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.e("111", remoteMessage.toString())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("222", token)
    }
}
