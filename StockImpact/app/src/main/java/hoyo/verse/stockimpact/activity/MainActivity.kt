package hoyo.verse.stockimpact.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import hoyo.verse.stockimpact.R
import hoyo.verse.stockimpact.TopicAdapter
import hoyo.verse.stockimpact.entity.History
import hoyo.verse.stockimpact.entity.MyDividerDecoration
import hoyo.verse.stockimpact.entity.Summaries
import hoyo.verse.stockimpact.util.Constraints
import hoyo.verse.stockimpact.util.DialogUtil
import hoyo.verse.stockimpact.util.TopicManager
import java.sql.Date


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TopicAdapter
    private lateinit var manager: LinearLayoutManager
    private lateinit var userIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        userIcon = findViewById(R.id.userIcon)
        adapter = TopicAdapter(getTopics())
        manager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = manager
        val dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider)!!
        val dividerDecoration = MyDividerDecoration(dividerDrawable)
        recyclerView.addItemDecoration(dividerDecoration)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        userIcon.setOnClickListener{
            DialogUtil.showDialog(this)
        }
    }

    private fun getTopics(): List<Summaries> {
        val res = mutableListOf<Summaries>()
        for (topic in TopicManager.topics){
            res.add(Summaries(Constraints.convertNumToTopics(topic), TopicManager.history.get(topic)!!))
        }
        return res
    }
}
