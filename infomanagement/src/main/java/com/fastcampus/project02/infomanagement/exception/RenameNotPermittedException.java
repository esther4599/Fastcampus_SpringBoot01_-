package com.fastcampus.project02.infomanagement.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RenameNotPermittedException extends RuntimeException{
    private static final String MESSAGE = "name is different";

    public RenameNotPermittedException(){
        super(MESSAGE);
        log.error(MESSAGE);
    }
}
