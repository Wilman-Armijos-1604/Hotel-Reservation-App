package com.example.myroom

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {


    val callbackManager = CallbackManager.Factory.create();

    private lateinit var auth: FirebaseAuth
    val CODIGO_GOOGLE_SIGN_IN = 600
    val CODIGO_FACEBOOK_SIGN_IN = 800
    private lateinit var googleSignIN:GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Para facebook


        //LoginManager.getInstance().logOut()
        val botonIngresar=findViewById<Button>(R.id.btn_IngresarAppFacebook)

        botonIngresar.setOnClickListener{
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))

            Log.i("facebook","hice clik")
            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult?> {
                    override fun onSuccess(loginResult: LoginResult?) {
                        loginResult?.let {
                            Log.i("facebook","token ${it.accessToken}")
                            val token=it.accessToken
                            val credential=FacebookAuthProvider.getCredential(token.token)
                            FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                                if(it.isSuccessful){
                                    Log.i("firebaseauth", "ususario iniciar session con Exito con facebook")
                                    LoginManager.getInstance().logOut()
                                    funcionAbrirXD()
                                    finish()
                                } else {
                                    Log.i("firebaseauth", "Error ${it.exception}")
                                    Toast.makeText(
                                        baseContext, "Falla de Autenticacion.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }

                    override fun onCancel() {
                        Log.i("facebook","cancelo")
                    }

                    override fun onError(exception: FacebookException) {
                        Log.i("facebook","error :${exception.message}")
                    }
                })
            LoginManager.getInstance().logOut()
        }



        val botonIniciarSessionGoogle = findViewById<Button>(R.id.btn_IngresarAppGoogle)

        botonIniciarSessionGoogle.setOnClickListener {
            googleSignIN=GoogleSignIn.getClient(this,GoogleSignInOptions
                .Builder(
                    GoogleSignInOptions
                        .DEFAULT_SIGN_IN
                )
                .requestIdToken(
                    getString(
                        R.string.default_web_client_id)
                ).requestEmail()
                .build())

            startActivityForResult(googleSignIN.signInIntent,CODIGO_GOOGLE_SIGN_IN
            )
            googleSignIN.signOut()
        }



        auth= Firebase.auth
        val botonRegistrarse =  findViewById<TextView>(R.id.txt_btn_IrRegistrarse)
        botonRegistrarse.setOnClickListener {
            startActivity(Intent(this,Registarse::class.java))
            finish()
        }

        val botonSalir = findViewById<Button>(R.id.btn_SalirApp)
        botonSalir.setOnClickListener {
            System.exit(0)
        }

        val botonIniciarSession= findViewById<Button>(R.id.btn_IngresarApp)
        botonIniciarSession.setOnClickListener {
            val correo = findViewById<EditText>(R.id.txtUsuario).text.toString()!!
            val contrase = findViewById<EditText>(R.id.txt_corroIniciarSession).text.toString()!!
            if (correo.length>0 && contrase.length>0  ){
                auth.signInWithEmailAndPassword(
                    correo,
                    contrase,
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.i("firebaseauth", "ususario iniciar session con Exito")
                            //Toast.makeText(baseContext, ".",
                            //Toast.LENGTH_SHORT).show()
                            //val user = auth.currentUser

                            startActivity(Intent(this, ListaDeHoteles::class.java))
                            finish()
                        } else {
                            Log.i("firebaseauth", "Error ${task.exception}")
                            Toast.makeText(
                                baseContext, "Falla de Autenticacion.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
            else{
                Toast.makeText(
                    baseContext, "Campos vacios.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            startActivity(Intent(this,ListaDeHoteles::class.java))
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode,resultCode,data)
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==CODIGO_GOOGLE_SIGN_IN){
            try {
                val account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException::class.java)
                if(account != null){
                    FirebaseAuth.getInstance().signInWithCredential(GoogleAuthProvider.getCredential(account.idToken,null))
                        .addOnCompleteListener {
                            startActivity(Intent(this,ListaDeHoteles::class.java))
                            finish()
                        }
                }
            }catch(e:ApiException){
                Log.i("google","Ocurrio exception")
            }

        }
    }

    fun funcionAbrirXD(){
        startActivity(Intent(this, ListaDeHoteles::class.java))
    }





}