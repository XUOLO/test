package com.example.BookMangement.Controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * CustomErrorController
 *
 * @author benvo
 * @version 01-00
 * @since 5/24/2024
 */
@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request){
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if(status!=null&& Integer.valueOf(status.toString())== HttpStatus.NOT_FOUND.value()){
            return "Error/404";
        }
        return "Error/404";
    }
}