package com.epam.brest.web_app;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Log LOGGER = LogFactory.getLog(CustomExceptionHandler.class);

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    protected String handleConflict(MethodArgumentTypeMismatchException ex, Model model) {
        LOGGER.error("filter bad params!");
        model.addAttribute("statusCodeName", "Bad Request");
        model.addAttribute("statusCode", "400".toCharArray());
        model.addAttribute("errorMessage", "filter param should be format: yyyy-MM-dd");
        return "error";
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    protected String handleConflict(IllegalArgumentException ex, Model model) {
        LOGGER.error(ex.getMessage());
        model.addAttribute("statusCodeName", "Bad Request");
        model.addAttribute("statusCode", "400".toCharArray());
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

}
