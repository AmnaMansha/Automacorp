package com.example.automacorp.viewmodel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automacorp.service.ApiServices
import com.example.automacorp.model.RoomCommandDto
import com.example.automacorp.model.RoomDto
import com.example.automacorp.service.RoomService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//class RoomViewModel: ViewModel() {
//
//    var room by mutableStateOf<RoomDto?>(null)
//
//}
class RoomViewModel : ViewModel() {
    var room by mutableStateOf<RoomDto?>(null)
     val _rooms = mutableStateOf<List<RoomDto>>(emptyList())

    // Fetch all rooms
    fun fetchRooms() {
        _rooms.value = RoomService.findAll()
    }
//    fun updateRoom(id: Long, roomDto: RoomDto) {
//        val command = RoomCommandDto(
//            name = roomDto.name,
//            targetTemperature = roomDto.targetTemperature ?.let { Math.round(it * 10) /10.0 },
//            currentTemperature = roomDto.currentTemperature,
//        )
//        viewModelScope.launch(context = Dispatchers.IO) {
//            runCatching { ApiServices.roomsApiService.updateRoom(id, command).execute() }
//                .onSuccess {
//                    val room = it.body()
//                }
//                .onFailure {
//                    it.printStackTrace()
//                    val room = null
//                }
//        }
//    }
fun updateRoom(id: Long, roomDto: RoomDto) {
    viewModelScope.launch(Dispatchers.IO) {
        val updatedRoom = RoomService.updateRoom(id, roomDto)
        if (updatedRoom != null) {
            // Update _rooms list with the updated room
            _rooms.value = _rooms.value.map { room ->
                if (room.id == updatedRoom.id) updatedRoom else room
            }
        }
    }
}
}
