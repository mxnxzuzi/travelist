package ddwu.com.mobile.myapplication.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ddwu.com.mobile.myapplication.data.model.Trip
import ddwu.com.mobile.myapplication.databinding.ItemTripBinding

class TripAdapter(private val onDeleteClick: (Int) -> Unit,
                  private val onAddMarkerClick: (Trip) -> Unit
    ) : ListAdapter<Trip, TripAdapter.TripViewHolder>(TripDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val binding = ItemTripBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TripViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip = getItem(position)
        holder.bind(trip)
        holder.binding.delete.setOnClickListener {
            onDeleteClick(trip.id)
        }
        holder.binding.addMarker.setOnClickListener {
            onAddMarkerClick(trip)
        }
    }

    class TripViewHolder(val binding: ItemTripBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(trip: Trip) {
            binding.tvTripName.text = trip.name
            binding.tvTripDates.text = "${trip.startDate} ~ ${trip.endDate}"
            binding.tvTripLocation.text = trip.location
            binding.vTripColor.setBackgroundColor(trip.color)
        }
    }

    class TripDiffCallback : DiffUtil.ItemCallback<Trip>() {
        override fun areItemsTheSame(oldItem: Trip, newItem: Trip): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Trip, newItem: Trip): Boolean = oldItem == newItem
    }
}
