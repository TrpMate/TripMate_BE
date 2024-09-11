package com.project.tripmate.global.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.tripmate.global.commons.utils.MessageUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommonRestException extends CommonException {
    public CommonRestException(Errors errors , HttpStatus status){
        super(toJSON(errors) , status);
    }

    public static String toJSON(Errors errors){
        List<String[]> list = errors.getFieldErrors()
                .stream()
                .map(e -> new String[]{e.getField() , MessageUtils.getMessage(e.getCodes()[0])})
                .toList();

        ObjectMapper om =new ObjectMapper();
        om.registerModule(new JavaTimeModule());

        try {
            return om.writeValueAsString(list);
        }catch (JsonProcessingException e){
        }
        return null;
    }


    private static String getErrorMessages(String[] codes){
        String message = Arrays.stream(codes)
                .map(c -> MessageUtils.getMessage(c)).filter(s -> !s.isBlank())
                .collect(Collectors.joining());
        return message;
    }
}
