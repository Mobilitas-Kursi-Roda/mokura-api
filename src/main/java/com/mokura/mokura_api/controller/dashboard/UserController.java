package com.mokura.mokura_api.controller.dashboard;

import com.mokura.mokura_api.entity.User;
import com.mokura.mokura_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/dashboard/user")
public class UserController {

    final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ModelAndView index() {
        List<User> users = userRepository.findAll();
        ModelAndView mav = new ModelAndView("dashboard/user/index");
        mav.addObject("users", users);
        return mav;
    }

    @GetMapping("/add")
    public ModelAndView add() {
        ModelAndView mav = new ModelAndView("dashboard/user/add");
        mav.addObject("user", new User());
        return mav;
    }
}
