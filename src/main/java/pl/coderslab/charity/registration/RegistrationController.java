package pl.coderslab.charity.registration;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.charity.user.AppUserRole;

import javax.validation.Valid;

@Controller
@RequestMapping("/register")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @GetMapping
    public String start(Model model) {
        model.addAttribute("request", new RegistrationRequest());
        return "log-reg/register";
    }

    @PostMapping
    public String register(@Valid @ModelAttribute("request") RegistrationRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "log-reg/register";
        }
        registrationService.register(request, AppUserRole.ROLE_USER);
        return "redirect:/login";
    }

    @GetMapping("/confirm/{token}")
    public String confirm(@PathVariable String token) {
        registrationService.confirmToken(token);
        return "log-reg/register-confirmation";

    }
}