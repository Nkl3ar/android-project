package com.example.myapplication

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.myapplication.databinding.FragmentShopJobDetailBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.UUID


class ShopJobDetailFragment  : Fragment() {

    private var _binding: FragmentShopJobDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val shopManagerRepository = ShopManagerRepository.get()

    private val args: ShopJobDetailFragmentArgs by navArgs()

    private val shopJobDetailViewModel: ShopJobDetailViewModel by viewModels {
        ShopJobDetailViewModelFactory(args.shopJobId)
    }
    private val spinnerArray = ArrayList<UUID>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentShopJobDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


    init {
        GlobalScope.launch {
            shopManagerRepository.getShopManagers().collect {
                for (item in it){
                    spinnerArray.add(item.id)
                }
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinnerArrayAdapter: ArrayAdapter<UUID> =
            ArrayAdapter<UUID>(requireContext(), R.layout.simple_spinner_dropdown_item, spinnerArray)
        spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.apply {
            shopJobName.doOnTextChanged  { text, _, _, _ ->
                shopJobDetailViewModel.updateShopjob { oldSJ ->
                    oldSJ.copy( name = text.toString())
                }
            }
            shopJobLat.doOnTextChanged  { text, _, _, _ ->
                shopJobDetailViewModel.updateShopjob { oldSJ ->
                    oldSJ.copy( gpsLat = text.toString().toDouble())
                }
            }
            shopJobLong.doOnTextChanged  { text, _, _, _ ->
                shopJobDetailViewModel.updateShopjob { oldSJ ->
                    oldSJ.copy( gpsLong = text.toString().toDouble())
                }
            }
            btnDelete.setOnClickListener{
                shopJobDetailViewModel.deleteShopjob()
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            shopManagerSpinner.adapter = spinnerArrayAdapter
            shopManagerSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedUUID = shopManagerSpinner.selectedItem
                    GlobalScope.launch {
                        val selectedSM = shopManagerRepository.getShopManager(selectedUUID as UUID)
                            shopJobDetailViewModel.updateShopjob { oldSJ ->
                             oldSJ.copy( shopManager = selectedSM)
                        }

                    }
                }

            }
        }


        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                shopJobDetailViewModel.shopjob.collect { shopjob ->
                    shopjob?.let { updateUi(it) }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUi(shopJob: ShopJob) {
        binding.apply {
            println(shopJobLong)
            println(shopJobLat)
            if (shopJobName.text.toString() != shopJob.name) {
                shopJobName.setText(shopJob.name)
            }
            if (shopJobLong.text.toString() != shopJob.gpsLong.toString() ) {
                shopJobLong.setText(shopJob.gpsLong.toString())
            }

            if (shopJobLat.text.toString() != shopJob.gpsLat.toString() ) {
                shopJobLat.setText(shopJob.gpsLat.toString())
            }

            if (shopManagerSpinner.selectedItem != shopJob.shopManager.id ) {
                shopManagerSpinner.setSelection(
                    (shopManagerSpinner.getAdapter() as ArrayAdapter<UUID>).getPosition(
                        shopJob.shopManager.id
                    )
                )
            }

        }
    }
}
