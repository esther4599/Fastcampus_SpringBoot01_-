package com.fastcampus.project02.infomanagement.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonNotFoundException extends RuntimeException{
    private static final String MESSAGE = "entity is not found";

    public PersonNotFoundException(){
        super(MESSAGE);
        log.error(MESSAGE);
    }
}
