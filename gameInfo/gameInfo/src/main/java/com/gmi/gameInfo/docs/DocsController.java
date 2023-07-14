package com.gmi.gameInfo.docs;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Api(tags = "Docs", description = "API문서 API")
@Controller
public class DocsController {

    @GetMapping("/api/docs")
    public String swagger() {
        return "redirect:/swagger-ui/index.html";
    }
}
