package com.radixlogos.littlebookstore.dto.error;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ValidationError extends CustomError{
    List<FieldMessage> errors = new ArrayList<>();

    public ValidationError(Instant timeStamp, Integer status, String error, String path) {
        super(timeStamp, status, error, path);
    }

    public List<FieldMessage> getErrors() {
        return errors;
    }

    public void addErrors(String field, String message){
        errors.removeIf(x -> x.field().equals(field));
        errors.add(new FieldMessage(field,message));
    }
}
