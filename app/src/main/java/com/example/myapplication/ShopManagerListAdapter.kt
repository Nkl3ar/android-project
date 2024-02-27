package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ListItemShopJobBinding
import java.util.UUID

class ShopManagerHolder(
    private val binding: ListItemShopJobBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(shopManager: ShopManager,onShopManagerClicked: (shopManId: UUID) -> Unit) {
        binding.jobTitle.text = shopManager.name
        binding.jobDesc.text = shopManager.id.toString()

        binding.root.setOnClickListener {
            onShopManagerClicked(shopManager.id)
        }

    }
}

class ShopManagerListAdapter(
    private val shopManagers: List<ShopManager>,
    private val onShopManagerClicked: (shopManId: UUID) -> Unit
) : RecyclerView.Adapter<ShopManagerHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShopManagerHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemShopJobBinding.inflate(inflater, parent, false)
        return ShopManagerHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopManagerHolder, position: Int) {
        val shopManagers = shopManagers[position]
        holder.bind(shopManagers,onShopManagerClicked)
    }

    override fun getItemCount() = shopManagers.size
}
