package ddwu.com.mobile.myapplication.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ddwu.com.mobile.myapplication.data.network.RetrofitInstance
import ddwu.com.mobile.myapplication.databinding.ActivityRecommendBinding
import ddwu.com.mobile.myapplication.ui.adapter.RecommendAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecommendActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecommendBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            finish()
        }

        binding.recommendRecyclerView.layoutManager = LinearLayoutManager(this)

        binding.searchButton.setOnClickListener {
            val searchQuery = binding.searchEditText.text.toString()
            if (searchQuery.isNotBlank()) {
                fetchRecommendedPlaces(searchQuery)
            } else {
                Toast.makeText(this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchRecommendedPlaces(query: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                Log.d("RecommendActivity", "API 호출 시작: query=$query")
                val response = RetrofitInstance.api.searchLocal(
                    query = query,
                    display = 20,
                    start = 1,
                    sort = "random"
                )

                if (response.isSuccessful) {
                    val places = response.body()?.items
                    places?.let {
                        Log.d("RecommendActivity", "추천 장소 수: ${it.size}")
                        val adapter = RecommendAdapter(it)
                        binding.recommendRecyclerView.adapter = adapter
                    } ?: Log.e("RecommendActivity", "API 응답 body가 null입니다.")
                } else {
                    Log.e("RecommendActivity", "Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("RecommendActivity", "Exception: ${e.message}")
            }
        }
    }
}
