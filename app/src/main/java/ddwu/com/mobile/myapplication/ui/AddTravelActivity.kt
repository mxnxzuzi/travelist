package ddwu.com.mobile.myapplication.ui

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import ddwu.com.mobile.myapplication.data.database.AppDatabase
import ddwu.com.mobile.myapplication.data.model.Trip
import ddwu.com.mobile.myapplication.data.repository.TripRepository
import ddwu.com.mobile.myapplication.databinding.ActivityAddTravelBinding
import ddwu.com.mobile.myapplication.ui.viewmodel.TripViewModel
import ddwu.com.mobile.myapplication.ui.viewmodel.TripViewModelFactory

class AddTravelActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityAddTravelBinding.inflate(layoutInflater)
    }
    private val tripViewModel: TripViewModel by viewModels {
        val tripDao = AppDatabase.getDatabase(this).tripDao()
        val repository = TripRepository(tripDao)
        TripViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            finish()
        }

        binding.selectColorButton.setOnClickListener {
            ColorPickerDialog.Builder(this)
                .setTitle("색상 선택")
                .setPreferenceName("ColorPickerDialog")
                .setPositiveButton("확인", ColorEnvelopeListener { envelope, _ ->
                    binding.colorPreview.setBackgroundColor(envelope.color)
                    val hexCode = envelope.hexCode
                })
                .setNegativeButton("취소") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                .attachAlphaSlideBar(true)
                .attachBrightnessSlideBar(true)
                .setBottomSpace(12)
                .show()
        }
        binding.add.setOnClickListener {
            val tripName = binding.tripName.text.toString()
            val startDate = binding.startDate.text.toString()
            val endDate = binding.endDate.text.toString()
            val location = binding.location.text.toString()
            val colorPreview = (binding.colorPreview.background as? ColorDrawable)?.color ?: 0xFFFFFF

            if (tripName.isBlank() || startDate.isBlank() || endDate.isBlank() || location.isBlank()) {
                Toast.makeText(this, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newTrip = Trip(
                name = tripName,
                startDate = startDate,
                endDate = endDate,
                location = location,
                color = colorPreview
            )

            tripViewModel.insertTrip(newTrip)
            Toast.makeText(this, "여행이 추가되었습니다.", Toast.LENGTH_SHORT).show()

            binding.tripName.text.clear()
            binding.startDate.text.clear()
            binding.endDate.text.clear()
            binding.location.text.clear()
            binding.colorPreview.setBackgroundColor(0xD2D2D2)

            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

    }

}


