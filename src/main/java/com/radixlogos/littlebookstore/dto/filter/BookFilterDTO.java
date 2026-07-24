package com.radixlogos.littlebookstore.dto.filter;

public class BookFilterDTO {

    private String name ;
    private Long editorId;
    private Long genreId;

    public BookFilterDTO(String name, Long editorId, Long genreId) {
        this.name = name;
        this.editorId = editorId;
        this.genreId = genreId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getEditorId() {
        return editorId;
    }

    public void setEditorId(Long editorId) {
        this.editorId = editorId;
    }

    public Long getGenreId() {
        return genreId;
    }

    public void setGenreId(Long genreId) {
        this.genreId = genreId;
    }
}
