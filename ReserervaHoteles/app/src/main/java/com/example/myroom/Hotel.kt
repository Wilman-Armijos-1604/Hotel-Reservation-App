package com.example.myroom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myroom.objetos.Habitaciones
import com.example.myroom.recyclerview.Rcv_Imagenes_Horizontales
import com.example.myroom.recyclerview.Rcv_lista_hoteles
import com.example.myroom.recyclerview.Rcv_metodos_pago
import com.example.myroom.recyclerview.Rcv_servicios
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class Hotel : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    val ref = Firebase.storage.reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel)
        val menuLateral = findViewById<NavigationView>(R.id.nv_menu_lateral)
        menuLateral.visibility = NavigationView.INVISIBLE
        auth = Firebase.auth



        val listaImagenes = arrayListOf<ByteArray>()
        val idHotel = intent.getStringExtra("id")

        val botonAddFavoritos=findViewById<ImageView>(R.id.img_btn_favoritos_hoteles)
        botonAddFavoritos.setOnClickListener {
               db.collection("HotelFavoritos").whereEqualTo("idHotel","${idHotel}").whereEqualTo("idUsuario","${auth.uid}").get()
                   .addOnSuccessListener {
                       if(it.isEmpty){
                           db.collection("Hotel").document("${idHotel}").get()
                               .addOnSuccessListener { hotel->
                                   db.collection("HotelFavoritos").add(
                                       hashMapOf(
                                           "idHotel" to "${hotel.id}",
                                           "nombreHotel" to "${hotel.getString("nombre")}",
                                           "direccion" to "${hotel.getString("ciudad")}, ${hotel.getString("pais")}",
                                           "puntuacion" to "${hotel.getDouble("puntuacion")}",
                                           "idUsuario" to "${auth.uid}"

                                       )
                                   ).addOnSuccessListener {
                                       Toast.makeText(this,"Agregado a Favoritos",Toast.LENGTH_SHORT).show()
                                   }

                               }




                       }
                       else{
                           db.collection("HotelFavoritos").document("${it.documents[0].id}").delete()
                               .addOnSuccessListener {
                                   Toast.makeText(this,"Eliminado de Favoritos",Toast.LENGTH_SHORT).show()
                               }
                       }
                   }
        }




        val recyclerViewImagenesHotel = findViewById<RecyclerView>(R.id.rv_fotosHotel)

        val adapter = Rcv_Imagenes_Horizontales(this, recyclerViewImagenesHotel, listaImagenes)

        val hotelHabitacionReference = ref.child("Hoteles/" + idHotel.toString())
        hotelHabitacionReference.listAll()
            .addOnSuccessListener {

                it.items.forEach() { imagenItem ->
                    imagenItem.getBytes(1024 * 1024 * 3).addOnSuccessListener { imagen ->
                        Log.i("storage", "consulta imagen 555HotelHabitacion")
                        listaImagenes.add(imagen)
                        recyclerViewImagenesHotel.adapter = adapter
                        recyclerViewImagenesHotel.itemAnimator =
                            androidx.recyclerview.widget.DefaultItemAnimator()
                        //recyclerViewImagenesHotel.layoutManager=androidx.recyclerview.widget.LinearLayoutManager(this)

                        recyclerViewImagenesHotel.layoutManager =
                            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                        adapter.notifyDataSetChanged()
                    }
                        .addOnFailureListener {
                            Log.i("recyclerView", "error ${it.message}")
                        }
                }

            }
        val recyclerViewIzquierda = findViewById<RecyclerView>(R.id.rv_servicios1)
        val recyclerViewDerecha = findViewById<RecyclerView>(R.id.rv_servicios2)
        val recyclerMetodoPago = findViewById<RecyclerView>(R.id.rv_metodoDePago_hotel)
        val listaIzquierza = arrayListOf<String>()
        val listaderecha = arrayListOf<String>()
        val listaMetodos = arrayListOf<String>()
        val adapterRVIz = Rcv_servicios(this, recyclerViewIzquierda, listaIzquierza)
        val adapterRVDer = Rcv_servicios(this, recyclerViewDerecha, listaderecha)
        val adapterRVMet = Rcv_metodos_pago(this, recyclerMetodoPago, listaMetodos)

        db.collection("Hotel").document("${idHotel}").get()
            .addOnSuccessListener {


                var hashMetodos: HashMap<String, Any> =
                    it.data!!.get("metodosDePago") as HashMap<String, Any>
                findViewById<TextView>(R.id.txv_NombreHotel).text = it.getString("nombre")
                val servicios = it.data!!.get("servicios")

                var count = 0
                (servicios as ArrayList<String>).forEach {
                    if (count % 2 == 0) {
                        listaIzquierza.add(it)
                        count++
                    } else {
                        listaderecha.add(it)
                        count++
                    }
                }

                if (hashMetodos["pagoConTarjeta"] == true) {
                    listaMetodos.add("Se acepta pago con Tarjeta")
                }
                if (hashMetodos["pagoConPayPal"] == true) {
                    listaMetodos.add("Se acepta pago con PayPal")
                }
                if (hashMetodos["pagoEnHotel"] == true) {
                    listaMetodos.add("Se acepta pago en el Hotel")
                }
                recyclerViewIzquierda.adapter = adapterRVIz
                recyclerViewIzquierda.itemAnimator =
                    androidx.recyclerview.widget.DefaultItemAnimator()
                recyclerViewIzquierda.layoutManager =
                    androidx.recyclerview.widget.LinearLayoutManager(this)
                adapterRVIz.notifyDataSetChanged()

                recyclerViewDerecha.adapter = adapterRVDer
                recyclerViewDerecha.itemAnimator =
                    androidx.recyclerview.widget.DefaultItemAnimator()
                recyclerViewDerecha.layoutManager =
                    androidx.recyclerview.widget.LinearLayoutManager(this)
                adapterRVDer.notifyDataSetChanged()

                recyclerMetodoPago.adapter = adapterRVMet
                recyclerMetodoPago.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
                recyclerMetodoPago.layoutManager =
                    androidx.recyclerview.widget.LinearLayoutManager(this)
                adapterRVMet.notifyDataSetChanged()
            }

        val botonIrTipoDeHabitacion = findViewById<TextView>(R.id.tv_btn_VerHabitacionesHotel)
        botonIrTipoDeHabitacion.setOnClickListener {
            /*
            val ref1 = Firebase.storage.reference
            val ImagenesTiposHabitacion = ArrayList<ArrayList<ByteArray>>()
            db.collection("TipoHabitacion").whereEqualTo("idHotel", "${idHotel}").get()
                .addOnSuccessListener {
                    for (a in it) {
                        Log.i("Lists", "${a.id}")
                        val nose = ref1.child("TiposHabitaciones/" + a.id.toString())
                        nose.listAll()
                            .addOnSuccessListener { listita ->
                                Log.i("ListsAAAA", "${listita.prefixes}")
                                val listaUnTipo = ArrayList<ByteArray>()
                                listita.items.forEach { imagen ->


                                    listaUnTipo.add(
                                        imagen.getBytes(1024 * 1024 * 3).addOnSuccessListener {
                                            Log.i("ListsBBBB", "exito")
                                        }.result
                                    )


                                }
                                ImagenesTiposHabitacion.add(listaUnTipo)
                                Log.i("Listsi", "${ImagenesTiposHabitacion.size}")
                            }
                            .addOnFailureListener {
                                Log.i("Error Lista", "${it.message}")
                            }
                            .addOnCompleteListener {
                                Log.i("Error Lista", "${it.result}")
                            }

                    }

                    Log.i("Listso", "${ImagenesTiposHabitacion.size}")

                }
*/

            val intent = Intent(this, TipoDeHabitacion::class.java)
            intent.putExtra("idHotel", "${idHotel}")
            intent.putExtra("nombreHotel","${findViewById<TextView>(R.id.txv_NombreHotel).text.toString()}")
            intent.putExtra("idPrereserva","no")
            startActivity(intent)
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