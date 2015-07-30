package com.huotu.hotedu.web.advice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * ControllerAdvice, Advice to Controller.
 * @author CJ
 */
@ControllerAdvice
public class TestAdvice {

    /**
     * 空指针异常处理器，然并卵。
     */
    @ExceptionHandler(NullPointerException.class)
    public String onNullPointerException(NullPointerException ex,Model model){
        model.addAttribute("message","系统故障");
        return "/errorPage";
    }

}
