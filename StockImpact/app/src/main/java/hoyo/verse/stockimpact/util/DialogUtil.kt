package hoyo.verse.stockimpact.util

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DialogUtil {
    private val auth = FirebaseAuth.getInstance()
    private val items = arrayOf(Constraints.convertNumToTopics(1), Constraints.convertNumToTopics(2), Constraints.convertNumToTopics(3), Constraints.convertNumToTopics(4), Constraints.convertNumToTopics(5))
    private var checkedItems = booleanArrayOf(false, false, false, false, false)

    fun showDialog(context: Context) {
        val selectedItems = ArrayList<Int>()
        val builder = AlertDialog.Builder(context)
        checkedItems = booleanArrayOf(false, false, false, false, false)
        for (topic in TopicManager.topics){
            checkedItems[topic-1] = true
        }
        builder.setTitle("Please select your interested topics")
        builder.setMultiChoiceItems(items, checkedItems) { dialog, which, isChecked ->
            if (isChecked) {
                selectedItems.add(which)
            } else if (selectedItems.contains(which)) {
                selectedItems.remove(Integer.valueOf(which))
            }
        }
        builder.setPositiveButton("Done") { dialog, id ->
            val topicList = mutableListOf<Int>()
            selectedItems.map {
                topicList.add(it+1)
            }
            TopicManager.updateUserTopics(topicList)
        }
        val dialog = builder.create()
        dialog.show()
    }
}