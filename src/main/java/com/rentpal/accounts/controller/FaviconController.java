package com.rentpal.accounts.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FaviconController {
    /**
     * Forwards favicon.ico to favicon.svg.
     *
     * @return the string
     */
    @RequestMapping("favicon.ico")
    String favicon() {
        return "forward:/resources/images/favicon.svg";
    }
}
