package com.colabear754.blog_example_kt.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import springfox.documentation.annotations.ApiIgnore

@Controller
@ApiIgnore
class MainController {
    @GetMapping("/")
    fun redirectToSwagger() = "redirect:/swagger-ui/index.html"
}