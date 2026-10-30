package com.example.evaluacion3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.evaluacion3.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class login : AppCompatActivity() {

    //Configurar viewBinding
    private lateinit var binding: ActivityLoginBinding
    // Configurar firebase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //Inicializar viewBinding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Programar el boton de login
        binding.btnLogin.setOnClickListener {

            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty()){
                binding.etEmail.error = "Por favor ingrese un correo"
                return@setOnClickListener
            }

            if (password.isEmpty()){
                binding.etPassword.error = "Por favor ingrese la contraseña"
                return@setOnClickListener
            }
            signIn(email, password)
        }

        // Programar el enlace para ir a Registro
        binding.tvRegistrar.setOnClickListener {
            try {
                val intent = Intent(this, RegistrarActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    Toast.makeText(this, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show()

                    // Ir al crud
                    try {
                        val intent = Intent(this, crud::class.java)
                        startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
                    }

                }else{
                    Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
                }
            }
    }
}