package com.example.evaluacion3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.evaluacion3.Models.Pulsacion
import com.example.evaluacion3.databinding.ActivityCrudBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class crud : AppCompatActivity() {

    // viewBinding
    private lateinit var binding: ActivityCrudBinding

    // Base de datos Firebase
    private lateinit var database: DatabaseReference

    // Configurar Firebase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCrudBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Inicializar Firebase Auth
        auth = Firebase.auth

        binding.btnLogout.setOnClickListener {

            MaterialAlertDialogBuilder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Estas seguro que deseas cerrar sesión?")
                .setNeutralButton("Cancelar") { dialog, which ->
                    // Respond to neutral button press
                }
                .setPositiveButton("Aceptar") { dialog, which ->
                    signOut() // Cerrar sesión
                }
                .show()
        }

        // Inicializar base de datos
        database = FirebaseDatabase.getInstance().getReference("Pulsaciones")

        binding.btnGuardar.setOnClickListener {
            // Obtener los datos del formulario de pulsaciones
            val pulso = binding.etPulsaciones.text.toString()
            val oxigeno = binding.etOxigeno.text.toString()

            // Crea el id de forma automatica al insertar una pulsacion
            val id = database.child("Pulsaciones").push().key

            if (pulso.isEmpty()) {
                binding.etPulsaciones.error = "Por favor ingresar una pulsacion"
                return@setOnClickListener
            }
            if (oxigeno.isEmpty()) {
                binding.etOxigeno.error = "Por favor ingresar nivel de oxigeno en sangre"
                return@setOnClickListener
            }

            // variable que contiene los datos del constructor
            val pulsacion = Pulsacion(id, pulso, oxigeno)

            // el dato puede ser string o nulo
            database.child(id!!).setValue(pulsacion).addOnSuccessListener {
                binding.etPulsaciones.setText("")
                binding.etOxigeno.setText("")
                Snackbar.make(binding.root, "Datos Agregados", Snackbar.LENGTH_SHORT).show()
                // confirmacion al insertar dato
            }
        }

        // Configurar el botón para cambiar la contraseña
        binding.btnCambiarPassword.setOnClickListener {
            val intent = Intent(this, cambiarPassword::class.java)
            startActivity(intent)
        }
    }

    private fun signOut() {
        Firebase.auth.signOut()
        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
        finish()
    }
}