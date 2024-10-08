package com.project.tripmate.config.commons.exceptions;

import org.springframework.http.HttpStatus;

public class CommonException extends RuntimeException {

    private HttpStatus status;


    public CommonException(String message){
        this(message , HttpStatus.INTERNAL_SERVER_ERROR);

    }

    public CommonException(String message , HttpStatus status){
        super(message);
        this.status =status;
    }

    public HttpStatus getStatus (){
        return status;
    }


}
