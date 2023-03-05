package com.example.myroom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myroom.objetos.ReservaHabitacion
import com.example.myroom.objetos.metodoDePago
import com.example.myroom.recyclerview.Rcv_prereserva
import com.example.myroom.recyclerview.Rcv_seleccion_metodos_pago
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class PreReserva : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        val db = Firebase.firestore

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_reserva)
        val menuLateral = findViewById<NavigationView>(R.id.nv_menu_lateral)
        menuLateral.visibility = NavigationView.INVISIBLE
        auth = Firebase.auth

        val estado = intent.getStringExtra("estado")
        val menuMetodoPago=findViewById<BottomNavigationView>(R.id.menu_metodo_de_pago)
        menuMetodoPago.visibility=BottomNavigationView.INVISIBLE
        val listaMetodosDePago = ArrayList<metodoDePago>()
        val recyclerViewMetodoDePago=findViewById<RecyclerView>(R.id.rv_prereserva_metodo_de_pago)

        val adapterMetodoDePago=Rcv_seleccion_metodos_pago(this,recyclerViewMetodoDePago,listaMetodosDePago)
        val botonSeleccionMetodoDePago=findViewById<TextView>(R.id.tv_btn_PagohacerReservar)
        if(estado!="abierta"){
            botonSeleccionMetodoDePago.visibility=TextView.INVISIBLE
        }
        botonSeleccionMetodoDePago.setOnClickListener {
            if(menuMetodoPago.visibility==BottomNavigationView.VISIBLE){
                menuMetodoPago.visibility=BottomNavigationView.INVISIBLE
                botonSeleccionMetodoDePago.text="Seleccionar Metodo de Pago"
            }
            else{
                menuMetodoPago.visibility=BottomNavigationView.VISIBLE
                botonSeleccionMetodoDePago.text="Regresar"
            }


        }

        val botonHomeHeader=findViewById<TextView>(R.id.Title)
        botonHomeHeader.setOnClickListener {
            startActivity(Intent(this,ListaDeHoteles::class.java))

            finish()
        }
        val botonHomeMenu=findViewById<ImageView>(R.id.img_Logo)
        botonHomeMenu.setOnClickListener {
            menuLateral.visibility = NavigationView.INVISIBLE
            startActivity(Intent(this,ListaDeHoteles::class.java))

            finish()
        }

        val botonHomeSlogan=findViewById<TextView>(R.id.tv_slogan)
        botonHomeSlogan.setOnClickListener {
            startActivity(Intent(this,ListaDeHoteles::class.java))
            menuLateral.visibility = NavigationView.INVISIBLE
            finish()
        }


        val listaPreReserva = ArrayList<ReservaHabitacion>()
        val reference = Firebase.storage.reference
        val recyclerViewPreReserva = findViewById<RecyclerView>(R.id.rv_listaPrereserva)
        val adapterPreReserva = Rcv_prereserva(this, recyclerViewPreReserva, listaPreReserva)
        if (estado=="abierta") {
            db.collection("ReservaCabecera").whereEqualTo("estado", "abierta").whereEqualTo("idUsuario","${auth.uid}").get()
                .addOnSuccessListener {
                    if (it.size() > 0) {
                        db.collection("ReservaHabitacion")
                            .whereEqualTo("idReservaCabecera", "${it.documents[0].id}").get()
                            .addOnSuccessListener { listaHabitaciones ->
                                var suma=0.0
                                for (habitacion in listaHabitaciones) {

                                    reference.child("TiposHabitaciones/${habitacion.data["idTipoDeHabitacion"]}/1.jpg")
                                        .getBytes(1024 * 1024 * 3)
                                        .addOnSuccessListener { imagen ->
                                            findViewById<TextView>(R.id.tv_preReserva_nombreHotel).text=it.documents[0].getString("nombreHotel")







                                            listaPreReserva.add(
                                                ReservaHabitacion(
                                                    habitacion.id,
                                                    imagen,
                                                    habitacion.getString("fechaEntrada"),
                                                    habitacion.getString("fechaSalida"),
                                                    habitacion.getString("idReservaCabecera"),
                                                    habitacion.getString("idTipoDeHabitacion"),
                                                    habitacion.getString("idUsuario"),
                                                    habitacion.getString("nombreTipoDeHabitacion"),
                                                    habitacion.getDouble("numeroDeAdultos")!!
                                                        .toInt(),
                                                    habitacion.getDouble("numeroDeCamas")!!.toInt(),
                                                    habitacion.getDouble("numeroDeCuartos")!!
                                                        .toInt(),
                                                    habitacion.getDouble("numeroDeDias")!!.toInt(),
                                                    habitacion.getDouble("numeroDeNin")!!.toInt(),
                                                    habitacion.getDouble("subtotal"),


                                                    )
                                            )
                                            recyclerViewPreReserva.adapter = adapterPreReserva
                                            recyclerViewPreReserva.itemAnimator =
                                                androidx.recyclerview.widget.DefaultItemAnimator()
                                            recyclerViewPreReserva.layoutManager =
                                                androidx.recyclerview.widget.LinearLayoutManager(
                                                    this
                                                )
                                            adapterPreReserva.notifyDataSetChanged()

                                        }
                                    suma= (suma + habitacion.getDouble("subtotal")!!)
                                }
                                db.collection("Hotel").document("${it.documents[0].getString("idHotel")}").get()
                                    .addOnSuccessListener { hotel->
                                        var hashMetodos: HashMap<String, Any> =
                                            hotel.data!!.get("metodosDePago") as HashMap<String, Any>

                                        if (hashMetodos["pagoConTarjeta"] == true) {
                                            listaMetodosDePago.add(metodoDePago("Pago Con Tarjeta","${it.documents[0].id}"))
                                        }
                                        if (hashMetodos["pagoConPayPal"] == true) {
                                            listaMetodosDePago.add(metodoDePago("Pago con PayPal","${it.documents[0].id}"))
                                        }
                                        if (hashMetodos["pagoEnHotel"] == true) {
                                            listaMetodosDePago.add(metodoDePago("Pago en el Hotel","${it.documents[0].id}"))
                                        }

                                       // Log.i("cabecera","${idCabecera}")
                                        recyclerViewMetodoDePago.adapter=adapterMetodoDePago
                                        recyclerViewMetodoDePago.itemAnimator=androidx.recyclerview.widget.DefaultItemAnimator()
                                        recyclerViewMetodoDePago.layoutManager=androidx.recyclerview.widget.LinearLayoutManager(this)
                                        adapterMetodoDePago.notifyDataSetChanged()
                                    }
                                findViewById<TextView>(R.id.txv_precioTotalPrereserva).text="$ ${suma}"
                            }
                    } else {
                        Toast.makeText(
                            this,
                            "Aun no ha registrado habitaciones",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
        }else{

            db.collection("ReservaCabecera").document("${estado}").get()
                .addOnSuccessListener {
                    if (it!= null) {
                        db.collection("ReservaHabitacion")
                            .whereEqualTo("idReservaCabecera", "${it.id}").get()
                            .addOnSuccessListener { listaHabitaciones ->
                                var suma=0.0
                                for (habitacion in listaHabitaciones) {

                                    reference.child("TiposHabitaciones/${habitacion.data["idTipoDeHabitacion"]}/1.jpg")
                                        .getBytes(1024 * 1024 * 3)
                                        .addOnSuccessListener { imagen ->
                                            findViewById<TextView>(R.id.tv_preReserva_nombreHotel).text=""
                                            listaPreReserva.add(
                                                ReservaHabitacion(
                                                    habitacion.id,
                                                    imagen,
                                                    habitacion.getString("fechaEntrada"),
                                                    habitacion.getString("fechaSalida"),
                                                    habitacion.getString("idReservaCabecera"),
                                                    habitacion.getString("idTipoDeHabitacion"),
                                                    habitacion.getString("idUsuario"),
                                                    habitacion.getString("nombreTipoDeHabitacion"),
                                                    habitacion.getDouble("numeroDeAdultos")!!
                                                        .toInt(),
                                                    habitacion.getDouble("numeroDeCamas")!!.toInt(),
                                                    habitacion.getDouble("numeroDeCuartos")!!
                                                        .toInt(),
                                                    -99,
                                                    habitacion.getDouble("numeroDeNin")!!.toInt(),
                                                    habitacion.getDouble("subtotal"),


                                                    )
                                            )
                                            recyclerViewPreReserva.adapter = adapterPreReserva
                                            recyclerViewPreReserva.itemAnimator =
                                                androidx.recyclerview.widget.DefaultItemAnimator()
                                            recyclerViewPreReserva.layoutManager =
                                                androidx.recyclerview.widget.LinearLayoutManager(
                                                    this
                                                )
                                            adapterPreReserva.notifyDataSetChanged()
                                        }
                                    suma= (suma + habitacion.getDouble("subtotal")!!)
                                }
                                findViewById<TextView>(R.id.txv_precioTotalPrereserva).text="$ ${suma}"
                            }
                    } else {
                        Toast.makeText(
                            this,
                            "Aun no ha registrado habitaciones",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
        }

        recyclerViewPreReserva.setOnClickListener{
            var suma=0.0
            listaPreReserva.forEach {
                suma=suma+ it.subtotal!!
            }
            findViewById<TextView>(R.id.txv_precioTotalPrereserva).text="$ ${suma}"
        }
        val botonAbrirYcerrarMenu = findViewById<ImageView>(R.id.img_btn_menulateral)
        botonAbrirYcerrarMenu.setOnClickListener {
            if (menuLateral.visibility == NavigationView.INVISIBLE) {
                menuLateral.visibility = NavigationView.VISIBLE
            } else {
                menuLateral.visibility = NavigationView.INVISIBLE
            }
        }

        val botonPerfilIcon = findViewById<ImageView>(R.id.tv_img_perfil)
        botonPerfilIcon.setOnClickListener {
            menuLateral.visibility = NavigationView.INVISIBLE
            startActivity(Intent(this, Perfil::class.java))
        }

        val botonPerfil = findViewById<TextView>(R.id.tv_btn_perfil)
        botonPerfil.setOnClickListener {
            menuLateral.visibility = NavigationView.INVISIBLE
            startActivity(Intent(this, Perfil::class.java))
        }

        val botonFavoritos = findViewById<TextView>(R.id.tv_btn_favoritos)
        botonFavoritos.setOnClickListener {
            menuLateral.visibility = NavigationView.INVISIBLE
            startActivity(Intent(this, Favoritos::class.java))
        }
        val botonReservar = findViewById<TextView>(R.id.tv_btn_reservar)
        botonReservar.setOnClickListener {
            menuLateral.visibility = NavigationView.INVISIBLE
            val intent =Intent(this,PreReserva::class.java)
            intent.putExtra("estado","abierta")
            startActivity(intent)
        }
        val botonMisReservas = findViewById<TextView>(R.id.tv_btn_mis_reservas)
        botonMisReservas.setOnClickListener {
            menuLateral.visibility = NavigationView.INVISIBLE
            startActivity(Intent(this, MisReservas::class.java))
        }
        val botonAyuda = findViewById<TextView>(R.id.tv_btn_ayuda)
        botonAyuda.setOnClickListener {
            menuLateral.visibility = NavigationView.INVISIBLE
            Toast.makeText(
                baseContext, "NO ESTA DISPONIBLE EN LA VERSION ACTUAL.",
                Toast.LENGTH_SHORT
            ).show()
        }
        val botonConfiguracion = findViewById<TextView>(R.id.tv_btn_configuracion)
        botonConfiguracion.setOnClickListener {
            menuLateral.visibility = NavigationView.INVISIBLE
            Toast.makeText(
                baseContext, "NO ESTA DISPONIBLE EN LA VERSION ACTUAL.",
                Toast.LENGTH_SHORT
            ).show()
        }

        val botonCerrarSession = findViewById<TextView>(R.id.tv_btn_cerrar_session)
        botonCerrarSession.setOnClickListener {
            menuLateral.visibility = NavigationView.INVISIBLE
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}