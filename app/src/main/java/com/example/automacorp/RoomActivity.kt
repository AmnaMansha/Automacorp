package com.example.automacorp
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import kotlin.math.round
class RoomActivity : ComponentActivity() {
    private val viewModel: RoomViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get room parameter from intent
        val param = intent.getStringExtra(MainActivity.ROOM_PARAM)
        val roomData = RoomService.findByNameOrId(param)
        val navigateBack: () -> Unit = {
            startActivity(Intent(baseContext, MainActivity::class.java))
        }

        // Initialize the room in ViewModel
        if (roomData != null) {
            viewModel.room = roomData
        } else {
            viewModel.fetchRooms()
        }

        enableEdgeToEdge()

        // Set up the content
        setContent {
            AutomacorpTheme {
                var isRoomListVisible by remember { mutableStateOf(false) }

                Scaffold(
                    topBar = {
                        AutomacorpTopAppBar(
                            title = stringResource(
                                if (viewModel.room != null) R.string.room_detail
                                else R.string.rooms_list
                            ),
                            onRoomIconClick = {
                                isRoomListVisible = !isRoomListVisible
                                navigateBack()
                            }
                        )
                    },
                    floatingActionButton = {
                        RoomUpdateButton(viewModel, baseContext)
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    if (isRoomListVisible) {
                        RoomListScreen(viewModel, Modifier.padding(innerPadding))
                    } else {
                        if (viewModel.room != null) {
                            RoomDetail(viewModel, Modifier.padding(innerPadding))
                        } else {
                            RoomListScreen(viewModel, Modifier.padding(innerPadding))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RoomDetail(viewModel: RoomViewModel, modifier: Modifier = Modifier) {
    val room = viewModel.room
    if (room == null) {
        Text("Room details not available", modifier = Modifier.padding(16.dp))
        return
    }

    Column(modifier = modifier.padding(16.dp)) {
        // Room Name Field
        OutlinedTextField(
            value = room.name,
            onValueChange = { updatedName ->
                viewModel.room = room.copy(name = updatedName)
            },
            label = { Text(text = "Room Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Current Temperature Field
        OutlinedTextField(
            value = room.currentTemperature.toString(),
            onValueChange = { updatedTemp ->
                viewModel.room = room.copy(
                    currentTemperature = updatedTemp.toDoubleOrNull() ?: room.currentTemperature
                )
            },
            label = { Text("Current Temperature") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Target Temperature Field
        OutlinedTextField(
            value = room.targetTemperature.toString(),
            onValueChange = { updatedTemp ->
                viewModel.room = room.copy(
                    targetTemperature = updatedTemp.toDoubleOrNull() ?: room.targetTemperature
                )
            },
            label = { Text("Target Temperature") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Temperature Slider
        Slider(
            value = room.targetTemperature?.toFloat() ?: 18.0f,
            onValueChange = { newTarget ->
                viewModel.room = room.copy(targetTemperature = newTarget.toDouble())
            },
            valueRange = 10f..28f
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display Target Temperature
        Text(
            text = "Target Temperature: ${(room.targetTemperature ?: 18.0)}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun RoomUpdateButton(viewModel: RoomViewModel, context: Context) {
    ExtendedFloatingActionButton(
        onClick = {
            val room = viewModel.room
            if (room != null) {
                // Trigger the room update
                viewModel.updateRoom(room.id, room)

                // Provide feedback to the user
                Toast.makeText(
                    context,
                    "Updating Room ${room.name}...",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    "No room data available to update",
                    Toast.LENGTH_SHORT
                ).show()
            }
        },
        icon = {
            Icon(
                imageVector = Icons.Filled.Done,
                contentDescription = "Update Room"
            )
        },
        text = { Text(text = stringResource(R.string.act_room_save)) }
    )
}

