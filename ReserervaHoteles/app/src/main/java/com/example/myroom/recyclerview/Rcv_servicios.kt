package com.example.myroom.recyclerview

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myroom.R

class Rcv_servicios (
    private val context: Context,
    private val recyclerView: RecyclerView,
    private val list: List<String>
): RecyclerView.Adapter<Rcv_servicios.myViewHolder>() {
    inner class myViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val servicio: TextView

        init {
            servicio = view.findViewById<TextView>(R.id.txt_Servicio)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.rcv_servicios_habitacion, parent, false)
        return myViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val serv = list[position]
        holder.servicio.text=serv

    }

    override fun getItemCount(): Int {
        return list.size
    }
}