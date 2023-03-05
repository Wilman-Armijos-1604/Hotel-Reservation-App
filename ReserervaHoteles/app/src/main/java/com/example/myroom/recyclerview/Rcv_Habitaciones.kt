package com.example.myroom.recyclerview

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myroom.Hotel
import com.example.myroom.PreReserva
import com.example.myroom.R
import com.example.myroom.TipoDeHabitacion
import com.example.myroom.objetos.Habitaciones
import com.example.myroom.objetos.HotelHabitacion
import com.example.myroom.objetos.ReservaHabitacion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.Format
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Rcv_Habitaciones(
    private val context: Context,
    private val recyclerView: RecyclerView,
    private val list: ArrayList<Habitaciones>,
    private val nombreHotel: String,
    private val idPreReserva: String
) : RecyclerView.Adapter<Rcv_Habitaciones.myViewHolder>() {
    inner class myViewHolder(view: View) : RecyclerView.ViewHolder(view),
        DatePickerDialog.OnDateSetListener {
        var fotoHabitacion: ImageView
        val nombre: TextView
        val numeroCamas: TextView
        val servicio1: TextView
        val servicio2: TextView
        val servicio3: TextView
        val numeroHabitacionesSeleccionadas: TextView
        val numeroDeAdultosSeleccionado: TextView
        val numeroDeNinSeleccionado: TextView
        val fechaEntrada: TextView
        val fechaSalida: TextView
        val precioCalculado: TextView
        val botonAdd: TextView

        // aumentar y disminuir
        val btnMenosHabs: TextView
        val btnMasHabs: TextView

        val btnMenosAdul: TextView
        val btnMasAdul: TextView

        val btnMenosNin: TextView
        val btnMasNin: TextView


        var Eyear: Int
        var Emes: Int
        var Edia: Int

        var EselectedYear: Int
        var EselectedMes: Int
        var EselectedDia: Int

        var Syear: Int
        var Smes: Int
        var Sdia: Int

        var SselectedYear: Int
        var SselectedMes: Int
        var SselectedDia: Int
        var selectorFecha: Boolean
        val cal = Calendar.getInstance()
        val cal2 = Calendar.getInstance()

        var costMin: Double
        var costoAddultos: Double
        var costoNinos: Double
        var numeroMinAdultos: Int

        var diferenciaFechas: Int


        //val Imagenes =ArrayList<ByteArray>()
        //val adapter: Rcv_Imagenes_Horizontales
        var total: Double


        var favorito:ImageView

        init {
            favorito=view.findViewById(R.id.img_btn_favoritos)
            diferenciaFechas = 1
            nombre = view.findViewById(R.id.txv_tipoHabitacionLista)
            fotoHabitacion = view.findViewById(R.id.img_tipo_habitacion)
            numeroCamas = view.findViewById(R.id.txt_primerServicioLista)
            servicio1 = view.findViewById(R.id.txt_segundoServicioLista)
            servicio2 = view.findViewById(R.id.txt_tercerServicioLista)
            servicio3 = view.findViewById(R.id.txt_cuartoServicioLista)
            numeroHabitacionesSeleccionadas = view.findViewById(R.id.txt_numHabitaciones)
            numeroDeAdultosSeleccionado = view.findViewById(R.id.txt_numAdultos)
            numeroDeNinSeleccionado = view.findViewById(R.id.txt_numNinos)
            fechaEntrada = view.findViewById(R.id.txt_fechaEntrada)
            fechaSalida = view.findViewById(R.id.txt_fechaSalida)
            precioCalculado = view.findViewById(R.id.txt_PrecioHabitacion)
            botonAdd = view.findViewById(R.id.txt_btn_AgregarHabitacion)
            if (idPreReserva != "no") {
                botonAdd.text = "Guardar Cambios"
            }
            btnMenosHabs = view.findViewById(R.id.txt_btn_menosNumHabitaciones)
            btnMasHabs = view.findViewById(R.id.txt_btn_masNumHabitaciones)

            btnMenosAdul = view.findViewById(R.id.txt_btn_menosNumAdultos)
            btnMasAdul = view.findViewById(R.id.txt_btn_masNumAdultos)

            btnMenosNin = view.findViewById(R.id.txt_btn_menosNumNinos)
            btnMasNin = view.findViewById(R.id.txt_btn_masNumNinos)
            //adapter= Rcv_Imagenes_Horizontales(context,fotosHabitaciones,Imagenes)

            costMin = 0.0
            costoAddultos = 0.0
            costoNinos = 0.0
            numeroMinAdultos = 0

            selectorFecha = true
            //val cal = Calendar.getInstance()
            Edia = cal.get(Calendar.DAY_OF_MONTH)
            Emes = cal.get(Calendar.MONTH)
            Eyear = cal.get(Calendar.YEAR)
            EselectedYear = Eyear
            EselectedMes = Emes
            EselectedDia = Edia

            //val cal2=Calendar.getInstance()
            cal2.add(Calendar.DATE, 1)
            Sdia = cal2.get(Calendar.DAY_OF_MONTH)
            Smes = cal2.get(Calendar.MONTH)
            Syear = cal2.get(Calendar.YEAR)
            SselectedYear = Syear
            SselectedMes = Smes
            SselectedDia = Sdia
            total = 0.0
            fechaEntrada.setOnClickListener {
                selectorFecha = true

                DatePickerDialog(context, this, Eyear, Emes, Edia).show()


            }
            fechaSalida.setOnClickListener {
                selectorFecha = false

                DatePickerDialog(context, this, Syear, Smes, Sdia).show()

            }

        }

        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

            if (selectorFecha) {
                EselectedYear = year
                EselectedMes = month
                EselectedDia = dayOfMonth
                fechaEntrada.text = "${EselectedDia}-${EselectedMes+1}-${EselectedYear}"
                cal.set(EselectedYear, EselectedMes, EselectedDia)


            } else {
                SselectedYear = year
                SselectedMes = month
                SselectedDia = dayOfMonth
                fechaSalida.text = "${SselectedDia}-${SselectedMes+1}-${SselectedYear}"
                cal2.set(SselectedYear, SselectedMes, SselectedDia)

            }
            val costoAdulto = (numeroDeAdultosSeleccionado.text.toString()
                .toInt() - numeroMinAdultos!!) * costoAddultos!!
            val costoNin = (numeroDeNinSeleccionado.text.toString().toInt() * costoNinos!!)
            val diferenciaFechas =
                ((cal2.timeInMillis - cal.timeInMillis) / (1000 * 3600 * 24)).toInt()
            total =
                ((costoAdulto + costoNin + costMin!!) * diferenciaFechas) * numeroHabitacionesSeleccionadas.text.toString()
                    .toInt()
            precioCalculado.text = "$: ${total}"


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.rcv_lista_habitaciones, parent, false)
        return myViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: myViewHolder, position: Int) {

        val habitacion = list[position]

        holder.costMin = habitacion.precioInicial!!
        holder.costoAddultos = habitacion.precioAdul!!
        holder.costoNinos = habitacion.precioNin!!
        holder.numeroMinAdultos = habitacion.numeroMinAdultos!!




        holder.nombre.text = habitacion.nombre
        holder.numeroCamas.text = habitacion.numeroCamas.toString() + " cama(s)"
        holder.servicio1.text = habitacion.servicios?.get(0)
        holder.servicio2.text = habitacion.servicios?.get(1)
        holder.servicio3.text = habitacion.servicios?.get(2)
        holder.numeroHabitacionesSeleccionadas.text = "1"
        holder.numeroDeAdultosSeleccionado.text = habitacion.numeroMinAdultos.toString()
        holder.numeroDeNinSeleccionado.text = "0"
        //var formato = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        holder.fechaEntrada.text =
            "${holder.EselectedDia}-${holder.EselectedMes+1}-${holder.EselectedYear}"
        holder.fechaSalida.text =
            "${holder.SselectedDia}-${holder.SselectedMes+1}-${holder.SselectedYear}"
        //LocalDate.parse( holder.fechaEntrada.text,formato)
        // LocalDate.parse(holder.fechaSalida.text,formato).plusDays(1)
        val costoAdulto = (holder.numeroDeAdultosSeleccionado.text.toString()
            .toInt() - habitacion.numeroMinAdultos!!) * habitacion.precioAdul!!
        val costoNin =
            (holder.numeroDeNinSeleccionado.text.toString().toInt() * habitacion.precioNin!!)
        holder.diferenciaFechas =
            ((holder.cal2.timeInMillis - holder.cal.timeInMillis) / (1000 * 3600 * 24)).toInt()
        holder.total =
            ((costoAdulto + costoNin + habitacion.precioInicial!!) * holder.diferenciaFechas) * holder.numeroHabitacionesSeleccionadas.text.toString()
                .toInt()
        holder.precioCalculado.text = "$: ${holder.total}"
        //habitaciones
        holder.btnMenosHabs.setOnClickListener {
            if (holder.numeroHabitacionesSeleccionadas.text.toString().toInt() <= 1) {
                Toast.makeText(
                    context,
                    "Numero Minimo de Habitaciones Permitido",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                holder.numeroHabitacionesSeleccionadas.text =
                    (holder.numeroHabitacionesSeleccionadas.text.toString().toInt() - 1).toString()
                val costoAdulto = (holder.numeroDeAdultosSeleccionado.text.toString()
                    .toInt() - habitacion.numeroMinAdultos!!) * habitacion.precioAdul!!
                val costoNin = (holder.numeroDeNinSeleccionado.text.toString()
                    .toInt() * habitacion.precioNin!!)
                holder.diferenciaFechas =
                    ((holder.cal2.timeInMillis - holder.cal.timeInMillis) / (1000 * 3600 * 24)).toInt()
                holder.total =
                    ((costoAdulto + costoNin + habitacion.precioInicial!!) * holder.diferenciaFechas) * holder.numeroHabitacionesSeleccionadas.text.toString()
                        .toInt()
                holder.precioCalculado.text = "$: ${holder.total}"
            }
        }
        holder.btnMasHabs.setOnClickListener {
            if (holder.numeroHabitacionesSeleccionadas.text.toString()
                    .toInt() >= habitacion.numeroHabitaciones!!
            ) {
                Toast.makeText(
                    context,
                    "Numero Maximo de Habitaciones Disponibles",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                holder.numeroHabitacionesSeleccionadas.text =
                    (holder.numeroHabitacionesSeleccionadas.text.toString().toInt() + 1).toString()
                val costoAdulto = (holder.numeroDeAdultosSeleccionado.text.toString()
                    .toInt() - habitacion.numeroMinAdultos!!) * habitacion.precioAdul!!
                val costoNin = (holder.numeroDeNinSeleccionado.text.toString()
                    .toInt() * habitacion.precioNin!!)
                holder.diferenciaFechas =
                    ((holder.cal2.timeInMillis - holder.cal.timeInMillis) / (1000 * 3600 * 24)).toInt()
                holder.total =
                    ((costoAdulto + costoNin + habitacion.precioInicial!!) * holder.diferenciaFechas) * holder.numeroHabitacionesSeleccionadas.text.toString()
                        .toInt()
                holder.precioCalculado.text = "$: ${holder.total}"
            }
        }
        //Adultos
        holder.btnMenosAdul.setOnClickListener {
            if (holder.numeroDeAdultosSeleccionado.text.toString()
                    .toInt() <= habitacion.numeroMinAdultos!!
            ) {
                Toast.makeText(context, "Numero Minimo de Adultos Permitido", Toast.LENGTH_SHORT)
                    .show()
            } else {
                holder.numeroDeAdultosSeleccionado.text =
                    (holder.numeroDeAdultosSeleccionado.text.toString().toInt() - 1).toString()
                val costoAdulto = (holder.numeroDeAdultosSeleccionado.text.toString()
                    .toInt() - habitacion.numeroMinAdultos!!) * habitacion.precioAdul!!
                val costoNin = (holder.numeroDeNinSeleccionado.text.toString()
                    .toInt() * habitacion.precioNin!!)
                holder.diferenciaFechas =
                    ((holder.cal2.timeInMillis - holder.cal.timeInMillis) / (1000 * 3600 * 24)).toInt()
                holder.total =
                    ((costoAdulto + costoNin + habitacion.precioInicial!!) * holder.diferenciaFechas) * holder.numeroHabitacionesSeleccionadas.text.toString()
                        .toInt()
                holder.precioCalculado.text = "$: ${holder.total}"
            }
        }
        holder.btnMasAdul.setOnClickListener {
            if (holder.numeroDeAdultosSeleccionado.text.toString()
                    .toInt() >= habitacion.numeroMaxAdultos!!
            ) {
                Toast.makeText(context, "Numero Maximo de Adultos Permitido", Toast.LENGTH_SHORT)
                    .show()
            } else {
                holder.numeroDeAdultosSeleccionado.text =
                    (holder.numeroDeAdultosSeleccionado.text.toString().toInt() + 1).toString()
                val costoAdulto = (holder.numeroDeAdultosSeleccionado.text.toString()
                    .toInt() - habitacion.numeroMinAdultos!!) * habitacion.precioAdul!!
                val costoNin = (holder.numeroDeNinSeleccionado.text.toString()
                    .toInt() * habitacion.precioNin!!)
                holder.diferenciaFechas =
                    ((holder.cal2.timeInMillis - holder.cal.timeInMillis) / (1000 * 3600 * 24)).toInt()
                holder.total =
                    ((costoAdulto + costoNin + habitacion.precioInicial!!) * holder.diferenciaFechas) * holder.numeroHabitacionesSeleccionadas.text.toString()
                        .toInt()
                holder.precioCalculado.text = "$: ${holder.total}"
            }
        }
        //Niños
        holder.btnMenosNin.setOnClickListener {
            if (holder.numeroDeNinSeleccionado.text.toString().toInt() <= 0) {
                Toast.makeText(context, "Numero Minimo seleccionado", Toast.LENGTH_SHORT).show()
            } else {
                holder.numeroDeNinSeleccionado.text =
                    (holder.numeroDeNinSeleccionado.text.toString().toInt() - 1).toString()
                val costoAdulto = (holder.numeroDeAdultosSeleccionado.text.toString()
                    .toInt() - habitacion.numeroMinAdultos!!) * habitacion.precioAdul!!
                val costoNin = (holder.numeroDeNinSeleccionado.text.toString()
                    .toInt() * habitacion.precioNin!!)
                holder.diferenciaFechas =
                    ((holder.cal2.timeInMillis - holder.cal.timeInMillis) / (1000 * 3600 * 24)).toInt()
                holder.total =
                    ((costoAdulto + costoNin + habitacion.precioInicial!!) * holder.diferenciaFechas) * holder.numeroHabitacionesSeleccionadas.text.toString()
                        .toInt()
                holder.precioCalculado.text = "$: ${holder.total}"
            }
        }
        holder.btnMasNin.setOnClickListener {
            if (holder.numeroDeNinSeleccionado.text.toString()
                    .toInt() >= habitacion.numeroMaxNin!!
            ) {
                Toast.makeText(
                    context,
                    "Numero Maximo de Niños para la habitación",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                holder.numeroDeNinSeleccionado.text =
                    (holder.numeroDeNinSeleccionado.text.toString().toInt() + 1).toString()
                val costoAdulto = (holder.numeroDeAdultosSeleccionado.text.toString()
                    .toInt() - habitacion.numeroMinAdultos!!) * habitacion.precioAdul!!
                val costoNin = (holder.numeroDeNinSeleccionado.text.toString()
                    .toInt() * habitacion.precioNin!!)
                holder.diferenciaFechas =
                    ((holder.cal2.timeInMillis - holder.cal.timeInMillis) / (1000 * 3600 * 24)).toInt()
                holder.total =
                    ((costoAdulto + costoNin + habitacion.precioInicial!!) * holder.diferenciaFechas) * holder.numeroHabitacionesSeleccionadas.text.toString()
                        .toInt()
                holder.precioCalculado.text = "$: ${holder.total}"
            }
        }


        val reference = Firebase.storage.reference
        reference.child("TiposHabitaciones/" + habitacion.id + "/1.jpg")
            .getBytes(1024 * 1024 * 3)
            .addOnSuccessListener {
                Log.i("storage", "Consulta de img Habitacion")
                holder.fotoHabitacion.setImageBitmap(
                    BitmapFactory.decodeByteArray(
                        it,
                        0,
                        it.size
                    )
                )


            }
        val auth = FirebaseAuth.getInstance()
        holder.botonAdd.setOnClickListener {
            if (idPreReserva == "no") {
                val db = Firebase.firestore
                db.collection("ReservaCabecera")
                    .whereEqualTo("idUsuario", auth.uid.toString())
                    .whereEqualTo("idHotel", "${habitacion.idHotel}")
                    .whereEqualTo("estado", "abierta").get()
                    .addOnSuccessListener { Cabecera ->

                        if (Cabecera.size() != 0) {
                            Log.i("firestore", "Se entontro un documento abierto")
                            db.collection("ReservaHabitacion")
                                .add(
                                    hashMapOf(
                                        "idUsuario" to auth.uid.toString(),
                                        "idTipoDeHabitacion" to "${habitacion.id}",
                                        "nombreTipoDeHabitacion" to "${holder.nombre.text.toString()}",
                                        "numeroDeCamas" to habitacion.numeroCamas.toString()
                                            .toInt(),
                                        "numeroDeAdultos" to holder.numeroDeAdultosSeleccionado.text.toString()
                                            .toInt(),
                                        "numeroDeNin" to holder.numeroDeNinSeleccionado.text.toString()
                                            .toInt(),
                                        "numeroDeCuartos" to holder.numeroHabitacionesSeleccionadas.text.toString()
                                            .toInt(),
                                        "idReservaCabecera" to "${Cabecera.documents[0].id}",
                                        "fechaEntrada" to "${holder.fechaEntrada.text.toString()}",
                                        "fechaSalida" to "${holder.fechaSalida.text}",
                                        "numeroDeDias" to holder.diferenciaFechas,
                                        "subtotal" to holder.total
                                    )
                                ).addOnSuccessListener {
                                    Log.i("firestore", "Se creo una prereserva de una habitacion")
                                    Toast.makeText(
                                        context,
                                        "Habitacion Registrada",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }

                        } else {
                            db.collection("ReservaCabecera")
                                .add(
                                    hashMapOf(
                                        "idHotel" to "${habitacion.idHotel}",
                                        "nombreHotel" to "${nombreHotel.toString()}",
                                        "idUsuario" to "${auth.uid}",
                                        "total" to holder.total,
                                        "fechaEntradaSalida" to "${holder.fechaEntrada.text} - ${holder.fechaSalida.text}",
                                        "estado" to "abierta",
                                        "metodoDePago" to ""
                                    )
                                ).addOnSuccessListener {
                                    Log.i("firestore", "Reserva Creada")
                                    db.collection("ReservaHabitacion")
                                        .add(
                                            hashMapOf(
                                                "idUsuario" to auth.uid.toString(),
                                                "idTipoDeHabitacion" to "${habitacion.id}",
                                                "nombreTipoDeHabitacion" to "${holder.nombre.text.toString()}",
                                                "numeroDeCamas" to habitacion.numeroCamas.toString()
                                                    .toInt(),
                                                "numeroDeAdultos" to holder.numeroDeAdultosSeleccionado.text.toString()
                                                    .toInt(),
                                                "numeroDeNin" to holder.numeroDeNinSeleccionado.text.toString()
                                                    .toInt(),
                                                "numeroDeCuartos" to holder.numeroHabitacionesSeleccionadas.text.toString()
                                                    .toInt(),
                                                "idReservaCabecera" to "${it.id}",
                                                "fechaEntrada" to "${holder.fechaEntrada.text.toString()}",
                                                "fechaSalida" to "${holder.fechaSalida.text}",
                                                "numeroDeDias" to holder.diferenciaFechas,
                                                "subtotal" to holder.total
                                            )
                                        ).addOnSuccessListener {
                                            Log.i("firestore", "PreReservaRegistrada")
                                            Toast.makeText(
                                                context,
                                                "Habitacion Registrada",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                        }
                                }
                        }
                    }
            } else {
                val db = Firebase.firestore
                db.collection("ReservaHabitacion").document("${idPreReserva}")
                    .update(





                            "numeroDeAdultos", holder.numeroDeAdultosSeleccionado.text.toString()
                                .toInt(),
                            "numeroDeNin" , holder.numeroDeNinSeleccionado.text.toString()
                                .toInt(),
                            "numeroDeCuartos" , holder.numeroHabitacionesSeleccionadas.text.toString()
                                .toInt(),

                            "fechaEntrada" ,"${holder.fechaEntrada.text.toString()}",
                            "fechaSalida" ,"${holder.fechaSalida.text}",
                            "numeroDeDias", holder.diferenciaFechas,
                            "subtotal" , holder.total

                    ).addOnSuccessListener {
                        Log.i("firestore", "PreReserva Editada")
                        Toast.makeText(
                            context,
                            "PrereservaEditada",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

            }

        }

        holder.favorito.setOnClickListener {
            val db =Firebase.firestore
                db.collection("HabitacionFavorita").whereEqualTo("idHabitacion","${habitacion.id}").whereEqualTo("idUsuario","${auth.uid}").get()
                    .addOnSuccessListener {
                        if(it.isEmpty){

                                    db.collection("HabitacionFavorita").add(
                                        hashMapOf(
                                            "idHabitacion" to "${habitacion.id}",
                                            "nombreHotel" to "${nombreHotel}",
                                            "nombreHabitacion" to "${habitacion.nombre}",
                                            "precioInicial" to "${habitacion.precioInicial}",
                                            "numCamasPersonas" to "${habitacion.numeroCamas} cama(s), ${habitacion.numeroMinAdultos} persona(s)",
                                            "idUsuario" to "${auth.uid}"

                                        )
                                    ).addOnSuccessListener {
                                        Toast.makeText(context,"Agregado a Favoritos",Toast.LENGTH_SHORT).show()
                                    }






                        }
                        else{
                            db.collection("HabitacionFavorita").document("${it.documents[0].id}").delete()
                                .addOnSuccessListener {
                                    Toast.makeText(context,"Eliminado de Favoritos",Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
            }

    }




    override fun getItemCount(): Int {
        return list.size
    }


}