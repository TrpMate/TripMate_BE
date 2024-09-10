package com.project.tripmate.config.commons.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class MessageUtils {

    private static ResourceBundle validationsBundle;

    private static ResourceBundle errorsBundle;

    private static ResourceBundle commonsBundle;

    static {
        validationsBundle = ResourceBundle.getBundle("messages.validations");
        errorsBundle = ResourceBundle.getBundle("messages.errors");
        commonsBundle = ResourceBundle.getBundle("messages.commons");
    }

    public static String getMessage (String code , String bundleType){
        bundleType = Objects.requireNonNullElse(bundleType , "validation");
        ResourceBundle  bundle = null;

        if(bundleType.equals("commons")){
            bundle = commonsBundle;
        }else if (bundleType.equals("errors")){
            bundle = errorsBundle;
        }else {
            bundle = validationsBundle;
        }

        try{
            return bundle.getString(code);
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public static String getMessage(String code){
        return getMessage(code , null);
    }

}
