package ddwu.com.mobile.myapplication.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ddwu.com.mobile.myapplication.R
import ddwu.com.mobile.myapplication.data.database.AppDatabase
import ddwu.com.mobile.myapplication.data.repository.NoteRepository
import ddwu.com.mobile.myapplication.data.repository.TripRepository
import ddwu.com.mobile.myapplication.databinding.ActivityMainBinding
import ddwu.com.mobile.myapplication.ui.viewmodel.NoteViewModel
import ddwu.com.mobile.myapplication.ui.viewmodel.NoteViewModelFactory
import ddwu.com.mobile.myapplication.ui.viewmodel.TripViewModel
import ddwu.com.mobile.myapplication.ui.viewmodel.TripViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private val markerNoteMap = mutableMapOf<Marker, Int>()

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val tripViewModel: TripViewModel by viewModels {
        val tripDao = AppDatabase.getDatabase(this).tripDao()
        TripViewModelFactory(TripRepository(tripDao))
    }
    private val noteViewModel: NoteViewModel by viewModels {
        val noteDao = AppDatabase.getDatabase(this).noteDao()
        val tripDao = AppDatabase.getDatabase(this).tripDao()
        NoteViewModelFactory(NoteRepository(noteDao, tripDao))
    }

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    companion object {
        private const val REQUEST_ADD_NOTE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.addTravel.setOnClickListener {
            val intent = Intent(this, AddTravelActivity::class.java)
            startActivity(intent)
        }
        binding.memories.setOnClickListener {
            val intent = Intent(this, MemoriesActivity::class.java)
            startActivity(intent)
        }

        setupRecyclerView()
        setupMap()
    }


    private fun setupRecyclerView() {
        val adapter = TripAdapter(
            onDeleteClick = { tripId ->
                showDeleteDialog(tripId)
            },
            onAddMarkerClick = { trip ->
                val intent = Intent(this, AddNoteActivity::class.java)
                intent.putExtra("tripId", trip.id)
                intent.putExtra("tripName", trip.name)
                intent.putExtra("tripColor", trip.color)
                startActivityForResult(intent, REQUEST_ADD_NOTE)
            }
        )
        binding.tripRecyclerView.adapter = adapter
        binding.tripRecyclerView.layoutManager = LinearLayoutManager(this)

        tripViewModel.allTrips.observe(this) { trips ->
            adapter.submitList(trips)
        }
    }

    private fun setupMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        checkLocationPermission()
        loadMarkersForAllTrips()
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            enableMyLocation()
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    enableMyLocation()
                } else {
                    Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                }
            }
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                }
            }
        }
    }

    private fun loadMarkersForAllTrips() {
        tripViewModel.allTrips.observe(this) { trips ->
            noteViewModel.new_notes.observe(this) { notes ->
                mMap.clear()
                markerNoteMap.clear()

                notes.forEach { note ->
                    val tripColor = trips.find { it.id == note.tripId }?.color ?: 0xFFFFA500.toInt()
                    val latLng = LatLng(note.latitude, note.longitude)
                    addMarker(latLng, note.location, tripColor, note.id)
                }
            }
        }
    }

    private fun addMarker(latLng: LatLng, title: String, color: Int, noteId: Int) {
        val marker = mMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(title)
                .icon(BitmapDescriptorFactory.defaultMarker(getMarkerColor(color)))
        )
        marker?.let { markerNoteMap[it] = noteId }
    }

    private fun getMarkerColor(color: Int): Float {
        return if (color < 0) {
            val hsv = FloatArray(3)
            Color.colorToHSV(color, hsv)
            hsv[0]
        } else {
            BitmapDescriptorFactory.HUE_RED
        }
    }


    // trip 삭제시 note & marker 동시 삭제 코드
    private fun showDeleteDialog(tripId: Int) {
        AlertDialog.Builder(this)
            .setTitle("삭제 확인")
            .setMessage("정말로 여행을 삭제하시겠습니까?")
            .setPositiveButton("삭제") { _, _ ->
                lifecycleScope.launch(Dispatchers.IO) {
                    tripViewModel.deleteTrip(tripId)
                    withContext(Dispatchers.Main) {
                        clearMarkersForTrip(tripId)
                        Toast.makeText(this@MainActivity, "여행이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun clearMarkersForTrip(tripId: Int) {
        val markersToRemove = markerNoteMap.filter { it.value == tripId }.keys
        markersToRemove.forEach { marker ->
            marker.remove()
            markerNoteMap.remove(marker)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ADD_NOTE && resultCode == RESULT_OK) {
            val noteId = data?.getLongExtra("noteId", -1L)
            val location = data?.getStringExtra("location")
            val latitude = data?.getDoubleExtra("latitude", 0.0)
            val longitude = data?.getDoubleExtra("longitude", 0.0)
            val color = data?.getIntExtra("color", 0)


            if (latitude != null && longitude != null && color != null) {
                val latLng = LatLng(latitude, longitude)
                addMarker(latLng, location ?: "Unknown", color, noteId?.toInt() ?: -1)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            }
        }
    }




}
