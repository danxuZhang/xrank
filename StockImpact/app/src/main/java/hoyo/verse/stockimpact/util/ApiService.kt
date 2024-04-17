package hoyo.verse.stockimpact.util

import hoyo.verse.stockimpact.entity.History
import hoyo.verse.stockimpact.entity.PostTopicBody
import hoyo.verse.stockimpact.entity.Topic
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    // For submit, hide real apis
    @GET("GetHistoryAPI")
    fun getHistoryOfTopic(@Query("topic") topic: Int): Call<List<History>>

    @GET("GetUserTopicAPI")
    fun getUserTopics(@Query("email") email: String): Call<List<Topic>>

    @POST("UpdateUserTopicAPI")
    fun submitData(@Body postTopicBody: PostTopicBody): Call<Void>
}
