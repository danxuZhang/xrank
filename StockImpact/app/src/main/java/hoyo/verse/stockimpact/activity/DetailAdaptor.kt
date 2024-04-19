package hoyo.verse.stockimpact.activity

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import hoyo.verse.stockimpact.R
import hoyo.verse.stockimpact.entity.History
import hoyo.verse.stockimpact.entity.Topic

class DetailAdapter(private val items: List<History>, private val isVisible: Boolean) : RecyclerView.Adapter<DetailAdapter.ItemViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail, parent, false)
        context = parent.context
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.timeText.text = item.Time.toString()
        holder.summaryText.text = item.Summary
        holder.itemView.setOnClickListener{
            showDialog(item.Summary)
        }
    }

    override fun getItemCount(): Int{
        return items.size
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeText: TextView = itemView.findViewById(R.id.timeText)
        val summaryText: TextView = itemView.findViewById(R.id.summaryText)
    }

    private fun showDialog(summary: String) {
        AlertDialog.Builder(context)
            .setTitle("Summary")
            .setMessage(summary)
            .setPositiveButton("Close") { dialog, which -> dialog.dismiss() }
            .show()
    }
}
