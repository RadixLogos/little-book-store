package com.radixlogos.littlebookstore.services;

import com.radixlogos.littlebookstore.dto.EditorDTO;
import com.radixlogos.littlebookstore.entities.Editor;
import com.radixlogos.littlebookstore.repositories.EditorRepository;
import com.radixlogos.littlebookstore.services.exceptions.DatabaseException;
import com.radixlogos.littlebookstore.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EditorService {
    @Autowired
    private EditorRepository editorRepository;

    @Transactional
    public Page<EditorDTO> findAllEditors(Pageable pageable){
        return editorRepository.findAllEditors(pageable).map(EditorDTO::fromEditor);
    }

    @Transactional
    public EditorDTO findEditorById(Long id){
        return EditorDTO
                .fromEditor(editorRepository.findById(id)
                        .orElseThrow(()->new ResourceNotFoundException("Editore não encontrado")));
    }
    @Transactional
    public EditorDTO insertEditor(EditorDTO editorDTO){
        var editorEntity = new Editor();
        copyDtoToEntity(editorDTO,editorEntity);
        editorEntity = editorRepository.save(editorEntity);
        return EditorDTO.fromEditor(editorEntity);
    }

    @Transactional
    public EditorDTO updateEditor(Long id, EditorDTO editorDTO){
        if(!editorRepository.existsById(id)){
            throw new ResourceNotFoundException("Editore não encontrado");
        }
        var editorEntity = editorRepository.getReferenceById(id);
        copyDtoToEntity(editorDTO,editorEntity);
        editorEntity = editorRepository.save(editorEntity);
        return EditorDTO.fromEditor(editorEntity);
    }

    @Transactional
    public void deleteEditor(Long id){
        if (!editorRepository.existsById(id))
            throw new ResourceNotFoundException("Editore não encontrado");
        try{
            editorRepository.deleteById(id);
        } catch (Exception e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }
    private void copyDtoToEntity(EditorDTO editorDTO, Editor editorEntity) {
        editorEntity.setName(editorDTO.name());
    }
}
