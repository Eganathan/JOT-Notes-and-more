package net.eknath.jot.domain.usecase

import net.eknath.jot.domain.model.Note
import net.eknath.jot.domain.repository.NoteRepository
class UpdateNoteUseCase(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(note: Note) {
        noteRepository.update(note)
    }
}