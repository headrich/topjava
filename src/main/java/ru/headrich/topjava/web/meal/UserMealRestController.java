package ru.headrich.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import ru.headrich.topjava.service.UserMealService;

/**
 * Created by Montana on 07.06.2016.
 */
@Controller
public class UserMealRestController {
    @Autowired
    WebApplicationContext webApplicationContext;

    private UserMealService userMealService;

    @ResponseBody
    @RequestMapping(value="/d2/index.htm", method= RequestMethod.GET)
    public String test(Model model) {


        //model.addAttribute("accounts", userService.getAll());
        return "tararat   from " + webApplicationContext.getDisplayName();
    }





}
