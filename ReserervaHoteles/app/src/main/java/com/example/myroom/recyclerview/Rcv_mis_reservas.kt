package com.example.myroom.recyclerview

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myroom.PreReserva
import com.example.myroom.R
import com.example.myroom.objetos.Variable

class Rcv_mis_reservas (
    private val context: Context,
    private val recyclerView: RecyclerView,
    private val list: ArrayList<Variable>
): RecyclerView.Adapter<Rcv_mis_reservas.myViewHolder>(){
    inner class myViewHolder(view: View): RecyclerView.ViewHolder(view){
        val imagen: ImageView

        val nombreHotel: TextView
        val Direccion: TextView
        val calificacion: TextView
        val entradaSalida: TextView
        val precio: TextView
        val layReserva:LinearLayout


        init {
            imagen=view.findViewById(R.id.img_ImagenDepende)

            nombreHotel=view.findViewById(R.id.txt_titulo)
            Direccion=view.findViewById(R.id.tv_campo1)
            calificacion=view.findViewById(R.id.tv_campo2)
            entradaSalida=view.findViewById(R.id.tv_campo3)
            precio=view.findViewById(R.id.txt_campoDerecha)
            layReserva=view.findViewById(R.id.ly_Reserva)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rcv_reserva_detalle,parent,false)
        return myViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {


        val Reserva = list[position]
        holder.imagen.setImageBitmap(BitmapFactory.decodeByteArray(Reserva.imagen,0,Reserva.imagen!!.size))
        holder.nombreHotel.text="${Reserva.titulo}"
        holder.calificacion.text="${Reserva.campo2}"
        holder.entradaSalida.text="${Reserva.campo3}"
        holder.Direccion.text="${Reserva.campo1}"
        holder.precio.text = "${Reserva.campoDerecha}"

        holder.layReserva.setOnClickListener {
            val intent = Intent(context,PreReserva::class.java)
            intent.putExtra("estado","${Reserva.id}")
            context.startActivity(intent)
        }




    }



    override fun getItemCount(): Int {
        return list.size
    }
}