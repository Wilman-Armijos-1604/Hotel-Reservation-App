package com.example.myroom.recyclerview

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myroom.R
import com.example.myroom.objetos.HotelHabitacion

class Rcv_Imagenes_Horizontales (
    private val context:Context,
    private val recyclerView: RecyclerView,
    private val list: List<ByteArray>
): RecyclerView.Adapter<Rcv_Imagenes_Horizontales.myViewHolder>() {
    inner class myViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fotoHotel: ImageView

        init {
            fotoHotel = view.findViewById<ImageView>(R.id.img_imagenesLista)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.rcv_imagenes, parent, false)
        return myViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val imagen = list[position]
        holder.fotoHotel.setImageBitmap(
            BitmapFactory.decodeByteArray(
               imagen,
                0,
                imagen!!.size
            )
        )
        //holder.fotoMiniHotel.scaleType=ImageView.ScaleType.CENTER_CROP

    }

    override fun getItemCount(): Int {
        return list.size
    }
}