package com.br.api.library.api.exceptions;

import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

public class ApiErrors {

    private List<String> errors;

    public ApiErrors(BindingResult bindingResult) {
        this.errors = new ArrayList<>();

        bindingResult.getAllErrors().forEach(errors -> this.errors.add(errors.getDefaultMessage()));
    }

    public List<String> getErrors() {
        return errors;
    }
}
