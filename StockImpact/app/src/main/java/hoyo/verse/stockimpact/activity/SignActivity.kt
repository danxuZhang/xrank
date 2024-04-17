package hoyo.verse.stockimpact.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import hoyo.verse.stockimpact.R


class SignActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        auth = FirebaseAuth.getInstance()

        val emailEditText = findViewById<EditText>(R.id.email)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val signUpButton = findViewById<Button>(R.id.signUp)
        val signInButton = findViewById<Button>(R.id.signIn)

        signUpButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            if (checkInput(email, password))
                signUp(email, password)
        }

        signInButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            if (checkInput(email, password))
                signIn(email, password)
        }

//        FirebaseMessaging.getInstance().subscribeToTopic("BitCoin")
//            .addOnCompleteListener { task: Task<Void?> ->
//                if (!task.isSuccessful) {
//                    Log.w("FCM", "Subscription failed")
//                } else {
//                    Log.d("FCM", "Subscribed to topic")
//                }
//            }
    }

    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(baseContext, "Sign Up Successful.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(baseContext, "Sign Up Failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success
                Toast.makeText(baseContext, "Sign In Successful.", Toast.LENGTH_SHORT).show()
            } else {
                // If sign in fails
                Toast.makeText(baseContext, "Sign In Failed. Please check email or password.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkInput(email: String, password: String): Boolean{
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Input is not an email!", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.length < 6){
            Toast.makeText(this, "Password too short!", Toast.LENGTH_SHORT).show()
            return false;
        }
        return true;
    }

}
