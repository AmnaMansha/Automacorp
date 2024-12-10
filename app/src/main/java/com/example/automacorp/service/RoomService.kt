package com.example.automacorp.service

import androidx.core.text.isDigitsOnly
import com.automacorp.service.ApiServices
import com.example.automacorp.model.RoomCommandDto
import com.example.automacorp.model.RoomDto
import com.example.automacorp.model.WindowDto
import com.example.automacorp.model.WindowStatus
object RoomService {
    val ROOM_KIND: List<String> = listOf("Room", "Meeting", "Laboratory", "Office", "Boardroom")
    val ROOM_NUMBER: List<Char> = ('A'..'Z').toList()
    val WINDOW_KIND: List<String> = listOf("Sliding", "Bay", "Casement", "Hung", "Fixed")

    // Generate a window with random details
    fun generateWindow(id: Long, roomId: Long, roomName: String): WindowDto {
        return WindowDto(
            id = id,
            name = "${WINDOW_KIND.random()} Window $id",
            roomName = roomName,
            roomId = roomId,
            windowStatus = WindowStatus.values().random()
        )
    }

    // Generate a room with random details and associated windows
    fun generateRoom(id: Long): RoomDto {
        val roomName = "${ROOM_NUMBER.random()}$id ${ROOM_KIND.random()}"
        val windows = (1..(1..6).random()).map { generateWindow(it.toLong(), id, roomName) }
        return RoomDto(
            id = id,
            name = roomName,
            currentTemperature = (15..30).random().toDouble(),
            targetTemperature = (15..22).random().toDouble(),
            windows = windows
        )
    }

    // Pre-generate 50 rooms for demonstration purposes
    val ROOMS = (1..50).map { generateRoom(it.toLong()) }.toMutableList()

    // Fetch all rooms
    fun findAll(): List<RoomDto> {
        return try {
            val response = ApiServices.roomsApiService.findAll().execute()
            if (response.isSuccessful) {
                response.body()?.sortedBy { it.name } ?: ROOMS
            } else {
                ROOMS
            }
        } catch (e: Exception) {
            println("Error fetching rooms from API: ${e.message}")
            ROOMS
        }
    }

    // Fetch room by ID
    fun findById(id: Long): RoomDto? {
        return try {
            val response = ApiServices.roomsApiService.findById(id).execute()
            if (response.isSuccessful) {
                response.body()
            } else {
                ROOMS.find { it.id == id }
            }
        } catch (e: Exception) {
            println("Error fetching room by ID from API: ${e.message}")
            ROOMS.find { it.id == id }
        }
    }

    // Fetch room by name
    fun findByName(name: String): RoomDto? {
        return ROOMS.find { it.name.equals(name, ignoreCase = true) }
    }

    // Update room details both locally and via API
    fun updateRoom(id: Long, room: RoomDto): RoomDto? {
        return try {
            val commandDto = RoomCommandDto(

                name = room.name,
                currentTemperature = room.currentTemperature,
                targetTemperature = room.targetTemperature,

            )
            val response = ApiServices.roomsApiService.updateRoom(id, commandDto).execute()
            if (response.isSuccessful) {
                val updatedRoom = response.body()
                updatedRoom?.let {
                    val index = ROOMS.indexOfFirst { it.id == id }
                    if (index != -1) {
                        ROOMS[index] = it
                    }
                }
                updatedRoom
            } else {
                throw IllegalArgumentException("Failed to update room via API: ${response.message()}")
            }
        } catch (e: Exception) {
            println("Error updating room via API: ${e.message}")
            val index = ROOMS.indexOfFirst { it.id == id }
            if (index != -1) {
                ROOMS[index] = room
                room
            } else {
                throw IllegalArgumentException("Room with ID $id not found locally")
            }
        }
    }


    // Fetch room by name or ID
    fun findByNameOrId(nameOrId: String?): RoomDto? {
        if (nameOrId != null) {
            return if (nameOrId.isDigitsOnly()) {
                findById(nameOrId.toLong())
            } else {
                findByName(nameOrId)
            }
        }
        return null
    }
}
