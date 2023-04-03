package com.civil31dot5.fitnessdiary.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.civil31dot5.fitnessdiary.NavGraphDirections
import com.civil31dot5.fitnessdiary.data.network.StravaApi
import com.civil31dot5.fitnessdiary.databinding.FragmentHomeBinding
import com.civil31dot5.fitnessdiary.domain.usecase.sport.GetStravaSportHistoryUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var stravaApi: StravaApi


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        initView()
        initListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
    }

    private fun initView() {

    }

    private fun initListener() {
        binding.btAdd.setOnClickListener {
            findNavController().navigate(NavGraphDirections.actionGlobalAddDietRecordFragment())
        }

        binding.btTest.setOnClickListener {
            lifecycleScope.launch {


            }
        }

    }

    private fun bindViewModel() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                    viewModel.uiState.collect{ uiState ->
                        uiState.count?.let {  count ->
                            binding.tvCount.text = "count:$count"
                        } ?: kotlin.run {
                            binding.tvCount.text = "count:--"
                        }
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