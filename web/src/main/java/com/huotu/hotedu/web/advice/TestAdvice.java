package com.huotu.hotedu.web.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * ControllerAdvice, Advice to Controller.
 * @author CJ
 */
@ControllerAdvice
public class TestAdvice {


    /**
     * 所有异常统一处理
     */
//    @ExceptionHandler(Throwable.class)
    public String allThrowable(Throwable ex){
        //model.addAttribute("message","系统故障");
        return "redirect:/error";
    }

}
