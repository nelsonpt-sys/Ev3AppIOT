package com.example.evaluacion3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.evaluacion3.databinding.ActivityCambiarPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class cambiarPassword : AppCompatActivity() {

    // Configurar viewBinding
    private lateinit var binding: ActivityCambiarPasswordBinding
    // Configurar firebase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCambiarPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.btnGuardarCambios.setOnClickListener {
            // Obtener el correo y las contraseñas
            val email = binding.etEmail2.text.toString()
            val pass1 = binding.etPasswordNew.text.toString()
            val pass2 = binding.etPasswordNew2.text.toString()

            if (email.isEmpty()) {
                binding.etEmail2.error = "Por favor ingrese su correo"
                return@setOnClickListener
            }

            if (pass1.isEmpty()) {
                binding.etPasswordNew.error = "Por favor ingrese una Nueva contraseña"
                return@setOnClickListener
            }

            if (pass2.isEmpty()) {
                binding.etPasswordNew2.error = "Por favor ingrese la Nueva contraseña nuevamente"
                return@setOnClickListener
            }

            // Validar que ambas contraseñas coincidan
            if (pass1 != pass2) {
                binding.etPasswordNew2.error = "Las contraseñas no coinciden"
                return@setOnClickListener
            } else {
                changePassword(email, pass1)
            }
        }
    }

    private fun changePassword(email: String, newPassword: String) {
        val user = auth.currentUser
        if (user != null && user.email == email) {
            user.updatePassword(newPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Contraseña cambiada exitosamente", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, login::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Error al cambiar la contraseña", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "El correo ingresado no coincide con el usuario actual", Toast.LENGTH_SHORT).show()
        }
    }
}