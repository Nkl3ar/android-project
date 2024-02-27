package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.myapplication.databinding.FragmentShopManagerDetailBinding
import kotlinx.coroutines.launch
import java.util.UUID


class ShopManagerDetailFragment  : Fragment() {

    private var _binding: FragmentShopManagerDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }


    private val args: ShopJobDetailFragmentArgs by navArgs()

    private val shopManagerDetailViewModel: ShopManagerDetailViewModel by viewModels {
        ShopManagerDetailViewModelFactory(args.shopJobId)
    }
    private val spinnerArray = ArrayList<UUID>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentShopManagerDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            shopManName.doOnTextChanged  { text, _, _, _ ->
                shopManagerDetailViewModel.updateShopManager { oldSM ->
                    oldSM.copy( name = text.toString())
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                shopManagerDetailViewModel.shopmanager.collect { shopManager ->
                    shopManager?.let { updateUi(it) }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUi(shopManager: ShopManager) {
        binding.apply {
            if (shopManName.text.toString() != shopManager.name) {
                shopManName.setText(shopManager.name)
            }

        }
    }
}
