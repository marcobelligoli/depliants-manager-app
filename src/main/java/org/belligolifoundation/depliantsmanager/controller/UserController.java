package org.belligolifoundation.depliantsmanager.controller;

import org.belligolifoundation.depliantsmanager.model.dto.UserDTO;
import org.belligolifoundation.depliantsmanager.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "users/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserDTO userDTO) {
        userService.registerUser(userDTO);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "users/login";
    }
}
