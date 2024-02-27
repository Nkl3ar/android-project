package com.example.myapplication



import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentShopJobListBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import java.util.UUID



class ShopJobListFragment : Fragment()  {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var currentLat = 0.0
    private var currentLong = 0.0
    private var _binding: FragmentShopJobListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private var smr = ShopManagerRepository.get()

    private val shopJobListViewmodel: ShopJobListViewmodel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopJobListBinding.inflate(inflater, container, false)

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
    private fun showNewShopJob() {
        viewLifecycleOwner.lifecycleScope.launch {
            getLocation()
            var shopManager = smr.getShopManagerLowest()
            var sj = ShopJob(UUID.randomUUID(),"New",currentLat,currentLong,shopManager)
            shopJobListViewmodel.addShopJob(sj)
            findNavController().navigate(ShopJobListFragmentDirections.showShopJobDetail(sj.id))

        }
    }

    private fun changeView() {
            findNavController().navigate(ShopJobListFragmentDirections.changeListView())

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        getLocation()

    }
    override fun onStart() {

        super.onStart()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                shopJobListViewmodel.jobs.collect { jobs ->
                    binding.shopjobRecyclerView.adapter =
                        ShopJobListAdapter(jobs){
                            view.findNavController().navigate(ShopJobListFragmentDirections.showShopJobDetail(it))
                        }
                }
            }
        }
    }
    private fun getLocation()
    {
        if (ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
              ActivityCompat.requestPermissions(requireActivity(),
                  arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100)
            return
        }
        val location = fusedLocationProviderClient.lastLocation
        location.addOnSuccessListener {
            if (it!=null){
                currentLat = it.latitude.toDouble()
                currentLong = it.longitude.toDouble()
                Log.d("MainActivity", it.latitude.toString());
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
