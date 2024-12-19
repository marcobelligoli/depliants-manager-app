package org.belligolifoundation.depliantsmanager.controller;

import org.belligolifoundation.depliantsmanager.model.dto.DepliantDTO;
import org.belligolifoundation.depliantsmanager.model.dto.UserDTO;
import org.belligolifoundation.depliantsmanager.service.DepliantService;
import org.belligolifoundation.depliantsmanager.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/depliants")
public class DepliantController {

    private final DepliantService depliantService;
    private final UserService userService;

    public DepliantController(DepliantService depliantService, UserService userService) {
        this.depliantService = depliantService;
        this.userService = userService;
    }

    @GetMapping
    public String listDepliants(
            Model model,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        UserDTO user = userService.findByUsername(userDetails.getUsername());
        Page<DepliantDTO> depliants = depliantService.getDepliantsByUser(user.getId(), PageRequest.of(page, size));
        model.addAttribute("depliants", depliants.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", depliants.getTotalPages());
        return "depliants/list";
    }

    @GetMapping("/new")
    public String newDepliantForm(Model model) {
        model.addAttribute("depliant", new DepliantDTO());
        return "depliants/form";
    }

    @PostMapping
    public String saveDepliant(@ModelAttribute DepliantDTO depliantDTO, @AuthenticationPrincipal UserDetails userDetails) {
        UserDTO user = userService.findByUsername(userDetails.getUsername());
        depliantDTO.setUserId(user.getId());
        depliantService.saveDepliant(depliantDTO);
        return "redirect:/depliants";
    }

    @PostMapping("/update/{id}")
    public String updateDepliant(
            @PathVariable Long id,
            @ModelAttribute DepliantDTO depliantDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UserDTO user = userService.findByUsername(userDetails.getUsername());
        depliantDTO.setUserId(user.getId());
        depliantDTO.setId(id);
        depliantService.updateDepliant(depliantDTO);
        return "redirect:/depliants";
    }

    @GetMapping("/delete/{id}")
    public String deleteDepliant(@PathVariable Long id) {
        depliantService.deleteDepliant(id);
        return "redirect:/depliants";
    }
}
