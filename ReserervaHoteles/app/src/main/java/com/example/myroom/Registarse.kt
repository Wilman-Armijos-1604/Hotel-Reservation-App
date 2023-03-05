package com.example.myroom

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.myroom.objetos.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class Registarse : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val db =Firebase.firestore
    val usuario= Usuario()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registarse)

        auth = Firebase.auth
        val botonIrLogin = findViewById<TextView>(R.id.txt_btn_IrInicioSession)
        botonIrLogin.setOnClickListener {
           startActivity(Intent(this,MainActivity::class.java))

            finish()
        }



        val botonSalir = findViewById<Button>(R.id.btn_SalirApp)
        botonSalir.setOnClickListener {
            System.exit(0)
        }
        val botonRegistarse= findViewById<Button>(R.id.btn_RegistrarseApp)



        botonRegistarse.setOnClickListener {
            usuario.nombre=findViewById<EditText>(R.id.txt_NombreRegistrar).text.toString()
            usuario.apellido=findViewById<EditText>(R.id.txt_ApellidoRegistrar).text.toString()
            usuario.correo=findViewById<EditText>(R.id.txt_CorreoRegistrar).text.toString()
            val pass=findViewById<EditText>(R.id.txt_ContraseñaRegistrar).text.toString()
            val passConf=findViewById<EditText>(R.id.txt_RepetirContraseñaRegistrar).text.toString()
           if(validarCamposLlenos(pass,passConf)) {
                if(pass==passConf) {
                    auth.createUserWithEmailAndPassword(
                        usuario.correo.toString(),
                        pass,
                    )
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                Log.i("firebaseauth", "Usuario Registrado Con exito")
                                Toast.makeText(
                                    baseContext, "Usuario registrado con exito.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val usuarioMap= hashMapOf<String,Any>(
                                    "nombre" to usuario.nombre.toString(),
                                    "apellido" to usuario.apellido.toString(),
                                    "correo" to usuario.correo.toString(),
                                    "nacionalidad" to "",
                                    "genero" to "",
                                    "descripcion" to "",
                                    "lugaresVisitados" to "",
                                    "fotoPerfil" to ""

                                )
                                db.collection("Usuario").document(auth.currentUser!!.uid).set(usuarioMap)
                                    .addOnSuccessListener {
                                        Log.i("firestore","Usuario Registrado")
                                    }.addOnFailureListener {
                                        Log.i("firestore","No se registro")
                                    }

                                val builder = AlertDialog.Builder(this)
                                builder.setTitle("Completar Perfil")
                                builder.setMessage("Desea continuar configurando su perfil")
                                builder.setPositiveButton(
                                    "Sí", DialogInterface.OnClickListener { dialog, id ->
                                        startActivity(Intent(this, Perfil::class.java))

                                        dialog.cancel()
                                        finish()
                                    }
                                )
                                builder.setNegativeButton(
                                    "Mas tarde", DialogInterface.OnClickListener { dialog, id ->
                                        startActivity(Intent(this, ListaDeHoteles::class.java))

                                        dialog.cancel()
                                        finish()
                                    }
                                )
                                builder.show()


                                //val user = auth.currentUser




                            } else {
                                Log.i("firebaseauth", "Error ${task.exception}")
                                Toast.makeText(
                                    baseContext, "Error registrando Usuario.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
                else{
                    Log.i("firebaseauth", "Las contraseñas no coinciden")
                    Toast.makeText(
                        baseContext, "Las contraseñas no coinciden",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }else{
                Log.i("firebaseauth", "Error campos vacios")
                Toast.makeText(
                    baseContext, "No estan llenos todos los campos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }



    private fun validarCamposLlenos(pass:String,valPass:String): Boolean {
        return usuario.nombre?.length!!  > 0 && usuario.apellido?.length!! >0 && usuario.correo?.length!! >0&& pass.length>0 && valPass.length>0
    }


}