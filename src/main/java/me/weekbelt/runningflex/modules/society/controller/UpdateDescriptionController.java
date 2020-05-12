package me.weekbelt.runningflex.modules.society.controller;

import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.account.CurrentAccount;
import me.weekbelt.runningflex.modules.society.Society;
import me.weekbelt.runningflex.modules.society.SocietyService;
import me.weekbelt.runningflex.modules.society.form.SocietyDescriptionForm;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.file.AccessDeniedException;

@RequiredArgsConstructor
@RequestMapping("/society/{path}/settings")
@Controller
public class UpdateDescriptionController {

    private final SocietyService societyService;
    private final ModelMapper modelMapper;

    @GetMapping("/description")
    public String viewSocietySetting(@CurrentAccount Account account,
                                     @PathVariable String path, Model model) throws AccessDeniedException {
        Society society = societyService.getSocietyToUpdate(account, path);
        model.addAttribute("account", account);
        model.addAttribute("society", society);
        model.addAttribute("societyDescriptionForm", modelMapper.map(society, SocietyDescriptionForm.class));
        return "society/settings/description";
    }
}
