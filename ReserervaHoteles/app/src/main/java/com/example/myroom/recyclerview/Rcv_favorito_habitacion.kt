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

class Rcv_favorito_habitacion (
    private val context: Context,
    private val recyclerView: RecyclerView,
    private val list: ArrayList<Variable>
): RecyclerView.Adapter<Rcv_favorito_habitacion.myViewHolder>(){
    inner class myViewHolder(view: View): RecyclerView.ViewHolder(view){
        val imagen: ImageView

        val nombreHotel: TextView
        val nombreHabitacion: TextView
        val numCamasPersonas: TextView
        //val entradaSalida: TextView
        val precioInial:TextView
        val favorito: TextView




        init {
            imagen=view.findViewById(R.id.img_ImagenDepende)

            nombreHotel=view.findViewById(R.id.txt_titulo)
            nombreHabitacion=view.findViewById(R.id.tv_campo1)
            numCamasPersonas=view.findViewById(R.id.tv_campo2)
            //entradaSalida=view.findViewById(R.id.tv_campo3)
            favorito=view.findViewById(R.id.txt_campoDerecha)
            favorito.text="Quitar"
            precioInial=view.findViewById(R.id.tv_campo3)



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


        holder.nombreHabitacion.text="${Favorito.campo1}"
        holder.numCamasPersonas.text="${Favorito.campo2}"
        holder.precioInial.text="${Favorito.campo3}"
        // holder.favorito.text = "${Reserva.campoDerecha}"

        holder.favorito.setOnClickListener {
            db.collection("HabitacionFavorita").document("${Favorito.id}").delete()
                .addOnSuccessListener {
                    list.removeAt(position)
                    notifyItemRemoved(position)
                    notifyDataSetChanged()
                    Toast.makeText(context,"Eliminado de Favoritos", Toast.LENGTH_SHORT).show()
                }
        }




    }



    override fun getItemCount(): Int {
        return list.size
    }
}