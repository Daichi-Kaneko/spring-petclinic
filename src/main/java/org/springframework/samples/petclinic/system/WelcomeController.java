package org.springframework.samples.petclinic.system;


import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class WelcomeController {
	
	@Autowired
	private HttpSession session;

    @GetMapping("/")
    public String welcome( Map<String, Object> model) {
    	
        return "welcome";
    }
}
