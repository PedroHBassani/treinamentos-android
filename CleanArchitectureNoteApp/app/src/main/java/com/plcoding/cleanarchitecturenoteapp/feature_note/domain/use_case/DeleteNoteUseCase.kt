package com.plcoding.cleanarchitecturenoteapp.feature_note.domain.use_case

import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.repository.NoteRepository

class DeleteNoteUseCase(
    private val repository: NoteRepository
){

    suspend operator fun invoke(id: Int){
        val note = repository.getNoteById(id) ?: return
        repository.deleteNote(note)
    }

}