package com.example.homework18

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.Collections

class MainActivity : AppCompatActivity() {
    private val disposable = CompositeDisposable()
    private var adapter:RecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        val request = ApiClient.retrofit.create(ApiInterface::class.java)
            .getSuperheroes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                adapter = RecyclerViewAdapter(response) { name ->
                    Toast.makeText(this, name, Toast.LENGTH_SHORT).show()
                }
                recyclerView.adapter = adapter
            }, { error ->
                Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
            })
        disposable.add(request)
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
            ): Int {
                return makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.END)
            }
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                val fromIndex = viewHolder.adapterPosition
                val toIndex = target.adapterPosition
                Collections.swap(adapter?.items, fromIndex, toIndex)
                adapter?.notifyItemMoved(fromIndex, toIndex)
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.END) {
//                    adapter?.items?.removeAt(viewHolder.adapterPosition)
                    adapter?.notifyItemRemoved(viewHolder.adapterPosition)
                }
            }

        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }
}
data class Superhero(
    val id: Int,
    val name: String,
    val images: Images
)
data class Images(
    val xs: String,
    val sm: String,
    val md: String,
    val lg: String
)