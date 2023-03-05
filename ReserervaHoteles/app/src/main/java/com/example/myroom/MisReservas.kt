package com.example.myroom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.example.myroom.objetos.Variable
import com.example.myroom.recyclerview.Rcv_mis_reservas
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class MisReservas : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    val db= Firebase.firestore
    val storage = Firebase.storage.reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_reservas)
        val menuLateral=findViewById<NavigationView>(R.id.nv_menu_lateral)
        menuLateral.visibility= NavigationView.INVISIBLE
        auth = Firebase.auth

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
        val listaVigente=ArrayList<Variable>()
        val listaFinalizadas=ArrayList<Variable>()

        val recyclerViewVigente = findViewById<RecyclerView>(R.id.rv_reservaVigente)
        val recyclerViewFinalizadas=findViewById<RecyclerView>(R.id.rv_reservaFinalizada)

        val adapterVigente=Rcv_mis_reservas(this,recyclerViewVigente,listaVigente)
        val adapterFinalizadas=Rcv_mis_reservas(this,recyclerViewFinalizadas,listaFinalizadas)

        val botonActivas=findViewById<TextView>(R.id.tv_btn_reserva_activa)
        botonActivas.setOnClickListener {
            if(recyclerViewVigente.visibility==RecyclerView.VISIBLE){
                recyclerViewVigente.visibility=RecyclerView.GONE

            }
            else{
                recyclerViewVigente.visibility=RecyclerView.VISIBLE
            }
        }

        val botonFinalizadas=findViewById<TextView>(R.id.tv_btn_reserva_finalizada)
        botonFinalizadas.setOnClickListener {
            if(recyclerViewFinalizadas.visibility==RecyclerView.VISIBLE){
                recyclerViewFinalizadas.visibility=RecyclerView.GONE

            }
            else{
                recyclerViewFinalizadas.visibility=RecyclerView.VISIBLE
            }
        }


        db.collection("ReservaCabecera").whereEqualTo("idUsuario","${auth.uid}").get()
            .addOnSuccessListener {
                for (reserva in it){

                    if(reserva["estado"]=="vigente"){
                        db.collection("Hotel").document("${reserva["idHotel"]}").get()
                            .addOnSuccessListener { hotel->
                               storage.child("Hoteles/${hotel.id}/1.jpg").getBytes(1024*1024*3)
                                   .addOnSuccessListener {  imagen->
                                       listaVigente.add(
                                           Variable(
                                               "${reserva.id}",
                                               imagen,
                                               reserva.getString("nombreHotel"),
                                               "${hotel.getString("ciudad")}, ${hotel.getString("pais")}",
                                               "${hotel.getDouble("puntuacion").toString()} estrellas",
                                               "${reserva.getString("fechaEntradaSalida")}",
                                               "${reserva.getDouble("total").toString()}"

                                           )
                                       )
                                       recyclerViewVigente.adapter=adapterVigente
                                       recyclerViewVigente.itemAnimator=androidx.recyclerview.widget.DefaultItemAnimator()
                                       recyclerViewVigente.layoutManager=androidx.recyclerview.widget.LinearLayoutManager(this)
                                       adapterVigente.notifyDataSetChanged()
                                   }



                            }

                    }
                    else if(reserva["estado"]=="finalizada"){
                        db.collection("Hotel").document("${reserva["idHotel"]}").get()
                            .addOnSuccessListener { hotel->
                                storage.child("Hoteles/${hotel.id}/1.jpg").getBytes(1024*1024*3)
                                    .addOnSuccessListener {  imagen->
                                        listaFinalizadas.add(
                                            Variable(
                                                "${reserva.id}",
                                                imagen,
                                                reserva.getString("nombreHotel"),
                                                "${hotel.getString("ciudad")}, ${hotel.getString("pais")}",
                                                "${hotel.getDouble("puntuacion")} estrallas",
                                                "${reserva.getString("fechaEntradaSalida")}",
                                                "$ ${reserva.getDouble("total")}"

                                            )
                                        )
                                        Log.i("Lista","${listaFinalizadas.size}")
                                        recyclerViewFinalizadas.adapter=adapterFinalizadas
                                        recyclerViewFinalizadas.itemAnimator=androidx.recyclerview.widget.DefaultItemAnimator()
                                        recyclerViewFinalizadas.layoutManager=androidx.recyclerview.widget.LinearLayoutManager(this)
                                        adapterFinalizadas.notifyDataSetChanged()
                                    }



                            }

                    }
                }
            }










        val botonAbrirYcerrarMenu= findViewById<ImageView>(R.id.img_btn_menulateral)
        botonAbrirYcerrarMenu.setOnClickListener{
            if (menuLateral.visibility== NavigationView.INVISIBLE){
                menuLateral.visibility= NavigationView.VISIBLE
            }else{
                menuLateral.visibility= NavigationView.INVISIBLE
            }
        }

        val botonPerfilIcon=findViewById<ImageView>(R.id.tv_img_perfil)
        botonPerfilIcon.setOnClickListener{
            menuLateral.visibility= NavigationView.INVISIBLE
            startActivity(Intent(this,Perfil::class.java))
            finish()
        }

        val botonPerfil = findViewById<TextView>(R.id.tv_btn_perfil)
        botonPerfil.setOnClickListener {
            menuLateral.visibility= NavigationView.INVISIBLE
            startActivity(Intent(this,Perfil::class.java))
            finish()
        }

        val botonFavoritos= findViewById<TextView>(R.id.tv_btn_favoritos)
        botonFavoritos.setOnClickListener {
            menuLateral.visibility= NavigationView.INVISIBLE
            startActivity(Intent(this,Favoritos::class.java))
            finish()
        }
        val botonReservar= findViewById<TextView>(R.id.tv_btn_reservar)
        botonReservar.setOnClickListener {
            menuLateral.visibility= NavigationView.INVISIBLE
            val intent =Intent(this,PreReserva::class.java)
            intent.putExtra("estado","abierta")
            startActivity(intent)
            finish()
        }
        val botonMisReservas= findViewById<TextView>(R.id.tv_btn_mis_reservas)
        botonMisReservas.setOnClickListener {
            menuLateral.visibility= NavigationView.INVISIBLE

        }
        val botonAyuda= findViewById<TextView>(R.id.tv_btn_ayuda)
        botonAyuda.setOnClickListener {
            menuLateral.visibility= NavigationView.INVISIBLE
            Toast.makeText(
                baseContext, "NO ESTA DISPONIBLE EN LA VERSION ACTUAL.",
                Toast.LENGTH_SHORT
            ).show()
        }
        val botonConfiguracion= findViewById<TextView>(R.id.tv_btn_configuracion)
        botonConfiguracion.setOnClickListener {
            menuLateral.visibility= NavigationView.INVISIBLE
            Toast.makeText(
                baseContext, "NO ESTA DISPONIBLE EN LA VERSION ACTUAL.",
                Toast.LENGTH_SHORT
            ).show()
        }

        val botonCerrarSession= findViewById<TextView>(R.id.tv_btn_cerrar_session)
        botonCerrarSession.setOnClickListener {
            menuLateral.visibility= NavigationView.INVISIBLE
            auth.signOut()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }



    }
}