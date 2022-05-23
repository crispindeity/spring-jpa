package jpabook.jpashop.controller;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    private final Logger log = getLogger(HomeController.class);

    @RequestMapping("/")
    public String home() {
        log.info("home controller");
        return "home";
    }
}
