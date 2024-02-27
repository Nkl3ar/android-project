package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentShopManagerListBinding
import kotlinx.coroutines.launch
import java.util.UUID


class ShopManagerListFragment : Fragment() {

    private var _binding: FragmentShopManagerListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val shopManagerListViewmodel: ShopManagerListViewmodel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopManagerListBinding.inflate(inflater, container, false)

        binding.shopjobRecyclerView.layoutManager = LinearLayoutManager(context)

        return binding.root
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_shop_job_list, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_shop_job -> {
                showNewShopJob()
                true
            }
            R.id.change_list_view -> {
                changeView()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }
    private fun changeView() {
        findNavController().navigate(ShopManagerListFragmentDirections.changeListView())

    }
    private fun showNewShopJob() {
        viewLifecycleOwner.lifecycleScope.launch {
            var shopManager = ShopManager(UUID.randomUUID(),"")
            shopManagerListViewmodel.addShopManager(shopManager)
            findNavController().navigate(ShopManagerListFragmentDirections.showShopJobDetail(shopManager.id))


        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onStart() {

        super.onStart()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                shopManagerListViewmodel.shopManagers.collect { shopManagers ->
                    binding.shopjobRecyclerView.adapter =
                        ShopManagerListAdapter(shopManagers){
                            view.findNavController().navigate(ShopManagerListFragmentDirections.showShopJobDetail(it))
                        }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
