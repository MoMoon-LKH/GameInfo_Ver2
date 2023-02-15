package com.gmi.gameInfo.docs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DocsController {

    @GetMapping("/api/docs")
    public String swagger() {
        return "redirect:/swagger-ui/index.html";
    }
}
