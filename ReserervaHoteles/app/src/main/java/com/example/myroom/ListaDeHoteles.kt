package com.example.myroom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myroom.objetos.HotelHabitacion
import com.example.myroom.recyclerview.Rcv_lista_hoteles

import com.facebook.login.LoginManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class ListaDeHoteles : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    val db =Firebase.firestore
    val ref=Firebase.storage.reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_de_hoteles)

        val list= arrayListOf<HotelHabitacion>()

        val recyclerView=findViewById<RecyclerView>(R.id.rv_hotel)
        recyclerView.setOnClickListener{

        }

        val adapter=Rcv_lista_hoteles(this,recyclerView,list)
        db.collection("HotelHabitacion").get()
            .addOnSuccessListener {
                Log.i("firebase","consulta Lista HotelHabitacion")
            for(hotelHabitacion in it){
                val hotelHabitacionReference=ref.child("Hoteles/"+hotelHabitacion.id.toString()+"/1.jpg")
                hotelHabitacionReference.getBytes(1024*1024*3).addOnSuccessListener {imagen->
                    Log.i("storage","consulta imagen HotelHabitacion")
                    list.add(
                        HotelHabitacion(
                            hotelHabitacion.id.toString(),
                            hotelHabitacion.getString("nombreHotel"),
                            hotelHabitacion.getString("direccion"),
                            hotelHabitacion.getString("puntuacion"),
                            hotelHabitacion.getString("nombre"),
                            hotelHabitacion.getString("numeroDeCamas"),
                            hotelHabitacion.getString("numeroDePersonas"),
                            hotelHabitacion.getDouble("precio"),
                            hotelHabitacion.getString("formaPago"),
                            imagen
                            )
                    )

                    recyclerView.adapter=adapter
                    recyclerView.itemAnimator=androidx.recyclerview.widget.DefaultItemAnimator()
                    recyclerView.layoutManager=androidx.recyclerview.widget.LinearLayoutManager(this)
                    adapter.notifyDataSetChanged()

                }.addOnFailureListener {
                    Log.i("storage","Error ${it.message}")
                }
                //Log.i("firebase","for: ${list[list.size-1].imagen}")
            }

        }
            .addOnFailureListener {
                Log.i("firebase","Falla ${it.message.toString()}")
        }

        val menuLateral=findViewById<NavigationView>(R.id.nv_menu_lateral)
        menuLateral.visibility=NavigationView.INVISIBLE
        auth = Firebase.auth


        val botonHomeHeader=findViewById<TextView>(R.id.Title)
        botonHomeHeader.setOnClickListener {



        }
        val botonHomeMenu=findViewById<ImageView>(R.id.img_Logo)
        botonHomeMenu.setOnClickListener {
            menuLateral.visibility = NavigationView.INVISIBLE



        }

        val botonHomeSlogan=findViewById<TextView>(R.id.tv_slogan)
        botonHomeSlogan.setOnClickListener {

            menuLateral.visibility = NavigationView.INVISIBLE

        }

        val botonAbrirYcerrarMenu= findViewById<ImageView>(R.id.img_btn_menulateral)
        botonAbrirYcerrarMenu.setOnClickListener{
            if (menuLateral.visibility==NavigationView.INVISIBLE){
                menuLateral.visibility=NavigationView.VISIBLE
            }else{
                menuLateral.visibility=NavigationView.INVISIBLE
            }
        }

        val botonPerfilIcon=findViewById<ImageView>(R.id.tv_img_perfil)
        botonPerfilIcon.setOnClickListener{
            menuLateral.visibility=NavigationView.INVISIBLE
            startActivity(Intent(this,Perfil::class.java))
        }

        val botonPerfil = findViewById<TextView>(R.id.tv_btn_perfil)
        botonPerfil.setOnClickListener {
            menuLateral.visibility=NavigationView.INVISIBLE
            startActivity(Intent(this,Perfil::class.java))
        }

        val botonFavoritos= findViewById<TextView>(R.id.tv_btn_favoritos)
        botonFavoritos.setOnClickListener {
            menuLateral.visibility=NavigationView.INVISIBLE
            startActivity(Intent(this,Favoritos::class.java))
        }
        val botonReservar= findViewById<TextView>(R.id.tv_btn_reservar)
        botonReservar.setOnClickListener {
            menuLateral.visibility=NavigationView.INVISIBLE
            val intent =Intent(this,PreReserva::class.java)
            intent.putExtra("estado","abierta")
            startActivity(intent)
        }
        val botonMisReservas= findViewById<TextView>(R.id.tv_btn_mis_reservas)
        botonMisReservas.setOnClickListener {
            menuLateral.visibility=NavigationView.INVISIBLE
            startActivity(Intent(this,MisReservas::class.java))
        }
        val botonAyuda= findViewById<TextView>(R.id.tv_btn_ayuda)
        botonAyuda.setOnClickListener {

            //insertarHoteles()
           // insertarTipoHabitacion()
            //insertarHoteHabitacion()




            menuLateral.visibility=NavigationView.INVISIBLE
            Toast.makeText(
                baseContext, "NO ESTA DISPONIBLE EN LA VERSION ACTUAL.",
                Toast.LENGTH_SHORT
            ).show()
        }
        val botonConfiguracion= findViewById<TextView>(R.id.tv_btn_configuracion)
        botonConfiguracion.setOnClickListener {
            menuLateral.visibility=NavigationView.INVISIBLE
            Toast.makeText(
                baseContext, "NO ESTA DISPONIBLE EN LA VERSION ACTUAL.",
                Toast.LENGTH_SHORT
            ).show()
        }

        val botonCerrarSession= findViewById<TextView>(R.id.tv_btn_cerrar_session)
        botonCerrarSession.setOnClickListener {
            menuLateral.visibility=NavigationView.INVISIBLE
            auth.signOut()
            LoginManager.getInstance().logOut()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }





        /*val recyclerArchivos = findViewById<RecyclerView>(R.id.rcv_Archivos)

        recyclerArchivos.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recyclerArchivos.adapter = RecyclerViewAdapterArchivos(listaArchivos,this)*/


    }

    private fun insertarHoteHabitacion() {
        db.collection("HotelHabitacion").document("61NEZBUxKlGIJHTcK5x5").set(
             hashMapOf(
                "nombreHotel" to "Casa Mia",
                 "direccion" to "Cagliari, Italia",
                 "puntuacion" to "3.92/5.00",
                "nombre" to "Doppia Speciale",
                "numeroDeCamas" to "2 letti",
                "numeroDePersonas" to "2 adulti",
                "precio" to 60,
                "formaPago" to "Carta di Credito Accettata"
            )
        )
        db.collection("HotelHabitacion").document("sFnB6eJ0UDGMBm6AtOE5").set(
            hashMapOf(
                "nombreHotel" to "París Hilton Colón",
                "direccion" to "Quito, Ecuador",
                "puntuacion" to "4.67/5.00",
                "nombre" to "Comfort",
                "numeroDeCamas" to "1 cama",
                "numeroDePersonas" to "2 adultos",
                "precio" to 65,
                "formaPago" to "Aceptamos tarjeta de Crédito"
            )
        )
        db.collection("HotelHabitacion").document("yobzMiSpn1BWnW2r5RTv").set(
            hashMapOf(
                "nombreHotel" to "Frijolito",
                "direccion" to "Loja, Ecuador",
                "puntuacion" to "4.64/5.00",
                "nombre" to "Deluxe",
                "numeroDeCamas" to "1 cama",
                "numeroDePersonas" to "1 persona",
                "precio" to 170,
                "formaPago" to "Pago en el Hotel"
            )
        )
    }

    private fun insertarHoteles() {
        db.collection("Hotel").document("61NEZBUxKlGIJHTcK5x5").set(
            hashMapOf(
                "nombre" to "Casa Mia",
                "pais" to "ciudad",
                "ciudad" to "Cagliari",
                "calNumeroDeCalificaciones" to 113,
                "calSumaAcumulada" to 440.5,
                "puntuacion" to 3.9,
                "checkIn" to "17:00",
                "checkOut" to "08:00",
                "metodosDePago" to hashMapOf(
                    "pagoConTarjeta" to false,
                    "pagoConPayPal" to false,
                    "pagoEnHotel" to true
                ),
                "servicios" to arrayListOf(
                    "WIFI","TV","Ristorante","Terrazza","Biliardo","Campo da Tennis","Servizio in Camera","Parcheggio"
                )
            )
        )
    }

    private fun insertarTipoHabitacion() {
        db.collection("TipoHabitacion").add(hashMapOf(
            "idHotel" to "yobzMiSpn1BWnW2r5RTv",
            "nombre" to "Delux",
            "numCamas" to 1,
            "numHabitaciones" to 20,
            "numMaxAdultos" to 2,
            "numMaxNinios" to 0,
            "numMinAdultos" to 1,
            "precioAdulto" to 30.00,
            "precioNinio" to 0.00,
            "precioInicial" to 170,
            "servicios" to arrayListOf<String>(
                "TV","Jacuzzi","Salón","Mini Bar"
            )

        ))
            .addOnSuccessListener {
                Log.i("firestore", "tipo habitacion registrada 1")
            }.addOnFailureListener {
                Log.i("firestore","NO registrado tipo Habitacion 1")
            }
        db.collection("TipoHabitacion").add(hashMapOf(
            "idHotel" to "yobzMiSpn1BWnW2r5RTv",
            "nombre" to "Doble",
            "numCamas" to 2,
            "numHabitaciones" to 15,
            "numMaxAdultos" to 2,
            "numMaxNinios" to 2,
            "numMinAdultos" to 1,
            "precioAdulto" to 17.00,
            "precioNinio" to 10.00,
            "precioInicial" to 58,
            "servicios" to arrayListOf<String>(
                "TV","Desayuno","Balcón","Cocina"
            )
        ))
            .addOnSuccessListener {
                Log.i("firestore", "tipo habitacion registrada 2")
            }.addOnFailureListener {
                Log.i("firestore","NO registrado tipo Habitacion 2")
            }
        db.collection("TipoHabitacion").add(hashMapOf(
            "idHotel" to "yobzMiSpn1BWnW2r5RTv",
            "nombre" to "Single",
            "numCamas" to 1,
            "numHabitaciones" to 30,
            "numMaxAdultos" to 2,
            "numMaxNinios" to 2,
            "numMinAdultos" to 1,
            "precioAdulto" to 10.00,
            "precioNinio" to 7.00,
            "precioInicial" to 40,
            "servicios" to arrayListOf<String>(
                "TV","Aire Acondicionado","Balcón","Agua Caliente"
            )
        ))
            .addOnSuccessListener {
                Log.i("firestore", "tipo habitacion registrada 3")
            }.addOnFailureListener {
                Log.i("firestore","NO registrado tipo Habitacion 3")
            }
    }
}