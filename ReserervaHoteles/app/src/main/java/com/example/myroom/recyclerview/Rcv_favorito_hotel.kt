package com.example.myroom.recyclerview

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myroom.R
import com.example.myroom.objetos.Variable
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Rcv_favorito_hotel (
    private val context: Context,
    private val recyclerView: RecyclerView,
    private val list: ArrayList<Variable>
): RecyclerView.Adapter<Rcv_favorito_hotel.myViewHolder>(){
    inner class myViewHolder(view: View): RecyclerView.ViewHolder(view){
        val imagen: ImageView

        val nombreHotel: TextView
        val Direccion: TextView
        val calificacion: TextView
        //val entradaSalida: TextView
        val favorito: TextView
        val campo3:TextView



        init {
            imagen=view.findViewById(R.id.img_ImagenDepende)

            nombreHotel=view.findViewById(R.id.txt_titulo)
            Direccion=view.findViewById(R.id.tv_campo1)
            calificacion=view.findViewById(R.id.tv_campo2)
            //entradaSalida=view.findViewById(R.id.tv_campo3)
            favorito=view.findViewById(R.id.txt_campoDerecha)
            favorito.text="Quitar"
            campo3=view.findViewById(R.id.tv_campo3)
            campo3.text=""


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rcv_reserva_detalle,parent,false)
        return myViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val db = Firebase.firestore

        val Favorito = list[position]
        holder.imagen.setImageBitmap(BitmapFactory.decodeByteArray(Favorito.imagen,0,Favorito.imagen!!.size))
        holder.nombreHotel.text="${Favorito.titulo}"
        holder.Direccion.text="${Favorito.campo1}"
        holder.calificacion.text="${Favorito.campo2}/5 estrellas"


       // holder.favorito.text = "${Reserva.campoDerecha}"

        holder.favorito.setOnClickListener {
            db.collection("HotelFavoritos").document("${Favorito.id}").delete()
                .addOnSuccessListener {
                    list.removeAt(position)
                    notifyItemRemoved(position)
                    notifyDataSetChanged()
                    Toast.makeText(context,"Eliminado de Favoritos",Toast.LENGTH_SHORT).show()
                }
        }




    }



    override fun getItemCount(): Int {
        return list.size
    }
}