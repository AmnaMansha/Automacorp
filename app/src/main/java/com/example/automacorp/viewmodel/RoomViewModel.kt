package com.example.automacorp.viewmodel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.automacorp.model.RoomDto
import com.example.automacorp.service.RoomService

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
}
