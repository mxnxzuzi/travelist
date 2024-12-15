package ddwu.com.mobile.myapplication.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import ddwu.com.mobile.myapplication.R
import ddwu.com.mobile.myapplication.data.model.Note

class ImageAdapter(private val context: Context, private val notes: List<Note>) : BaseAdapter() {


    override fun getCount(): Int = notes.size

    override fun getItem(position: Int): Note = notes[position]

    override fun getItemId(position: Int): Long = notes[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.grid_item_image, parent, false)
        val imageView: ImageView = view.findViewById(R.id.imageView)

        val note = notes[position]

        Log.d("ImageAdapter", "Image Path: ${note.image}")

        Glide.with(context)
            .load(note.image)
            .error(R.drawable.logo)
            .override(300, 300)
            .fitCenter()
            .into(imageView)

        return view
    }
}
