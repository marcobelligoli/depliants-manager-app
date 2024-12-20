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
    public String showRegistrationForm(Model model, UserDTO userRegistrationDTO) {
        model.addAttribute("user", userRegistrationDTO);
        return "users/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserDTO userRegistrationDTO, Model model) {
        UserDTO user = userService.findByUsername(userRegistrationDTO.getUsername());
        if (user != null) {
            model.addAttribute("Userexist", user);
            return "users/register";
        }
        userService.registerUser(userRegistrationDTO);
        return "redirect:/register?success";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model, UserDTO userDTO) {
        model.addAttribute("user", userDTO);
        return "users/login";
    }
}
