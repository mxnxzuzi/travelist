package ddwu.com.mobile.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.map
import ddwu.com.mobile.myapplication.R
import ddwu.com.mobile.myapplication.data.database.AppDatabase
import ddwu.com.mobile.myapplication.data.model.Note
import ddwu.com.mobile.myapplication.data.model.Trip
import ddwu.com.mobile.myapplication.databinding.ActivityAddNoteBinding
import ddwu.com.mobile.myapplication.databinding.ActivityAddTravelBinding
import ddwu.com.mobile.myapplication.databinding.ActivityMemoriesBinding
import ddwu.com.mobile.myapplication.ui.adapter.ImageAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MemoriesActivity : AppCompatActivity() {

    private lateinit var photoGridView: GridView
    private lateinit var tripFilterSpinner: Spinner
    private var notes: List<Note> = listOf()
    private var filteredNotes: List<Note> = listOf()
    private val binding by lazy {
        ActivityMemoriesBinding.inflate(layoutInflater)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            finish()
        }

        photoGridView = findViewById(R.id.photoGridView)
        tripFilterSpinner = findViewById(R.id.tripFilterSpinner)

        loadNotesAndTrips()
    }

    private fun loadNotesAndTrips() {
        val noteDao = AppDatabase.getDatabase(this).noteDao()
        val tripDao = AppDatabase.getDatabase(this).tripDao()

        noteDao.getAllNotesLiveData().observe(this) { noteList ->
            notes = noteList
            Log.d("MemoriesActivity", "Notes: $noteList")
            tripDao.getAllTrips().observe(this) { trips ->
                val tripNames = listOf("전체") + trips.map { it.name ?: "이름 없음" }
                setupTripFilter(tripNames, trips)
                displayPhotos(notes)
            }
        }
    }


    private fun setupTripFilter(tripNames: List<String>, trips: List<Trip>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tripNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        tripFilterSpinner.adapter = adapter

        tripFilterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedTrip = if (position == 0) null else trips[position - 1].id
                filteredNotes = if (selectedTrip == null) notes else notes.filter { it.tripId == selectedTrip }
                displayPhotos(filteredNotes)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                displayPhotos(notes)
            }
        }
    }

    private fun displayPhotos(notes: List<Note>) {
        val imageAdapter = ImageAdapter(this, notes)
        photoGridView.adapter = imageAdapter

        photoGridView.setOnItemClickListener { _, _, position, _ ->
            val note = notes[position]
            val intent = Intent(this, NoteDetailActivity::class.java).apply {
                putExtra("noteId", note.id)
            }
            startActivity(intent)
        }
    }
}
