package com.example.listalumnos

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listalumnos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: AlumnoAdapter
    private val data = ArrayList<Alumno>()
    private lateinit var rvAdapter: AlumnoAdapter


    val dbalumnos = DBHelperAlumno(this)
    val db = dbalumnos.writableDatabase

    companion object {
        private const val REQUEST_CODE_NEW_ALUMNO = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        cargarDatosDesdeSQLite()

        binding.recyclerview.adapter = adapter

        adapter.setOnItemClickListener(object: AlumnoAdapter.ClickListener {
            override fun onItemClick(position: Int) {
                itemOptionsMenu(position)
            }
        })

        binding.faButton.setOnClickListener {
            val intento1 = Intent(this, MainActivityNuevo::class.java)
            startActivityForResult(intento1, REQUEST_CODE_NEW_ALUMNO)
        }
    }

    private fun cargarDatosDesdeSQLite() {
        val cursor = db.rawQuery("SELECT * FROM alumnos", null)
        if (cursor.moveToFirst()) {
            do {
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val cuenta = cursor.getString(cursor.getColumnIndexOrThrow("nocuenta"))
                val correo = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                val imagen = cursor.getString(cursor.getColumnIndexOrThrow("imagen"))

                val alumno = Alumno(nombre, cuenta, correo, imagen)
                data.add(Alumno("${nombre}", "${cuenta}", "${correo}", "${imagen}"))
            } while (cursor.moveToNext())
        }
        db.close()
        cursor.close()

        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        rvAdapter = AlumnoAdapter(this, data, object : AlumnoAdapter.ClickListener {
            override fun onItemClick(position: Int) {
                itemOptionsMenu(position)
            }
        })

        binding.recyclerview.adapter = rvAdapter

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_NEW_ALUMNO && resultCode == RESULT_OK) {
            val msje = data?.getStringExtra("mensaje")
            val nombre = data?.getStringExtra("nombre")
            val cuenta = data?.getStringExtra("cuenta")
            val correo = data?.getStringExtra("correo")
            val image = data?.getStringExtra("image")

            if (msje == "nuevo") {
                data?.let {
                    val insertIndex: Int = this.data.size
                    this.data.add(Alumno("$nombre", "$cuenta", "$correo", "$image"))
                    adapter.notifyItemInserted(insertIndex)
                }
            }
        }
    }

    private fun itemOptionsMenu(position: Int) {
        val viewHolder = binding.recyclerview.findViewHolderForAdapterPosition(position)
            ?: return

        val popupMenu = PopupMenu(this, viewHolder.itemView.findViewById(R.id.textViewOptions))
        popupMenu.inflate(R.menu.options_menu)

        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId) {
                    R.id.borrar -> {
                        val tmpAlum = data[position]
                        data.remove(tmpAlum)
                        adapter.notifyItemRemoved(position)
                        return true
                    }
                    R.id.editar -> {
                        val intento2 = Intent(this@MainActivity, MainActivityNuevo::class.java)
                        val alumno = data[position]
                        intento2.putExtra("mensaje", "edit")
                        intento2.putExtra("nombre", alumno.nombre)
                        intento2.putExtra("cuenta", alumno.cuenta)
                        intento2.putExtra("correo", alumno.correo)
                        intento2.putExtra("image", alumno.imagen)
                        startActivityForResult(intento2, REQUEST_CODE_NEW_ALUMNO)
                        return true
                    }
                }
                return false
            }
        })
        popupMenu.show()
    }
}
