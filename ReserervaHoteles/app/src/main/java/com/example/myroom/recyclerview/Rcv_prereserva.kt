package com.example.myroom.recyclerview

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

import androidx.recyclerview.widget.RecyclerView
import com.example.myroom.Hotel
import com.example.myroom.PreReserva

import com.example.myroom.R
import com.example.myroom.TipoDeHabitacion

import com.example.myroom.objetos.ReservaHabitacion
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Rcv_prereserva (
    private val context: Context,
    private val recyclerView: RecyclerView,
    private val list: ArrayList<ReservaHabitacion>
): RecyclerView.Adapter<Rcv_prereserva.myViewHolder>(){
    inner class myViewHolder(view: View): RecyclerView.ViewHolder(view){
        val imagen:ImageView
        val nombreTipo:TextView
        val numHabitacionCamas:TextView
        val numAdultosNinos:TextView
        val entradaSalida:TextView
        val precio:TextView

        val editar:ImageView
        val eliminar:ImageView

        init {
            imagen=view.findViewById(R.id.img_habitacionPrereserva)
            nombreTipo=view.findViewById(R.id.txt_tipoHabitacionPrereserva)
            numHabitacionCamas=view.findViewById(R.id.txt_numHabitacionCamaPrereserva)
            numAdultosNinos=view.findViewById(R.id.txt_numAdultosNinosPrereserva)
            entradaSalida=view.findViewById(R.id.txt_fechaEntrSalPrereserva)
            precio=view.findViewById(R.id.txt_precioPrereserva)

            editar=view.findViewById(R.id.img_btn_editarHabitacion)
            eliminar=view.findViewById(R.id.img_btn_eliminarHabitacion)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rcv_prereserva,parent,false)
        return myViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {

        val db= Firebase.firestore
        val preReserva = list[position]
        holder.imagen.setImageBitmap(BitmapFactory.decodeByteArray(preReserva.imagen,0,preReserva.imagen!!.size))
        holder.nombreTipo.text="${preReserva.nombreHabitacion}"
        holder.numHabitacionCamas.text="${preReserva.numeroDeCuartos} habitacion(es) | ${preReserva.numeroDeCamas} cama(s)"
        holder.numAdultosNinos.text=   "${preReserva.numeroDeAdultos} adulto(s)      | ${preReserva.numeroDeNinos} niño(s)"
        holder.entradaSalida.text="${preReserva.fechaEntrada} -- ${preReserva.fechaSalida}"
        holder.precio.text = "$ ${preReserva.subtotal}"

        if(preReserva.numeroDeDias==-99){
            holder.editar.visibility=TextView.INVISIBLE
            holder.eliminar.visibility=TextView.INVISIBLE
        }

        holder.eliminar.setOnClickListener {
            db.collection("ReservaHabitacion").document("${preReserva.id}").delete()
                .addOnSuccessListener {
                    list.removeAt(position)
                    notifyItemRemoved(position)
                    notifyDataSetChanged()
                    Log.i("firestore","Reserva Habitacion eliminado")
                }.addOnFailureListener {
                  Toast.makeText(context,"Error eliminando",Toast.LENGTH_SHORT).show()
                }
            val intent=Intent(context,PreReserva::class.java)
            intent.putExtra("estado","abierta")
            context.startActivity(intent)
        }
        holder.editar.setOnClickListener {
            val intent = Intent(context,TipoDeHabitacion::class.java)
            intent.putExtra("nombreHotel","")
            intent.putExtra("idHotel","")
            intent.putExtra("idPrereserva","${preReserva.id}")

            context.startActivity(intent)

        }


    }



    override fun getItemCount(): Int {
        return list.size
    }
}