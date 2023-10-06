package com.example.listalumnos

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.listalumnos.databinding.ActivityMainNuevoBinding

class MainActivityNuevo : AppCompatActivity() {

    private lateinit var binding: ActivityMainNuevoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainNuevoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGuardar.setOnClickListener {
            val txtNom = binding.txtNombre.text.toString()
            val txtCue = binding.txtCuenta.text.toString()
            val txtCorr = binding.txtCorreo.text.toString()
            val txtImg = binding.txtImage.text.toString()

            val dbalumnos = DBHelperAlumno(this)

            val db = dbalumnos.writableDatabase
            val newReg = ContentValues()
            newReg.put("nombre", txtNom)
            newReg.put("nocuenta", txtCue)
            newReg.put("email", txtCorr)
            newReg.put("imagen", txtImg)


            val res = db.insert("alumnos", null, newReg)

            db.close()

            if (res.toInt() == -1) {
                Toast.makeText(this, "No se inserto el registro", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Registro insertado correctamente", Toast.LENGTH_LONG).show()
                binding.txtNombre.text.clear()
                binding.txtCuenta.text.clear()
                binding.txtCorreo.text.clear()
                binding.txtImage.text.clear()
            }



            val intento2 = Intent()
            intento2.putExtra("mensaje", "nuevo")
            intento2.putExtra("nombre", txtNom)
            intento2.putExtra("cuenta", txtCue)
            intento2.putExtra("correo", txtCorr)
            intento2.putExtra("image", txtImg)

            setResult(Activity.RESULT_OK, intento2)
            finish()
        }
    }
}
