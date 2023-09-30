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

    companion object {
        private const val REQUEST_CODE_NEW_ALUMNO = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        // ArrayList of class Alumno
        data.add(Alumno("José Nabor", "20102345", "jmorfin@ucol.mx", "https://imagenpng.com/wp-content/uploads/2017/02/pokemon-hulu-pikach.jpg"))
        data.add(Alumno("Luis Antonio", "20112345", "jmorfin@ucol.mx", "https://i.pinimg.com/236x/e0/b8/3e/e0b83e84afe193922892917ddea28109.jpg"))
        data.add(Alumno("Juan Pedro", "20122345", "jmorfin@ucol.mx", "https://i.pinimg.com/736x/9f/6e/fa/9f6efa277ddcc1e8cfd059f2c560ee53--clipart-gratis-vector-clipart.jpg"))

        adapter = AlumnoAdapter(this, data)
        binding.recyclerview.adapter = adapter

        adapter.setOnItemClickListener(object: AlumnoAdapter.ClickListener {
            override fun onItemClick(view: View, position: Int) {
                itemOptionsMenu(position)
            }
        })

        binding.faButton.setOnClickListener {
            val intento1 = Intent(this, MainActivityNuevo::class.java)
            startActivityForResult(intento1, REQUEST_CODE_NEW_ALUMNO)
        }
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
