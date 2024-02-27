package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ListItemShopJobBinding
import java.util.UUID

class ShopJobHolder(
    private val binding: ListItemShopJobBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(shopJob: ShopJob,onShopJobClicked: (shopJobID: UUID) -> Unit) {
        binding.jobTitle.text = shopJob.name
        binding.jobDesc.text = shopJob.shopManager.toString()

        binding.root.setOnClickListener {
            onShopJobClicked(shopJob.id)
        }

    }
}

class ShopJobListAdapter(
    private val shopJobs: List<ShopJob>,
    private val onShopJobClicked: (shopJobID: UUID) -> Unit
) : RecyclerView.Adapter<ShopJobHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShopJobHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemShopJobBinding.inflate(inflater, parent, false)
        return ShopJobHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopJobHolder, position: Int) {
        val shopJob = shopJobs[position]
        holder.bind(shopJob,onShopJobClicked)
    }

    override fun getItemCount() = shopJobs.size
}
