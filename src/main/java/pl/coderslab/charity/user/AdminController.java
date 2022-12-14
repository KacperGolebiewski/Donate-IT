package pl.coderslab.charity.user;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.charity.category.Category;
import pl.coderslab.charity.category.CategoryRepository;
import pl.coderslab.charity.donation.Donation;
import pl.coderslab.charity.donation.DonationRepository;
import pl.coderslab.charity.institution.Institution;
import pl.coderslab.charity.institution.InstitutionRepository;
import pl.coderslab.charity.institution.InstitutionService;
import pl.coderslab.charity.message.Message;
import pl.coderslab.charity.message.MessageRepository;
import pl.coderslab.charity.message.MessageService;
import pl.coderslab.charity.registration.RegistrationRequest;
import pl.coderslab.charity.registration.RegistrationService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final InstitutionService institutionService;
    private final CategoryRepository categoryRepository;
    private final AppUserService appUserService;
    private final DonationRepository donationRepository;
    private final AppUserRepository appUserRepository;
    private final RegistrationService registrationService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final InstitutionRepository institutionRepository;
    private final MessageService messageService;
    private final MessageRepository messageRepository;


    @GetMapping("/dashboard")
    public String viewDashboardPage(Model model) {
        return adminDashboard(1, "firstName", "asc", model);
    }

    @GetMapping("/users")
    public String viewUsersPage(Model model) {
        return adminUsers(1, "firstName", "asc", model);
    }

    @GetMapping("/institutions")
    public String viewInstitutionsPage(Model model) {
        return adminInstitutions(1, "name", "asc", model);
    }

    @GetMapping("/messages")
    public String viewMessagesPages(Model model) {
        return adminMessages(1, "created", "desc", model);
    }

    //ADMIN SECTION START
    @GetMapping("/dashboard/{pageNo}")
    String adminDashboard(@PathVariable int pageNo, @RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir, Model model) {
        int pageSize = 10;
        Page<AppUser> page = appUserService.findAdminsPaginated(pageNo, pageSize, sortField, sortDir);
        List<AppUser> admins = page.getContent();
        addAttribute(pageNo, sortField, sortDir, model, page.getTotalPages(), page.getTotalElements(), page);
        model.addAttribute("admins", admins);
        return "admin/admins/dashboard";
    }

    @GetMapping("/dashboard/add")
    String adminAdd(Model model) {
        model.addAttribute("request", new RegistrationRequest());
        return "admin/admins/add-admin";
    }

    @PostMapping("/dashboard/add")
    String adminSave(@Valid @ModelAttribute("request") RegistrationRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/admins/add-admin";
        }
        registrationService.register(request, AppUserRole.ROLE_ADMIN);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/dashboard/edit/{id}")
    String adminEdit(@PathVariable long id, Model model) {
        model.addAttribute("admin", appUserRepository.findById(id).orElseThrow(EntityNotFoundException::new));
        return "admin/admins/edit-admin";
    }

    @PostMapping("/dashboard/edit/{id}")
    String adminUpdate(@PathVariable long id, @Valid @ModelAttribute("admin") AppUser admin, BindingResult bindingResult) {
        AppUser appUser = appUserRepository.findById(id).get();
        if (bindingResult.hasErrors()) {
            return "admin/admins/edit-admin";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (appUserRepository.findById(id).isPresent()) {
            if (appUserRepository.findById(id).get().getEmail().equals(auth.getName())) {
                admin.setEnabled(appUser.getEnabled());
                admin.setLocked(appUser.getLocked());
            }
        }
        admin.setAppUserRole(appUser.getAppUserRole());
        admin.setPassword(bCryptPasswordEncoder.encode(admin.getPassword()));
        appUserRepository.save(admin);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/dashboard/confirm-delete/{id}")
    String adminConfirmDelete(@PathVariable long id, Model model) {
        model.addAttribute("admin", appUserRepository.findById(id).orElseThrow(EntityNotFoundException::new));
        model.addAttribute("id", id);
        return "admin/admins/delete-admin";
    }

    @GetMapping("/dashboard/delete/{id}")
    @Transactional
    String adminDelete(@PathVariable long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (appUserRepository.findById(id).isPresent()) {
            if (auth.getName().equals(appUserRepository.findById(id).get().getEmail())) {
                throw new IllegalStateException("Cannot delete currently logged admin");
            } else {
                List<Donation> donations = donationRepository.findAllByUserId(id);
                for (Donation donation : donations) {
                    donation.setUser(null);
                }
                appUserRepository.deleteById(id);
            }
        }
        return "redirect:/admin/dashboard";
    }

    //ADIN SECTION END
    //USER SECTION START
    @GetMapping("/users/{pageNo}")
    String adminUsers(@PathVariable int pageNo, @RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir, Model model) {
        int pageSize = 20;
        Page<AppUser> page = appUserService.findUsersPaginated(pageNo, pageSize, sortField, sortDir);
        List<AppUser> users = page.getContent();
        addAttribute(pageNo, sortField, sortDir, model, page.getTotalPages(), page.getTotalElements(), page);
        model.addAttribute("users", users);
        return "admin/users/users";
    }

    @GetMapping("/users/add")
    String adminUserAdd(Model model) {
        model.addAttribute("request", new RegistrationRequest());
        return "admin/users/add-user";
    }

    @PostMapping("/users/add")
    String adminUserSave(@Valid @ModelAttribute("request") RegistrationRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/users/add-user";
        }
        registrationService.register(request, AppUserRole.ROLE_USER);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/edit/{id}")
    String adminUserEdit(@PathVariable long id, Model model) {
        model.addAttribute("user", appUserRepository.findById(id).orElseThrow(EntityNotFoundException::new));
        return "admin/users/edit-user";
    }

    @PostMapping("/users/edit/{id}")
    String adminUserUpdate(@PathVariable long id, @Valid @ModelAttribute("user") AppUser user, BindingResult bindingResult) {
        AppUser appUser = appUserRepository.findById(id).get();
        if (bindingResult.hasErrors()) {
            return "admin/admins/edit-admin";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (appUserRepository.findById(id).isPresent()) {
            if (appUserRepository.findById(id).get().getEmail().equals(auth.getName())) {
                user.setEnabled(appUser.getEnabled());
                user.setLocked(appUser.getLocked());
            }
        }
        user.setAppUserRole(appUser.getAppUserRole());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        appUserRepository.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/confirm-delete/{id}")
    String adminUserConfirmDelete(@PathVariable long id, Model model) {
        model.addAttribute("user", appUserRepository.findById(id).orElseThrow(EntityNotFoundException::new));
        model.addAttribute("id", id);
        return "admin/users/delete-user";
    }

    @GetMapping("/users/delete/{id}")
    String adminUserDelete(@PathVariable long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (appUserRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException();
        }
        if (auth.getName().equals(appUserRepository.findById(id).get().getEmail())) {
            throw new IllegalStateException("Cannot delete currently logged admin");
        } else {
            List<Donation> donations = donationRepository.findAllByUserId(id);
            for (Donation donation : donations) {
                donation.setUser(null);
            }
            appUserRepository.deleteById(id);
        }
        return "redirect:/admin/users";
    }
    //USER SECTION END
    //INSTITUTION SECTION START

    @GetMapping("/institutions/{pageNo}")
    String adminInstitutions(@PathVariable int pageNo, @RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir, Model model) {
        int pageSize = 10;
        Page<Institution> page = institutionService.findAllPaginated(pageNo, pageSize, sortField, sortDir);
        List<Institution> institutions = page.getContent();
        addAttribute(pageNo, sortField, sortDir, model, page.getTotalPages(), page.getTotalElements(), page);
        model.addAttribute("institutions", institutions);
        return "admin/institutions/institutions";
    }

    @GetMapping("/institutions/add")
    String adminInstitutionsAdd(Model model) {
        model.addAttribute("institution", new Institution());
        return "admin/institutions/add-institution";
    }

    @PostMapping("/institutions/add")
    String adminInstitutionsSave(@Valid Institution institution, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/institutions/add-institution";
        }
        institutionRepository.save(institution);
        return "redirect:/admin/institutions";
    }

    @GetMapping("/institutions/edit/{id}")
    String adminInstitutionsEdit(@PathVariable long id, Model model) {
        model.addAttribute("institution", institutionRepository.findById(id).orElseThrow(EntityNotFoundException::new));
        return "admin/institutions/edit-institution";
    }

    @PostMapping("/institutions/edit/{id}")
    String adminInstitutionsUpdate(@Valid Institution institution, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/institutions/edit-institution";
        }
        institutionRepository.save(institution);
        return "redirect:/admin/institutions";
    }

    @GetMapping("/institutions/confirm-delete/{id}")
    String adminInstitutionsConfirmDelete(@PathVariable long id, Model model) {
        model.addAttribute("id", id);
        return "admin/institutions/delete-institution";
    }

    @GetMapping("/institutions/delete/{id}")
    String adminInstitutionsDelete(@PathVariable long id) {
        if (donationRepository.findDonationByInstitutionId(id).isPresent()) {
            return "redirect:/admin/institutions/confirm-archive/" + id;
        } else {
            institutionRepository.deleteById(id);
        }
        return "redirect:/admin/institutions";
    }

    @GetMapping("/institutions/confirm-archive/{id}")
    String adminInstitutionsConfirmArchive(@PathVariable long id, Model model) {
        model.addAttribute("id", id);
        return "admin/institutions/archive-institution";
    }

    @GetMapping("/institutions/archive/{id}")
    String adminInstitutionsArchive(@PathVariable long id) {
        if (institutionRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException();
        }
        Institution institution = institutionRepository.findById(id).get();
        institution.setActive(false);
        institutionRepository.save(institution);
        return "redirect:/admin/institutions";
    }

    //INSTITUTION SECTION END
    //CATEGORY SECTION START
    @GetMapping("/categories")
    String adminCategories(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/categories/category";
    }

    @GetMapping("/categories/add")
    String adminCategoriesAdd(Model model) {
        model.addAttribute("category", new Category());
        return "admin/categories/add-category";
    }

    @PostMapping("/categories/add")
    String adminCategoriesSave(@Valid Category category, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/categories/add-category";
        }
        categoryRepository.save(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/edit/{id}")
    String adminCategoriesEdit(@PathVariable long id, Model model) {
        model.addAttribute("category", categoryRepository.findById(id).orElseThrow(EntityNotFoundException::new));
        return "admin/categories/edit-category";
    }

    @PostMapping("/categories/edit/{id}")
    String adminCategoriesUpdate(@Valid Category category, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/categories/edit-category";
        }
        categoryRepository.save(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/confirm-delete/{id}")
    String adminCategoriesConfirmDelete(@PathVariable long id, Model model) {
        model.addAttribute("id", id);
        return "admin/categories/delete-category";
    }

    @GetMapping("/categories/delete/{id}")
    String adminCategoriesDelete(@PathVariable long id) {
        if (!donationRepository.findDonationByCategoryId(id).isEmpty()) {
            return "redirect:/admin/categories/confirm-archive/" + id;
        } else {
            categoryRepository.deleteById(id);
        }
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/confirm-archive/{id}")
    String adminCategoriesConfirmArchive(@PathVariable long id, Model model) {
        model.addAttribute("id", id);
        return "admin/categories/archive-category";
    }

    @GetMapping("/categories/archive/{id}")
    String adminCategoriesArchive(@PathVariable long id) {
        if (categoryRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException();
        }
        Category category = categoryRepository.findById(id).get();
        category.setActive(false);
        categoryRepository.save(category);
        return "redirect:/admin/categories";
    }

    //Category SECTION END
    //MESSAGE SECTION START
    @GetMapping("/messages/{pageNo}")
    String adminMessages(@PathVariable int pageNo, @RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir, Model model) {
        int pageSize = 20;
        Page<Message> page = messageService.findAllPaginated(pageNo, pageSize, sortField, sortDir);
        List<Message> messages = page.getContent();
        addAttribute(pageNo, sortField, sortDir, model, page.getTotalPages(), page.getTotalElements(), page);
        model.addAttribute("messages", messages);
        return "admin/messages/messages";
    }


    @GetMapping("/messages/confirm-delete/{id}")
    String adminMessagesConfirmDelete(@PathVariable long id, Model model) {
        model.addAttribute("id", id);
        return "admin/messages/delete-message";
    }

    @GetMapping("/messages/delete/{id}")
    String adminMessagesDelete(@PathVariable long id) {
        messageRepository.deleteById(id);
        return "redirect:/admin/messages";
    }

    public static <T> void addAttribute(@PathVariable int pageNo, @RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir, Model model, int totalPages, long totalElements, Page<T> page) {
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalElements);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
    }
}
