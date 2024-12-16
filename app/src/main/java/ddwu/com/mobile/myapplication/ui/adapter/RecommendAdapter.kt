package ddwu.com.mobile.myapplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ddwu.com.mobile.myapplication.data.network.Place
import ddwu.com.mobile.myapplication.databinding.ItemRecommendPlaceBinding

class RecommendAdapter(private val places: List<Place>) :
    RecyclerView.Adapter<RecommendAdapter.RecommendViewHolder>() {

    inner class RecommendViewHolder(private val binding: ItemRecommendPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(place: Place) {
            binding.placeName.text = android.text.Html.fromHtml(place.title).toString()
            binding.placeAddress.text = place.address
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendViewHolder {
        val binding = ItemRecommendPlaceBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return RecommendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendViewHolder, position: Int) {
        holder.bind(places[position])
    }

    override fun getItemCount(): Int = places.size
}
