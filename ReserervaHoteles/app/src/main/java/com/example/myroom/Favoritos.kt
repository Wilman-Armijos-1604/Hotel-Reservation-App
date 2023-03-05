package com.example.myroom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myroom.objetos.Variable
import com.example.myroom.recyclerview.Rcv_favorito_habitacion
import com.example.myroom.recyclerview.Rcv_favorito_hotel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class Favoritos : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    val storage = Firebase.storage.reference
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favoritos)

        val menuLateral = findViewById<NavigationView>(R.id.nv_menu_lateral)
        menuLateral.visibility = NavigationView.INVISIBLE
        auth = Firebase.auth

        val listaDeHotelesFavoritos = ArrayList<Variable>()
        val listaDeHabitacionFavoritos = ArrayList<Variable>()

        val recyclerViewHotelesFavoritos = findViewById<RecyclerView>(R.id.rv_hotelesFavoritos)
        val recyclerViewHabitacionesFavoritos =
            findViewById<RecyclerView>(R.id.rv_habitacionesFavoritas)

        val adapterHotelesFavoritos =
            Rcv_favorito_hotel(this, recyclerViewHotelesFavoritos, listaDeHotelesFavoritos)
        val adapterHaitacionesFavoritos = Rcv_favorito_habitacion(
            this,
            recyclerViewHabitacionesFavoritos,
            listaDeHabitacionFavoritos
        )

        db.collection("HotelFavoritos").whereEqualTo("idUsuario", "${auth.uid}").get()
            .addOnSuccessListener { querySnapFavHotel ->
                for (hotelFav in querySnapFavHotel) {
                    storage.child("Hoteles/${hotelFav.getString("idHotel").toString()}/1.jpg")
                        .getBytes(12024 * 1024 * 3)
                        .addOnSuccessListener { imagen ->

                            listaDeHotelesFavoritos.add(
                                Variable(
                                    "${hotelFav.id}",
                                    imagen,
                                    hotelFav.getString("nombreHotel"),
                                    hotelFav.getString("direccion"),
                                    hotelFav.getString("puntuacion"),
                                    "", ""


                                )

                            )
                            recyclerViewHotelesFavoritos.adapter=adapterHotelesFavoritos
                            recyclerViewHotelesFavoritos.itemAnimator=androidx.recyclerview.widget.DefaultItemAnimator()
                            recyclerViewHotelesFavoritos.layoutManager=androidx.recyclerview.widget.LinearLayoutManager(this)
                            adapterHotelesFavoritos.notifyDataSetChanged()

                        }
                }
            }

        db.collection("HabitacionFavorita").whereEqualTo("idUsuario", "${auth.uid}").get()
            .addOnSuccessListener { querySnapFavHab ->
                for (habFav in querySnapFavHab) {
                    storage.child("TiposHabitaciones/${habFav.getString("idHabitacion").toString()}/1.jpg")
                        .getBytes(12024 * 1024 * 3)
                        .addOnSuccessListener { imagen ->
                            Log.i("ayuda","mamahuevo")
                            listaDeHabitacionFavoritos.add(
                                Variable(
                                    "${habFav.id}",
                                    imagen,
                                    habFav.getString("nombreHotel"),
                                    habFav.getString("nombreHabitacion"),

                                    "${habFav.getString("numCamasPersonas")}",
                                    "$ ${habFav.getString("precioInicial")}",
                                    ""


                                )

                            )
                            recyclerViewHabitacionesFavoritos.adapter=adapterHaitacionesFavoritos
                            recyclerViewHabitacionesFavoritos.itemAnimator=androidx.recyclerview.widget.DefaultItemAnimator()
                            recyclerViewHabitacionesFavoritos.layoutManager=androidx.recyclerview.widget.LinearLayoutManager(this)
                            adapterHaitacionesFavoritos.notifyDataSetChanged()

                        }
                }
            }

        val botonFavHoteles=findViewById<TextView>(R.id.tv_btn_hotelesFavoritos)
        botonFavHoteles.setOnClickListener {
            if(recyclerViewHotelesFavoritos.visibility==RecyclerView.VISIBLE){
                recyclerViewHotelesFavoritos.visibility=RecyclerView.GONE

            }
            else{
                recyclerViewHotelesFavoritos.visibility=RecyclerView.VISIBLE
            }
        }

        val botonFavHabitacion=findViewById<TextView>(R.id.tv_btn_habitacionesFavoritos)
        botonFavHabitacion.setOnClickListener {
            if(recyclerViewHabitacionesFavoritos.visibility==RecyclerView.VISIBLE){
                recyclerViewHabitacionesFavoritos.visibility=RecyclerView.GONE

            }
            else{
                recyclerViewHabitacionesFavoritos.visibility=RecyclerView.VISIBLE
            }
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
            finish()
        }

        val botonPerfil = findViewById<TextView>(R.id.tv_btn_perfil)
        botonPerfil.setOnClickListener {
            menuLateral.visibility = NavigationView.INVISIBLE
            startActivity(Intent(this, Perfil::class.java))
            finish()
        }

        val botonFavoritos = findViewById<TextView>(R.id.tv_btn_favoritos)
        botonFavoritos.setOnClickListener {
            menuLateral.visibility = NavigationView.INVISIBLE

        }
        val botonReservar = findViewById<TextView>(R.id.tv_btn_reservar)
        botonReservar.setOnClickListener {
            menuLateral.visibility = NavigationView.INVISIBLE
            val intent = Intent(this, PreReserva::class.java)
            intent.putExtra("estado", "abierta")
            startActivity(intent)
            finish()
        }
        val botonMisReservas = findViewById<TextView>(R.id.tv_btn_mis_reservas)
        botonMisReservas.setOnClickListener {
            menuLateral.visibility = NavigationView.INVISIBLE
            startActivity(Intent(this, MisReservas::class.java))
            finish()
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


    }

}