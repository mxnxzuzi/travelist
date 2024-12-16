package ddwu.com.mobile.myapplication.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import ddwu.com.mobile.myapplication.R
import ddwu.com.mobile.myapplication.databinding.ActivityAddTravelBinding
import ddwu.com.mobile.myapplication.databinding.ActivityNoteDetailBinding

class NoteDetailActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityNoteDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            finish()
        }

        val imageView: ImageView = binding.noteImageView
        val contentTextView: TextView = binding.contentTextView
        val locationTextView: TextView = binding.locationTextView
        val weatherTextView: TextView = binding.weatherTextView

        val imagePath = intent.getStringExtra("imagePath")
        val content = intent.getStringExtra("content")
        val location = intent.getStringExtra("location")
        val weather = intent.getStringExtra("weather")

        Glide.with(this)
            .load(imagePath)
            .error(R.drawable.logo)
            .into(imageView)

        contentTextView.text = content
        locationTextView.text = location
        weatherTextView.text = weather
    }
}
