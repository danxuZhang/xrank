package hoyo.verse.stockimpact.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import hoyo.verse.stockimpact.activity.InitTopicActivity
import hoyo.verse.stockimpact.activity.MainActivity
import hoyo.verse.stockimpact.activity.SignActivity
import hoyo.verse.stockimpact.entity.History
import hoyo.verse.stockimpact.entity.PostTopicBody
import hoyo.verse.stockimpact.entity.Topic
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ConcurrentHashMap

object TopicManager {
    val topics = mutableListOf<Int>()
    val user = FirebaseAuth.getInstance().currentUser
    val apiService = RetrofitClient.instance.create(ApiService::class.java)
    val history = ConcurrentHashMap<Int, List<History>>()
    var count = 0

    fun getTopicsFromDB(){
        apiService.getUserTopics(user?.email!!).enqueue(object : Callback<List<Topic>> {
            override fun onResponse(call: Call<List<Topic>>, response: Response<List<Topic>>) {
                if (response.isSuccessful) {
                    val fetchedTopics = response.body()
                    topics.clear()
                    Log.e("Success", fetchedTopics.toString())
                    if (fetchedTopics != null) {
                        for (topic in fetchedTopics){
                            topics.add(topic.Topic)
                        }
                    }
                } else {
                    Log.e("Fail", response.toString())
                }
            }
            override fun onFailure(call: Call<List<Topic>>, t: Throwable) {
                Log.e("HTTPRequest", t.toString())
            }
        })
    }

    fun getTopicsAndCheckIfInit(activity: Activity){
        Log.e("11", user?.email!!)
        count = 0
        apiService.getUserTopics(user?.email!!).enqueue(object : Callback<List<Topic>> {
            override fun onResponse(call: Call<List<Topic>>, response: Response<List<Topic>>) {
                if (response.isSuccessful) {
                    val fetchedTopics = response.body()
                    topics.clear()
                    Log.e("Success", fetchedTopics.toString())
                    if (fetchedTopics != null) {
                        for (topic in fetchedTopics){
                            topics.add(topic.Topic)
                        }
                    }
                    var intent: Intent? = null
                    if (topics.size == 0){
                        intent = Intent(activity, InitTopicActivity::class.java)
                        activity.startActivity(intent)
                        activity.finish()
                    }
                    else{
                        history.clear()
                        topics.map {
                            apiService.getHistoryOfTopic(it).enqueue(object: Callback<List<History>>{
                                override fun onResponse(call: Call<List<History>>, response: Response<List<History>>) {


                                    if (response.isSuccessful) {
                                        val fetchedSummary = response.body()
                                        Log.e("Success", fetchedSummary.toString())
                                        if (fetchedSummary != null) {
                                            history.put(it, fetchedSummary)
                                        }
                                        count += 1
                                        if (count == topics.size){
                                            intent = Intent(activity, MainActivity::class.java)
                                            activity.startActivity(intent)
                                            activity.finish()
                                        }
                                    }
                                    else{
                                        Log.e("Fail", response.toString())
                                    }
                                }

                                override fun onFailure(call: Call<List<History>>, t: Throwable) {
                                    Log.e("HTTPRequest", t.toString())
                                }
                            })
                        }

                    }

                } else {
                    Log.e("Fail", response.toString())
                }
            }

            override fun onFailure(call: Call<List<Topic>>, t: Throwable) {
                Log.e("HTTPRequest", t.toString())
            }
        })
    }

    fun updateUserTopics(topics: List<Int>){
        val body = PostTopicBody(user?.email!!, topics)
        apiService.submitData(body).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.e("Success", "Update Topic Success")
                } else {
                    // Handle other responses
                    Log.e("Fail", "Update Topic Fail: " + response.code())
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Error", "Update Topic Error: " + t.message)
            }
        })
    }


}