package com.example.automacorp
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val param = intent.getStringExtra(MainActivity.ROOM_PARAM)
        val roomdata = RoomService.findByNameOrId(param)
        val navigateBack: () -> Unit = {
            startActivity(Intent(baseContext, MainActivity::class.java))
        }
        Log.d("roomdata", roomdata.toString())
        val viewModel: RoomViewModel by viewModels()
        viewModel.room = roomdata
        val onRoomSave: () -> Unit = {
            roomdata?.let { roomDto ->
                try {
                    RoomService.updateRoom(roomDto.id, roomDto)
                    Toast.makeText(baseContext, "Room ${roomDto.name} was updated successfully", Toast.LENGTH_LONG).show()
                    startActivity(Intent(baseContext, MainActivity::class.java))
                    finish() // Close current activity
                } catch (e: Exception) {
                    Toast.makeText(baseContext, "Failed to update room: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } ?: run {
                Toast.makeText(baseContext, "No room data available to update", Toast.LENGTH_SHORT).show()
            }
        }

        enableEdgeToEdge()
        setContent {
            AutomacorpTheme {
                Scaffold(
                    topBar = { AutomacorpTopAppBar("Room", navigateBack) { finish() } },
                    floatingActionButton = { RoomUpdateButton(onRoomSave) },
                    modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if ( viewModel.room!= null) {
                        Toast.makeText(this, "Havr Rooms!", Toast.LENGTH_SHORT).show()

                        RoomDetail(viewModel,Modifier.padding(innerPadding))
                    } else {
                        NoRoom(Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}

@Composable
fun RoomDetail(model: RoomViewModel, modifier: Modifier = Modifier) {
    val room = model.room
    if (room == null) {
        Text("Room details not available", modifier = Modifier.padding(16.dp))
        return
    }
    Column(modifier = modifier.padding(16.dp)) {

        OutlinedTextField(
            value = room.name,
            onValueChange = { model.room = room.copy(name = it) },
            label = { Text(text = "Room Name") },
            modifier = Modifier.fillMaxWidth(),


            )
        Spacer(modifier = Modifier.height(16.dp))

        // Current Temperature
        OutlinedTextField(
            value = room.currentTemperature.toString(),
            onValueChange = {
                model.room = room.copy(
                    currentTemperature = it.toDoubleOrNull() ?: room.currentTemperature
                )
            },
            label = { Text("Current Temperature") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Target Temperature
        OutlinedTextField(
            value = room.targetTemperature.toString(),
            onValueChange = {
                model.room = room.copy(
                    targetTemperature = it.toDoubleOrNull() ?: room.targetTemperature
                )
            },
            label = { Text("Target Temperature") },
            modifier = Modifier.fillMaxWidth()
        )
        // Slider to adjust target temperature
        Slider(
            value = room.targetTemperature?.toFloat() ?: 18.0f,
            onValueChange = { model.room = room.copy(targetTemperature = it.toDouble()) },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            steps = 0,
            valueRange = 10f..28f // Adjust the range as needed
        )

        // Display the target temperature value as text
        Text(text = (round((room.targetTemperature ?: 18.0) * 10) / 10).toString())
    }


}

@Preview(showBackground = true)
@Composable
fun RoomDetailPreview() {
    val sampleRoom = RoomDto(
        id = 1L,
        name = "RoomA",
        currentTemperature = 22.0,
        targetTemperature = 20.0,
        windows = listOf(
            WindowDto(
                id = 1L,
                name = "Sliding Window",
                roomName = "RoomA",
                roomId = 1L,
                windowStatus = WindowStatus.CLOSED
            ),
            WindowDto(
                id = 2L,
                name = "Casement Window",
                roomName = "RoomA",
                roomId = 1L,
                windowStatus = WindowStatus.OPEN
            )
        )
    )
    val viewModel = RoomViewModel()
    viewModel.room = sampleRoom
    // Pass the mocked viewModel to the RoomDetail composable
    RoomDetail(model = viewModel)
}
@Composable
fun NoRoom(modifier: Modifier = Modifier) {
    Log.d("roomdata","lll")
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.act_room_none),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
    }
}
@Composable
fun RoomUpdateButton(onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = { onClick() },
        icon = {
            Icon(
                Icons.Filled.Done,
                contentDescription = stringResource(R.string.act_room_save),
            )
        },
        text = { Text(text = stringResource(R.string.act_room_save)) }
    )
}