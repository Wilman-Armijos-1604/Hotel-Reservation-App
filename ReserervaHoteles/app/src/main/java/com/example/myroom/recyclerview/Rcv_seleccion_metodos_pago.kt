package com.example.myroom.recyclerview

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myroom.MisReservas
import com.example.myroom.PagoConPaypal
import com.example.myroom.PagoConTarjeta
import com.example.myroom.R
import com.example.myroom.objetos.metodoDePago
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Rcv_seleccion_metodos_pago(
    private val context: Context,
    private val recyclerView: RecyclerView,
    private val list: List<metodoDePago>


) : RecyclerView.Adapter<Rcv_seleccion_metodos_pago.myViewHolder>() {
    inner class myViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val metodosPago: TextView
        val idCabecera: String

        init {

            metodosPago = view.findViewById<TextView>(R.id.txt_btn_seleccionMetodoPago)
            idCabecera = ""
            Log.i("cabecera", "${idCabecera}")


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.rcv_metodos_de_pago, parent, false)
        return myViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {

        val metodo = list[position]


        val db = Firebase.firestore
        db.collection("ReservaHabitacion").whereEqualTo("idReservaCabecera", metodo.idCabecera)
            .get()
            .addOnSuccessListener { querySnapListaReservaHabitaciones ->
                var suma = 0.0
                for (reservaHab in querySnapListaReservaHabitaciones) {
                    suma = suma + reservaHab.getDouble("subtotal")!!
                }

                db.collection("ReservaCabecera").document("${metodo.idCabecera.toString()}")
                    .update(
                        "total",suma
                    )
            }



        holder.metodosPago.text = metodo.metodoDepago
        holder.metodosPago.setOnClickListener {
            if (holder.metodosPago.text == "Pago Con Tarjeta") {
                val intent = Intent(context, PagoConTarjeta::class.java)
                intent.putExtra("idCabecera", "${metodo.idCabecera}")
                context.startActivity(intent)
            } else if (holder.metodosPago.text == "Pago con PayPal") {
                val intent = Intent(context, PagoConPaypal::class.java)
                intent.putExtra("idCabecera", "${metodo.idCabecera}")
                context.startActivity(intent)
            } else if (holder.metodosPago.text == "Pago en el Hotel") {
                val db = Firebase.firestore
                db.collection("ReservaCabecera").document("${metodo.idCabecera}")
                    .update(
                        "estado", "vigente",
                        "metodoDePago", "pagoEnHotel"
                    ).addOnSuccessListener {
                        context.startActivity(Intent(context, MisReservas::class.java))
                    }


            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}