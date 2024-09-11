package com.project.tripmate.global.commons.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConfigUtils {

    private final HttpServletRequest request;
    private final EntityManager em;

    public String getParam(String name){
        return request.getParameter(name);
    }

    public String[] getParams (String name){
        return request.getParameterValues(name);
    }

    public static int getNumber(int num, int defaultValue){
        return num <=0 ? defaultValue : num;
    }

    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(em);
    }

    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    public ObjectMapper objectMapper(){
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return om;
    }

}
