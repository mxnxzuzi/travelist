package ddwu.com.mobile.myapplication.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import ddwu.com.mobile.myapplication.data.database.AppDatabase
import ddwu.com.mobile.myapplication.data.model.Note
import ddwu.com.mobile.myapplication.data.repository.NoteRepository
import ddwu.com.mobile.myapplication.databinding.ActivityAddNoteBinding
import ddwu.com.mobile.myapplication.ui.viewmodel.NoteViewModel
import ddwu.com.mobile.myapplication.ui.viewmodel.NoteViewModelFactory
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddNoteActivity : AppCompatActivity() {

    companion object {
        private const val API_KEY = "AIzaSyBv1JCVtrZhkiyFBiep6b-xptUSq0a4sWQ"
    }

    private lateinit var binding: ActivityAddNoteBinding
    private var selectedLatLng: LatLng? = null
    private var currentPhotoPath: String? = null

    private val noteViewModel: NoteViewModel by viewModels {
        val noteDao = AppDatabase.getDatabase(this).noteDao()
        val tripDao = AppDatabase.getDatabase(this).tripDao()
        val repository = NoteRepository(noteDao, tripDao)
        NoteViewModelFactory(repository)
    }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri: Uri? = result.data?.data
                imageUri?.let {
                    binding.image.setImageURI(it)
                    currentPhotoPath = it.toString()
                }
            }
        }

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess && currentPhotoPath != null) {
                val imageUri = Uri.parse(currentPhotoPath)
                binding.image.setImageURI(imageUri)
            } else {
                Toast.makeText(this, "사진 촬영에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener { finish() }

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, API_KEY)
        }

        val tripName = intent.getStringExtra("tripName") ?: "Unknown Trip"
        val tripId = intent.getIntExtra("tripId", -1)
        val tripColor = intent.getIntExtra("tripColor", 0xFFFFA500.toInt())

        if (tripId == -1) {
            Toast.makeText(this, "유효하지 않은 여행 ID입니다.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.tvTripName.text = tripName

        val autocompleteLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val place = Autocomplete.getPlaceFromIntent(result.data!!)
                    binding.location.setText(place.name)
                    selectedLatLng = place.latLng
                } else {
                    Toast.makeText(this, "위치 검색에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

        binding.location.setOnClickListener {
            val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
            val intent =
                Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
            autocompleteLauncher.launch(intent)
        }

        binding.addNote.setOnClickListener {
            val content = binding.content.text.toString()
            val location = binding.location.text.toString()
            val weather = binding.weather.text.toString()

            if (content.isBlank() || location.isBlank() || selectedLatLng == null || currentPhotoPath == null) {
                Toast.makeText(this, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val note = Note(
                tripId = tripId,
                content = content,
                weather = weather,
                location = location,
                image = currentPhotoPath,
                latitude = selectedLatLng!!.latitude,
                longitude = selectedLatLng!!.longitude
            )

            noteViewModel.insertNoteAndGetId(note, tripId) { noteId ->
                val resultIntent = Intent().apply {
                    putExtra("noteId", noteId)
                    putExtra("location", location)
                    putExtra("latitude", selectedLatLng!!.latitude)
                    putExtra("longitude", selectedLatLng!!.longitude)
                    putExtra("color", tripColor)
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }

        binding.addPicture.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickImageLauncher.launch(intent)
        }

        binding.takePicture.setOnClickListener {
            val photoFile: File? = createImageFile()
            photoFile?.let {
                val photoUri = FileProvider.getUriForFile(
                    this,
                    "${applicationContext.packageName}.fileprovider",
                    it
                )
                currentPhotoPath = it.absolutePath
                takePictureLauncher.launch(photoUri)
            }

        }
    }

    private fun createImageFile(): File? {
        return try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val storageDir = getExternalFilesDir("cache")
            File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
        } catch (e: IOException) {
            Toast.makeText(this, "이미지 파일 생성에 실패했습니다.", Toast.LENGTH_SHORT).show()
            null
        }
    }


}
