package com.example.evaluacion3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.evaluacion3.databinding.ActivityMainBinding
import com.example.evaluacion3.databinding.ActivityRegistrarBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegistrarActivity : AppCompatActivity() {

    //Configurar viewBinding
    private lateinit var binding: ActivityRegistrarBinding
    // Configurar firebase
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegistrarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.btnRegistrar.setOnClickListener {
            // Obtener el correo
            val email = binding.etEmail.text.toString()
            val pass1 = binding.etPassword.text.toString()
            val pass2 = binding.etPassword2.text.toString()

            if(email.isEmpty()){
                binding.etEmail.error = "Por favor ingrese un correo"
                return@setOnClickListener
            }

            if(pass1.isEmpty()){
                binding.etPassword.error = "Por favor ingrese una contraseña"
                return@setOnClickListener
            }

            if(pass2.isEmpty()){
                binding.etPassword2.error = "Por favor ingrese la contraseña nuevamente"
                return@setOnClickListener
            }

            //Validar que ambas contraseñas coincidan
            if(pass1 != pass2){
                binding.etPassword2.error = "Las contraseñas no coinciden"
                return@setOnClickListener
            } else {
                signUp(email, pass1)
            }
        }
    }

    private fun signUp(email: String, pass1: String) {
        auth.createUserWithEmailAndPassword(email,pass1)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, login::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this, "Error en el registro del usuario", Toast.LENGTH_SHORT).show()
                }
            }
    }
}