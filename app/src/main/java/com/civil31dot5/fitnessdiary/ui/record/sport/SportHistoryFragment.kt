package com.civil31dot5.fitnessdiary.ui.record.sport

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.civil31dot5.fitnessdiary.R
import com.civil31dot5.fitnessdiary.databinding.FragmentSportHistoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SportHistoryFragment : Fragment(), MenuProvider {

    private var _binding: FragmentSportHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SportHistoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSportHistoryBinding.inflate(layoutInflater)
        initView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
    }

    private fun initView() {
        requireActivity().addMenuProvider(this, viewLifecycleOwner)
        binding.rvSport.adapter = SportHistoryAdapter()
    }

    private fun bindViewModel() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){

                launch {
                    viewModel.uiState.map { it.hasConnectStrava }
                        .distinctUntilChanged()
                        .collect{ requireActivity().invalidateMenu() }
                }

                launch {
                    viewModel.uiState.collect{ uiState ->
                        (binding.rvSport.adapter as? SportHistoryAdapter)?.submitList(uiState.sportHistory)
                    }
                }

            }
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.sport_history_menu, menu)
    }

    override fun onPrepareMenu(menu: Menu) {
        val connectStravaMenu = menu.findItem(R.id.connect_strava)
        connectStravaMenu.isVisible = viewModel.uiState.value.hasConnectStrava == false
        val disconnectStravaMenu = menu.findItem(R.id.disconnect_strava)
        disconnectStravaMenu.isVisible = viewModel.uiState.value.hasConnectStrava == true
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId){
            R.id.connect_strava -> {
                viewModel.connectStrava()
            }
            R.id.disconnect_strava -> {
                viewModel.disconnectStrava()
            }
        }

        return true
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}