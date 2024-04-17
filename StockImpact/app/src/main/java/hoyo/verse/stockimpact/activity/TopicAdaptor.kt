package hoyo.verse.stockimpact

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hoyo.verse.stockimpact.activity.DetailAdapter
import hoyo.verse.stockimpact.entity.Summaries
import okhttp3.internal.notify

class TopicAdapter(private val topics: List<Summaries>) : RecyclerView.Adapter<TopicAdapter.ViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.topic_header, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val topic = topics[position]
        holder.topicTitle.text = topic.topic
        holder.topicTitle.setOnClickListener {
            Log.e("!", "Toggle")
            topic.expanded = !topic.expanded
            notifyItemChanged(position)
        }
        holder.recyclerView.visibility = if (topic.expanded) View.VISIBLE else View.GONE
        val childLayoutManager = LinearLayoutManager(holder.recyclerView.context, RecyclerView.VERTICAL, false)
        holder.recyclerView.layoutManager = childLayoutManager
        val adapter = DetailAdapter(topic.items, topic.expanded)
        holder.recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
//        holder.recyclerView.setRecycledViewPool(viewPool)
    }

    override fun getItemCount(): Int{
        return topics.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val topicTitle: TextView = itemView.findViewById(R.id.topicTitle)
        val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
    }
}
