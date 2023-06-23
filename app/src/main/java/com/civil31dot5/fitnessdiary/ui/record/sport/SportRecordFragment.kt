package com.civil31dot5.fitnessdiary.ui.record.sport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.civil31dot5.fitnessdiary.R
import com.civil31dot5.fitnessdiary.domain.model.StravaSportRecord
import com.civil31dot5.fitnessdiary.ui.theme.FitnessDiaryTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class SportRecordFragment : Fragment(), MenuProvider {

    private val viewModel by viewModels<SportRecordViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireActivity()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                FitnessDiaryTheme {
                    SportRecordScreen(viewModel)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(this, viewLifecycleOwner)

        bindViewModel()
    }

    private fun bindViewModel() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.map { it.hasConnectStrava }
                        .distinctUntilChanged()
                        .collect { requireActivity().invalidateMenu() }
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
        when (menuItem.itemId) {
            R.id.connect_strava -> {
                viewModel.connectStrava()
            }

            R.id.disconnect_strava -> {
                viewModel.disconnectStrava()
            }
        }

        return true
    }

}

@Composable
fun SportRecordScreen(viewModel: SportRecordViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    SportRecordContent(uiState.sportHistory)
}

@Composable
fun SportRecordContent(sportHistory: List<StravaSportRecord>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(all = 8.dp)
    ) {
        items(sportHistory, key = { it.id }) { record ->
            SportRecordCard(
                record.dateTime,
                record.type,
                record.calories,
                record.elapsedTimeSec
            )
        }
    }
}

@Preview
@Composable
fun PreviewSportRecordContent() {
    SportRecordContent(
        listOf(
            StravaSportRecord(
                0,
                LocalDateTime.now(),
                "running name",
                13.0,
                800.0,
                "running",
                3600
            ),
            StravaSportRecord(
                1,
                LocalDateTime.now(),
                "evening bike",
                45.0,
                800.0,
                "bike",
                7200
            ),
        )
    )
}

@Composable
fun SportRecordCard(dateTime: LocalDateTime, type: String, calories: Double, elapsedTimeSec: Long) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE))
                Text(text = type.uppercase(), style = MaterialTheme.typography.headlineLarge)
                Text(
                    text = "%.0f卡路里".format(calories),
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Text(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp),
                text = LocalTime.MIN.plusSeconds(elapsedTimeSec)
                    .format(DateTimeFormatter.ISO_LOCAL_TIME)
            )
        }
    }
}

@Preview
@Composable
fun PreviewSportRecordCard() {
    SportRecordCard(
        LocalDateTime.now(),
        "running",
        800.0,
        3600
    )
}