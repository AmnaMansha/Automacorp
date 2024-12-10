package com.example.automacorp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.automacorp.model.RoomDto
import com.example.automacorp.model.WindowDto
import com.example.automacorp.model.WindowStatus
import com.example.automacorp.service.RoomService
import com.example.automacorp.ui.theme.AutomacorpTheme
import com.example.automacorp.viewmodel.RoomViewModel
import java.text.DecimalFormat
import kotlin.math.round

fun formatTemperature(value: Double): String {
    val decimalFormat = DecimalFormat("#.##")
    return decimalFormat.format(value) + "°C"
}
@Composable
fun RoomListScreen(viewModel: RoomViewModel, modifier: Modifier = Modifier,) {

    val rooms by remember { viewModel._rooms } // Observe the mutable state

    Column(modifier = modifier.padding(16.dp)) {
        if (rooms.isEmpty()) {
            Text(text = "No rooms available", style = MaterialTheme.typography.bodyLarge)
        } else {
            LazyColumn {
                items(rooms) { room ->
                    RoomItem(room = room) { selectedRoom ->
                        // Handle room selection (if you want to navigate to the detail screen for a selected room)
                        viewModel.room = selectedRoom
                    }
                }
            }
        }
    }
}

@Composable
fun RoomItem(room: RoomDto, onClick: (RoomDto) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(room) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = room.name ?: "Unnamed Room",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(
                    text = "Current Temp: ${room.currentTemperature?.let { formatTemperature(it) } ?: ""}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Target Temp: ${room.targetTemperature?.let { formatTemperature(it) } ?: ""}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}