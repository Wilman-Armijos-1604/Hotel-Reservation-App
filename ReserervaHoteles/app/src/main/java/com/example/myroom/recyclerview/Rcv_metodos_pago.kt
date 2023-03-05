package com.example.myroom.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myroom.R

class Rcv_metodos_pago (
    private val context: Context,
    private val recyclerView: RecyclerView,
    private val list: List<String>
): RecyclerView.Adapter<Rcv_metodos_pago.myViewHolder>() {
    inner class myViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val metodosPago: TextView

        init {
            metodosPago = view.findViewById<TextView>(R.id.txt_btn_seleccionMetodoPago)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.rcv_metodos_de_pago, parent, false)
        return myViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val metodo = list[position]
        holder.metodosPago.text=metodo

    }

    override fun getItemCount(): Int {
        return list.size
    }
}