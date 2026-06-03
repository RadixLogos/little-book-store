package com.radixlogos.littlebookstore.dto;

import com.radixlogos.littlebookstore.entities.Client;
import com.radixlogos.littlebookstore.entities.Editor;
import jakarta.validation.constraints.NotEmpty;

public record EditorDTO(
        Long id,
        @NotEmpty(message = "The name is required")
        String name
)
        {
    public static EditorDTO fromEditor(Editor editor) {
        return new EditorDTO(editor.getId(), editor.getName());
    }
    public static Editor fromEditorDTO(EditorDTO editorDTO){
        return  new Editor(editorDTO.id(), editorDTO.name());
    }
        }
