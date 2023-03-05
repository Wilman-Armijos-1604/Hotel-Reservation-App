package com.example.myroom.recyclerview

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myroom.Hotel
import com.example.myroom.ListaDeHoteles
import com.example.myroom.R
import com.example.myroom.objetos.HotelHabitacion

class Rcv_lista_hoteles(
    private val context:Context,
    private val recyclerView: RecyclerView,
    private val list: ArrayList<HotelHabitacion>
        ):RecyclerView.Adapter<Rcv_lista_hoteles.myViewHolder>(){
            inner class myViewHolder(view: View):RecyclerView.ViewHolder(view){
                val fotoMiniHotel: ImageView
                val nombreHotel:TextView
                val direccion:TextView
                val puntuacion:TextView
                val nombreHabitacion:TextView
                val numeroCamas:TextView
                val numeroAdultos: TextView
                val precio:TextView
                val tipoPago:TextView
                val lhotelHabitacion:LinearLayout
                var id:String

                init {
                    fotoMiniHotel= view.findViewById<ImageView>(R.id.img_hotelLista)

                     nombreHotel= view.findViewById<TextView>(R.id.txt_nombre_hotelLista)
                     direccion= view.findViewById<TextView>(R.id.txt_direccionHotelLista)
                     puntuacion= view.findViewById<TextView>(R.id.txt_puntuacionHotelLista)
                     nombreHabitacion= view.findViewById<TextView>(R.id.txt_habitacionLista)
                     numeroCamas= view.findViewById<TextView>(R.id.txt_numeroCamasLista)
                     numeroAdultos= view.findViewById<TextView>(R.id.txt_adultosNinosHotelLista)
                     precio= view.findViewById<TextView>(R.id.txt_precioHotelLista)
                     tipoPago= view.findViewById<TextView>(R.id.txt_tipoPagoLista)
                    lhotelHabitacion=view.findViewById<LinearLayout>(R.id.ll_hotelHabitacion)
                    id=""

                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rcv_hoteles,parent,false)
        return myViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {

        val hotelHabitacion = list[position]
        holder.fotoMiniHotel.setImageBitmap(BitmapFactory.decodeByteArray(hotelHabitacion.imagen,0,hotelHabitacion.imagen!!.size))
        //holder.fotoMiniHotel.scaleType=ImageView.ScaleType.CENTER_CROP
        holder.nombreHotel.text=hotelHabitacion.nombreHotel
        holder.direccion.text=hotelHabitacion.direccion
        holder.puntuacion.text=hotelHabitacion.puntuacion
        holder.nombreHabitacion.text=hotelHabitacion.nombreHabitacion
        holder.numeroCamas.text=hotelHabitacion.numeroCamas
        holder.numeroAdultos.text=hotelHabitacion.numeroAdultos
        holder.precio.text="$ "+String.format("%.2f",hotelHabitacion.precio)
        holder.tipoPago.text=hotelHabitacion.tipoPago
        holder.id= hotelHabitacion.id.toString()
        holder.lhotelHabitacion.setOnClickListener{
            Log.i("recyclerVierw","Seselecciono el item ${holder.id}")
            val intent=Intent(context,Hotel::class.java)
            intent.putExtra("id","${holder.id}")

            context.startActivity(intent)

        }
    }



    override fun getItemCount(): Int {
        return list.size
    }
}