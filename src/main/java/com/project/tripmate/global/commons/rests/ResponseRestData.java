package com.project.tripmate.global.commons.rests;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseRestData<T> {

    private boolean success=true;
    private HttpStatus status = HttpStatus.OK;

    @NonNull
    private T data;
    private String message;


}
