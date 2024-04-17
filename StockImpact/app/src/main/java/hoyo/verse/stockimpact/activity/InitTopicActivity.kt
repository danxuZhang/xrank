package hoyo.verse.stockimpact.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import hoyo.verse.stockimpact.R
import hoyo.verse.stockimpact.util.Constraints
import hoyo.verse.stockimpact.util.DialogUtil
import hoyo.verse.stockimpact.util.TopicManager

class InitTopicActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_auth)
        auth = FirebaseAuth.getInstance()
        DialogUtil.showDialog(this)
    }

}