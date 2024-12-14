package ddwu.com.mobile.myapplication.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
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
import com.google.android.gms.maps.model.LatLng
import ddwu.com.mobile.myapplication.R
import ddwu.com.mobile.myapplication.data.database.AppDatabase
import ddwu.com.mobile.myapplication.data.model.Trip
import ddwu.com.mobile.myapplication.data.repository.TripRepository
import ddwu.com.mobile.myapplication.databinding.ActivityMainBinding
import ddwu.com.mobile.myapplication.ui.viewmodel.TripViewModel
import ddwu.com.mobile.myapplication.ui.viewmodel.TripViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val tripViewModel: TripViewModel by viewModels {
        val tripDao = AppDatabase.getDatabase(this).tripDao()
        val repository = TripRepository(tripDao)
        TripViewModelFactory(repository)
    }

    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //insertDummyTrips()

        // 여행 추가
        binding.addTravel.setOnClickListener {
            val intent = Intent(this, AddTravelActivity::class.java)
            startActivity(intent)
        }

        // Trip 표시
        val adapter = TripAdapter { tripId ->
            val builder = AlertDialog.Builder(this)
            builder.setTitle("삭제 확인")
                .setMessage("정말로 여행을 삭제하시겠습니까?")
                .setPositiveButton("삭제") { _, _ ->
                    tripViewModel.deleteTrip(tripId)
                    Toast.makeText(this, "여행이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("취소") { dialog, _ ->
                    dialog.dismiss()
                }
            builder.create().show()
        }

        binding.tripRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.tripRecyclerView.adapter = adapter

        tripViewModel.allTrips.observe(this) { trips ->
            adapter.submitList(trips)
        }



        // 위치 정보 가져오기
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        requestLocationPermission()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enableUserLocation()
            moveToCurrentLocation()
        } else {
            requestLocationPermission()
        }
    }

    private fun enableUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        }
    }

    private fun moveToCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f)) // 줌 레벨 15
                } else {
                    Toast.makeText(this, "현재 위치를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(this, "위치 정보를 가져오는 데 실패했습니다: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            // 위치 권한이 없을 경우 권한 요청
            Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestLocationPermission() {
        val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                enableUserLocation()
                moveToCurrentLocation()
            } else {
                Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
    private fun insertDummyTrips() {
        val dummyTrips = listOf(
            Trip(name = "가족여행 - 제주도", startDate = "2024-01-10", endDate = "2024-01-15", location = "제주도", color = 0xFFFFA07A.toInt()),
            Trip(name = "우정여행 - 부산", startDate = "2024-02-01", endDate = "2024-02-05", location = "부산", color = 0xFF98FB98.toInt()),
            Trip(name = "혼자여행 - 서울", startDate = "2024-03-15", endDate = "2024-03-18", location = "서울", color = 0xFFADD8E6.toInt()),
            Trip(name = "회사 워크샵", startDate = "2024-04-10", endDate = "2024-04-12", location = "강원도 평창", color = 0xFFFFD700.toInt()),
            Trip(name = "가을여행 - 경주", startDate = "2024-10-01", endDate = "2024-10-05", location = "경주", color = 0xFFFF69B4.toInt())
        )

        lifecycleScope.launch(Dispatchers.IO) { // Dispatchers.IO: 백그라운드 스레드
            dummyTrips.forEach { trip ->
                val tripDao = AppDatabase.getDatabase(this@MainActivity).tripDao() // TripDao 초기화
                tripDao.insertTrip(trip) // Trip 데이터 삽입
            }
        }
    }

}
