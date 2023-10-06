
package com.example.listalumnos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listalumnos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    var idAlumno: Int = 0
    private var  data = ArrayList<Alumno>()
    private lateinit var rvAdapter : AlumnoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dbalumnos = DBHelperAlumno(this)
        val db = dbalumnos.readableDatabase

        val cursor = db.rawQuery("SELECT * FROM alumnos",null)

        if(cursor.moveToFirst()){
            do {
                idAlumno = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                var itemNom = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                var itemCue = cursor.getString(cursor.getColumnIndexOrThrow("nocuenta"))
                var itemCorr = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                var itemImg = cursor.getString(cursor.getColumnIndexOrThrow("imagen"))

                data.add(
                    Alumno("${itemNom}",
                        "${itemCue}",
                        "${itemCorr}",
                        "${itemImg}"
                    )
                )

            }while (cursor.moveToNext())
            db.close()
            cursor.close()

            binding.recyclerview.layoutManager = LinearLayoutManager(this)

            rvAdapter = AlumnoAdapter(this, data, object : AlumnoAdapter.OptionsMenuClickListener{
                override fun onOptionsMenuClicked(position: Int) {
                    itemOptiomsMenu(position)
                }
            })

            binding.recyclerview.adapter = rvAdapter

        }

        //Add elements to data array
        /*data.add(Alumno("José Nabor", "20102345","jmorfin@ucol.mx","https://imagenpng.com/wp-content/uploads/2017/02/pokemon-hulu-pikach.jpg"))
        data.add(Alumno("Luis Antonio", "20112345","jmorfin@ucol.mx","https://i.pinimg.com/236x/e0/b8/3e/e0b83e84afe193922892917ddea28109.jpg"))
        data.add(Alumno("Juan Pedro", "20122345","jmorfin@ucol.mx","https://i.pinimg.com/736x/9f/6e/fa/9f6efa277ddcc1e8cfd059f2c560ee53--clipart-gratis-vector-clipart.jpg"))*/


        val parExtra = intent.extras
        val msje = parExtra?.getString("mensaje")
        val nombre = parExtra?.getString("nombre")
        val cuenta = parExtra?.getString("cuenta")
        val correo = parExtra?.getString("correo")
        val image = parExtra?.getString("image")


        if (msje== "nuevo"){

            val insertIndex: Int = data.count()

            data.add(insertIndex,
                Alumno(
                    "${nombre}",
                    "$cuenta}",
                    "${correo}",
                    "${image}"
                )
            )

            rvAdapter.notifyItemInserted(insertIndex)

        }


        binding.faButton.setOnClickListener {
            val intento1 = Intent(this,MainActivityNuevo::class.java)

            startActivity(intento1)

        }

    }

    private fun itemOptiomsMenu(position: Int) {
        val popupMenu = PopupMenu(this,binding.recyclerview[position].findViewById(R.id.textViewOptions))
        popupMenu.inflate(R.menu.options_menu)

        val intento2 = Intent(this,MainActivityNuevo::class.java)

        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId){
                    R.id.borrar -> {
                        val tmpAlum = data[position]
                        data.remove(tmpAlum)
                        rvAdapter.notifyDataSetChanged()
                        return true
                    }
                    R.id.editar ->{
                        val nombre = data[position].nombre
                        val cuenta = data[position].cuenta
                        val correo = data[position].correo
                        val image = data[position].imagen
                        val idAlum: Int = position
                        intento2.putExtra("mensaje","edit")
                        intento2.putExtra("nombre","${nombre}")
                        intento2.putExtra("cuenta","${cuenta}")
                        intento2.putExtra("correo","${correo}")
                        intento2.putExtra("image","${image}")
                        intento2.putExtra("idA",idAlum)
                        startActivity(intento2)
                        return true
                    }
                }
                return false
            }
        })
        popupMenu.show()


    }

}
