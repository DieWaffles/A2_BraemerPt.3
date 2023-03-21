package edu.usna.mobileos.a2_braemerpt3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView


class Bookmark() : AppCompatActivity(), BookListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmark)
        val bookList = intent.getStringArrayListExtra("list") as ArrayList<String>
        val bookmarkListView : RecyclerView = findViewById(R.id.recyclerView)
        bookList.add("https://courses.cs.usna.edu")
        val bookListAdapter = BookmarkAdapter(bookList, this)
        bookmarkListView.adapter = bookListAdapter
    }

    override fun onItemClick(bookmark: String) {
        Log.println(Log.INFO, "intent", bookmark)
        val resultIntent = Intent()
        resultIntent.putExtra("returned_url",bookmark)
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}



class TextItemViewHolder(v: View): RecyclerView.ViewHolder(v){
    val textView: TextView = v.findViewById(R.id.itemTextView)
    fun bind(bookName: String, listener: BookListener){
        textView.text = bookName
        textView.setOnClickListener{ listener.onItemClick(bookName)}
    }
}

class BookmarkAdapter(val data: ArrayList<String>, val listener: BookListener): RecyclerView.Adapter<TextItemViewHolder>() {

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.item_layout, parent, false)
        return TextItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        holder.bind(data[position], listener)
    }
}

interface BookListener{
    fun onItemClick(bookmark: String)
}