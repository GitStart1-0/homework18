package com.example.homework18

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecyclerViewAdapter(var items: List<Superhero>, val onClick: (String) -> Unit) : RecyclerView.Adapter<RecyclerViewAdapter.SuperheroViewHolder>() {

    class SuperheroViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.list_title)
        val image: ImageView = view.findViewById(R.id.list_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperheroViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return SuperheroViewHolder(itemView)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: SuperheroViewHolder, position: Int) {
        val superhero = items[position]
        holder.name.text = superhero.name
        Glide.with(holder.itemView).load(superhero.images.lg).into(holder.image)
        holder.itemView.setOnClickListener {
            onClick(superhero.name)
        }
    }
}