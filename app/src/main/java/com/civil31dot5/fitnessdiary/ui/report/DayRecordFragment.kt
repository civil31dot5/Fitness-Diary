package com.civil31dot5.fitnessdiary.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.navArgs
import com.civil31dot5.fitnessdiary.domain.model.BodyShapeRecord
import com.civil31dot5.fitnessdiary.domain.model.DietRecord
import com.civil31dot5.fitnessdiary.domain.model.StravaSportRecord
import com.civil31dot5.fitnessdiary.ui.record.bodyshape.BodyShapeRecordCard
import com.civil31dot5.fitnessdiary.ui.record.diet.DietRecordCard
import com.civil31dot5.fitnessdiary.ui.record.sport.SportRecordCard
import com.civil31dot5.fitnessdiary.ui.theme.FitnessDiaryTheme
import dagger.hilt.android.AndroidEntryPoint
import java.time.format.DateTimeFormatter


@AndroidEntryPoint
class DayRecordFragment : Fragment() {

    private val viewModel: DayRecordViewModel by viewModels()

    private val arg: DayRecordFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireActivity()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                FitnessDiaryTheme {
                    DayRecordScreen(viewModel)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setDate(arg.date)
    }

    @Composable
    fun DayRecordScreen(viewModel: DayRecordViewModel) {
        val dayRecords by viewModel.selectedDateRecords.collectAsStateWithLifecycle()
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item {
                Text(
                    text = arg.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            items(
                count = dayRecords.size,
                key = { index -> dayRecords[index].id },
                contentType = { index -> dayRecords[index].javaClass }
            ) { index ->
                val record = dayRecords[index]
                when (record) {
                    is DietRecord -> {
                        DietRecordCard(record = record)
                    }

                    is StravaSportRecord -> {
                        SportRecordCard(
                            record.dateTime,
                            record.type,
                            record.calories,
                            record.elapsedTimeSec
                        )
                    }

                    is BodyShapeRecord -> {
                        BodyShapeRecordCard(record = record)
                    }

                    else -> {}
                }
            }
        }
    }

}