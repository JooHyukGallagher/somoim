package me.weekbelt.runningflex.modules.society.form;

import lombok.Data;
import me.weekbelt.runningflex.modules.society.SocietyType;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SocietyForm {

    public static final String VALID_PATH_PATTERN = "^[ㄱ-ㅎ가-힣A-Za-z0-9_-]{2,20}$";

    @NotBlank
    @Length(min = 2, max = 20)
    @Pattern(regexp = VALID_PATH_PATTERN)
    private String path;

    @NotBlank
    @Length(max = 50)
    private String title;

    @NotBlank
    @Length(max = 100)
    private String shortDescription;

    @NotBlank
    private String fullDescription;

    private SocietyType societyType = SocietyType.FREE;

}
