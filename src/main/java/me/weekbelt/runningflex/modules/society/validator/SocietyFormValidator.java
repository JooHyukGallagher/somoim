package me.weekbelt.runningflex.modules.society.validator;

import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.society.repository.SocietyRepository;
import me.weekbelt.runningflex.modules.society.form.SocietyForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class SocietyFormValidator implements Validator {

    private final SocietyRepository societyRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return SocietyForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SocietyForm societyForm = (SocietyForm) target;
        if (societyRepository.existsByPath(societyForm.getPath())) {
            errors.rejectValue("path", "wrong.path", "경로값을 사용할 수 없습니다.");
        }
    }
}
