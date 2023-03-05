package com.example.myroom

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.widget.*
import android.widget.TextView.*
import androidx.core.view.isVisible

import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.util.jar.Manifest

class Perfil : AppCompatActivity() {

    var storage = Firebase.storage.reference
    private lateinit var auth: FirebaseAuth
    val db =Firebase.firestore
    //lateinit var imageUri: Uri



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
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

        val fileName=auth.currentUser!!.uid +".jpg"
        val userReference=storage.child("FotoPerfil/"+fileName)


        val fotoPerfil = findViewById<ImageView>(R.id.img_perfil)
        userReference.getBytes(1024*1024*3).addOnSuccessListener {
            if(it!=null){
                fotoPerfil.setImageBitmap(BitmapFactory.decodeByteArray(it,0,it.size))
                fotoPerfil.isVisible= true
            }

        }.addOnFailureListener {
            fotoPerfil.isVisible= true
            Toast.makeText(this,"No se descargo la imagen de perfil",Toast.LENGTH_SHORT).show()
        }




        fotoPerfil.setOnClickListener {
            if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                val permission= arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permission,PERMISSION_CODE)
            }else{
                pickImageFromGalery()


                //fotoPerfil.setImageURI(imageUri)


            }



        }
        val textoaceptar = switchEnable()

        val botonEditar=findViewById<TextView>(R.id.txt_btn_editar_perfil)
        botonEditar.setOnClickListener {
            botonEditar.text=switchEnable()
            if(botonEditar.text=="Editar") {
                db.collection("Usuario")
                    .document(auth.currentUser!!.uid)
                    .update(
                        "genero",
                        findViewById<EditText>(R.id.txt_perfil_genero).text.toString(),
                        "nacionalidad",
                        findViewById<EditText>(R.id.txt_perfil_nacionalidad).text.toString(),
                        "descripcion",
                        findViewById<EditText>(R.id.txt_perfil_descripcion_personal).text.toString()
                    ).addOnSuccessListener {
                        Toast.makeText(
                            baseContext, "Las datos han sido actualizados",
                            Toast.LENGTH_SHORT
                        ).show()
                    }.addOnFailureListener {
                        Log.i("firestore", "Error ${it.toString()}")
                    }
                fotoPerfil.isDrawingCacheEnabled=true
                fotoPerfil.buildDrawingCache()
                val bitmap = (fotoPerfil.drawable as BitmapDrawable).bitmap
                val baos = ByteArrayOutputStream()
                bitmap .compress(Bitmap.CompressFormat.JPEG,100,baos)
                val data = baos.toByteArray()
                userReference.putBytes(data)
                    .addOnSuccessListener {
                        Log.i("storage","foto registrada")
                    }
                    .addOnFailureListener{
                        Toast.makeText(
                            baseContext, "Error subiendo la foto",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }


        }
        val botonCancelar=findViewById<TextView>(R.id.txt_btn_cancelar_editar_perfil)
        botonCancelar.setOnClickListener {
            botonEditar.text=switchEnable()
        }




        db.collection("Usuario").document(auth.currentUser!!.uid).get()
            .addOnSuccessListener { DocumentSnapshot->
                findViewById<TextView>(R.id.tv_nombrePerfil).text=DocumentSnapshot.getString("nombre")+" "+DocumentSnapshot.getString("apellido")
                findViewById<EditText>(R.id.txt_perfil_email).setText(DocumentSnapshot.getString("correo"))
                findViewById<EditText>(R.id.txt_perfil_genero).setText(DocumentSnapshot.getString("genero"))
                findViewById<EditText>(R.id.txt_perfil_nacionalidad).setText(DocumentSnapshot.getString("nacionalidad"))
                findViewById<EditText>(R.id.txt_perfil_descripcion_personal).setText(DocumentSnapshot.getString("descripcion"))
                findViewById<EditText>(R.id.txt_perfil_lugares_visitados).setText(DocumentSnapshot.getString("lugaresVisitados"))

            }
            .addOnFailureListener { }

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

        }

        val botonPerfil = findViewById<TextView>(R.id.tv_btn_perfil)
        botonPerfil.setOnClickListener {
            menuLateral.visibility= NavigationView.INVISIBLE

        }

        val botonFavoritos= findViewById<TextView>(R.id.tv_btn_favoritos)
        botonFavoritos.setOnClickListener {
            menuLateral.visibility= NavigationView.INVISIBLE
            startActivity(Intent(this,Favoritos::class.java))
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
            startActivity(Intent(this,MisReservas::class.java))
            finish()
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
            finish()
        }

        val botonCerrarSession= findViewById<TextView>(R.id.tv_btn_cerrar_session)
        botonCerrarSession.setOnClickListener {
            menuLateral.visibility= NavigationView.INVISIBLE
            auth.signOut()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

    }

    private fun pickImageFromGalery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object{
        private val IMAGE_PICK_CODE = 100
        private val PERMISSION_CODE =100

    }

    private fun switchEnable():String {
        if(findViewById<TextView>(R.id.txt_perfil_genero).isEnabled){
            findViewById<TextView>(R.id.txt_perfil_lugares_visitados).isEnabled=false
            findViewById<TextView>(R.id.txt_perfil_email).isEnabled=false
            findViewById<TextView>(R.id.txt_perfil_genero).isEnabled=false
            findViewById<TextView>(R.id.txt_perfil_descripcion_personal).isEnabled=false
            findViewById<TextView>(R.id.txt_perfil_nacionalidad).isEnabled=false
            findViewById<TextView>(R.id.txt_btn_cancelar_editar_perfil).visibility= INVISIBLE
            findViewById<ImageView>(R.id.img_perfil).isClickable=false
            return "Editar"


        }else{
            //findViewById<TextView>(R.id.txt_perfil_lugares_visitados).isEnabled=true
           // findViewById<TextView>(R.id.txt_perfil_email).isEnabled=true
            findViewById<TextView>(R.id.txt_perfil_genero).isEnabled=true
            findViewById<TextView>(R.id.txt_perfil_descripcion_personal).isEnabled=true
            findViewById<TextView>(R.id.txt_perfil_nacionalidad).isEnabled=true
            findViewById<TextView>(R.id.txt_btn_cancelar_editar_perfil).visibility= VISIBLE
            findViewById<ImageView>(R.id.img_perfil).isClickable=true
            return "Guardar"
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data!!.data!=null){
                findViewById<ImageView>(R.id.img_perfil).setImageURI(data.data)
                //imageUri = data.data!!

            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode){
            PERMISSION_CODE->{
                if(grantResults.size>0&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    pickImageFromGalery()
                }else{
                    Toast.makeText(this,"permiso denegado",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}