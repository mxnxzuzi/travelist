package ddwu.com.mobile.myapplication.ui

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import ddwu.com.mobile.myapplication.R
import ddwu.com.mobile.myapplication.data.database.AppDatabase
import ddwu.com.mobile.myapplication.data.repository.NoteRepository
import ddwu.com.mobile.myapplication.databinding.ActivityNoteDetailBinding
import ddwu.com.mobile.myapplication.ui.viewmodel.NoteViewModel
import ddwu.com.mobile.myapplication.ui.viewmodel.NoteViewModelFactory

class NoteDetailActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityNoteDetailBinding.inflate(layoutInflater)
    }
    private val noteViewModel: NoteViewModel by viewModels {
        val noteDao = AppDatabase.getDatabase(this).noteDao()
        val tripDao = AppDatabase.getDatabase(this).tripDao()
        NoteViewModelFactory(NoteRepository(noteDao, tripDao))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val noteId = intent.getIntExtra("noteId", -1)
        val imagePath = intent.getStringExtra("imagePath")
        val content = intent.getStringExtra("content")
        val location = intent.getStringExtra("location")
        val weather = intent.getStringExtra("weather")

        binding.back.setOnClickListener {
            finish()
        }

        binding.delete.setOnClickListener {
            Log.d("NoteDetailActivity", "Deleting Note ID: $noteId")
            noteViewModel.deleteNoteById(noteId)
            setResult(RESULT_OK)
            finish()
        }



        val imageView: ImageView = binding.noteImageView
        val contentTextView: TextView = binding.contentTextView
        val locationTextView: TextView = binding.locationTextView
        val weatherTextView: TextView = binding.weatherTextView



        Glide.with(this)
            .load(imagePath)
            .error(R.drawable.logo)
            .into(imageView)

        contentTextView.text = content
        locationTextView.text = location
        weatherTextView.text = weather
    }
}
