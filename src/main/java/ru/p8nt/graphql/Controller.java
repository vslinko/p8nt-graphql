package ru.p8nt.graphql;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String get() {
        return "graphql";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String post() {
        return "graphql";
    }
}
