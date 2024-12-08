package com.example.automacorp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.automacorp.model.RoomDto

@Composable
    fun RoomLisActivity(
        rooms: List<RoomDto>,
        onRoomClick: (RoomDto) -> Unit,
        modifier: Modifier = Modifier
    ) {
        Column(modifier = modifier.fillMaxSize()) {
            if (rooms.isEmpty()) {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No rooms available",
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                // Display rooms in a LazyColumn
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(rooms) { room ->
                        RoomItem(
                            room = room,
                            onClick = { onRoomClick(room) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Room",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Add Room")
            }
        }
    }


@Composable
    fun RoomItem(room: RoomDto, onClick: () -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = room.name, style = MaterialTheme.typography.headlineSmall)
                Text(text = "Current Temperature: ${room.currentTemperature}°C")
                Text(text = "Target Temperature: ${room.targetTemperature}°C")

                Spacer(modifier = Modifier.height(8.dp))



            }
        }
    }
