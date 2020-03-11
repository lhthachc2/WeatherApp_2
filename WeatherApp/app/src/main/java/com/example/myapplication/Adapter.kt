package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Parcelable
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Database.CityModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item_recycleview.view.*
import java.io.IOException
import java.io.Serializable


class Adapter constructor(val messages: ArrayList<CityModel>) : RecyclerView.Adapter<Adapter.MessageViewHolder>()  {

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id = itemView.findViewById<TextView>(R.id.textView2)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val Context : Context = parent.context
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_recycleview, parent, false)
        val MessageViewHolder = MessageViewHolder(itemView)
        if(MessageViewHolder.itemView.linearitem!=null)
        {
            MessageViewHolder.itemView.setOnClickListener {
                var Model : CityModel = messages.get(MessageViewHolder.adapterPosition)
                val Intent = Intent(Context,Main2Activity::class.java)
                Intent.putExtra("weather",Model)
                Context.startActivity(Intent)
            }
        }
        return MessageViewHolder
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val current = messages[position]
        holder.id.text = current.City
    }
    override fun getItemCount() = messages.count()

}

private fun Intent.putExtra(s: String, model: Model) {

}
