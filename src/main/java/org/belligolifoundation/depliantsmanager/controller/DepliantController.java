package org.belligolifoundation.depliantsmanager.controller;

import org.belligolifoundation.depliantsmanager.model.dto.DepliantDTO;
import org.belligolifoundation.depliantsmanager.model.dto.UserDTO;
import org.belligolifoundation.depliantsmanager.service.DepliantService;
import org.belligolifoundation.depliantsmanager.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public String listDepliants(Model model, @AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size,
                                @RequestParam(required = false) String search, @RequestParam(defaultValue = "desc") String sortDirection) {
        UserDTO user = userService.findByUsername(userDetails.getUsername());
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "number");
        Page<DepliantDTO> depliants = depliantService.getDepliantsByUser(user.getId(), PageRequest.of(page, size, sort), search);
        model.addAttribute("depliants", depliants.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", depliants.getTotalPages());
        model.addAttribute("searchTerms", search);
        model.addAttribute("sortDirection", sortDirection);
        return "depliants/list";
    }

    @GetMapping("/new")
    public String newDepliantForm(Model model) {
        model.addAttribute("depliant", new DepliantDTO());
        model.addAttribute("formAction", "/depliants");
        return "depliants/form";
    }

    @PostMapping
    public String saveDepliant(@ModelAttribute DepliantDTO depliantDTO, @AuthenticationPrincipal UserDetails userDetails,
                               Model model) {
        try {
            UserDTO user = userService.findByUsername(userDetails.getUsername());
            depliantDTO.setUserId(user.getId());
            depliantService.saveDepliant(depliantDTO);
            return "redirect:/depliants";
        } catch (Exception e) {
            return manageDepliantsFormErrors(model, e, depliantDTO, "/depliants");
        }
    }

    @GetMapping("/update/{id}")
    public String updateDepliantForm(@PathVariable Long id, Model model) {
        DepliantDTO depliantDTO = depliantService.getDepliantById(id);
        model.addAttribute("depliant", depliantDTO);
        model.addAttribute("formAction", "/depliants/update/" + id);
        return "depliants/form";
    }

    @PostMapping("/update/{id}")
    public String updateDepliant(@PathVariable Long id, @ModelAttribute DepliantDTO depliantDTO,
                                 @AuthenticationPrincipal UserDetails userDetails, Model model) {
        try {
            UserDTO user = userService.findByUsername(userDetails.getUsername());
            depliantDTO.setUserId(user.getId());
            depliantDTO.setId(id);
            depliantService.updateDepliant(depliantDTO);
            return "redirect:/depliants";
        } catch (Exception e) {
            return manageDepliantsFormErrors(model, e, depliantDTO, "/depliants/update/" + id);
        }
    }

    private static String manageDepliantsFormErrors(Model model, Exception e, DepliantDTO depliantDTO, String id) {
        model.addAttribute("errorData", e instanceof DataIntegrityViolationException ? "submitted data not valid." : e.getMessage());
        model.addAttribute("depliant", depliantDTO);
        model.addAttribute("formAction", id);
        return "depliants/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteDepliant(@PathVariable Long id) {
        depliantService.deleteDepliant(id);
        return "redirect:/depliants";
    }
}
