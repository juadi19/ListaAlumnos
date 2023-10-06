package com.example.listalumnos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AlumnoAdapter(private val context: Context, private val listAlumno: List<Alumno>, private var optionsMenuClickListener : ClickListener) : RecyclerView.Adapter<AlumnoAdapter.ViewHolder>() {

    private var clickListener: ClickListener? = null
    interface ClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the single_item view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val ItemsViewModel = listAlumno[position]
        // sets the image to the imageview from our itemHolder class
        //holder.imageView.setImageResource(ItemsViewModel.imagen)
        Glide.with(context).load(ItemsViewModel.imagen).into(holder.imageView)
        // sets the text to the textview from our itemHolder class
        holder.txtNombre.text=ItemsViewModel.nombre
        // sets the text to the textview from our itemHolder class
        holder.txtCuenta.text=ItemsViewModel.cuenta
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return listAlumno.size
    }

    fun setOnItemClickListener(clickListener: ClickListener){
        this.clickListener = clickListener
    }

    inner class ViewHolder(itView: View):RecyclerView.ViewHolder(itView), View.OnClickListener {
        val imageView: ImageView = itemView.findViewById(R.id.imgAlumno)
        val txtNombre: TextView = itemView.findViewById(R.id.nombre)
        val txtCuenta: TextView = itemView.findViewById(R.id.cuenta)

        init {
            if(clickListener != null){
                itemView.setOnClickListener(this)
            }
        }

        override fun onClick(itView: View){
            clickListener?.onItemClick( adapterPosition)
        }
    }
}