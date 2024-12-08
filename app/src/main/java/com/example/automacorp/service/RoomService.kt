package com.example.automacorp.service

import androidx.core.text.isDigitsOnly
import com.example.automacorp.model.RoomDto
import com.example.automacorp.model.WindowDto
import com.example.automacorp.model.WindowStatus

object RoomService {
    val ROOM_KIND: List<String> = listOf("Room", "Meeting", "Laboratory", "Office", "Boardroom")
    val ROOM_NUMBER: List<Char> = ('A'..'Z').toList()
    val WINDOW_KIND: List<String> = listOf("Sliding", "Bay", "Casement", "Hung", "Fixed")

    fun generateWindow(id: Long, roomId: Long, roomName: String): WindowDto {
        return WindowDto(
            id = id,
            name = "${ WINDOW_KIND.random()} Window $id",
            roomName = roomName,
            roomId = roomId,
            windowStatus = WindowStatus.values().random()
        )
    }

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

    // Create 2 rooms
    val ROOMS = (1..50).map { generateRoom(it.toLong()) }.toMutableList()

    fun findAll(): List<RoomDto> {
        return ROOMS.sortedBy { it.name }
    }

    fun findById(id: Long): RoomDto? {
        return ROOMS.find { it.id == id }
    }

    fun findByName(name: String): RoomDto? {
        return ROOMS.find { it.name.equals(name, ignoreCase = true) }
    }

    fun updateRoom(id: Long, room: RoomDto): RoomDto? {
        val index = ROOMS.indexOfFirst { it.id == id }

        // If room exists in the list, update it; otherwise, throw an exception
        return if (index != -1) {
            // Create a new room with updated values and replace the old one
            val updatedRoom = room.copy(
                id = id,  // Maintain the original room ID
                currentTemperature = room.currentTemperature,
                targetTemperature = room.targetTemperature,
                windows = room.windows
            )
            // Replace the existing room at the index
            ROOMS[index] = updatedRoom  // This is the array-like assignment to update the room
            updatedRoom
        } else {
            throw IllegalArgumentException("Room with ID $id not found")
        }
    }
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