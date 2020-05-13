package me.weekbelt.runningflex.modules.society.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.weekbelt.runningflex.infra.MockMvcTest;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.account.AccountRepository;
import me.weekbelt.runningflex.modules.account.AccountService;
import me.weekbelt.runningflex.modules.account.WithAccount;
import me.weekbelt.runningflex.modules.society.Society;
import me.weekbelt.runningflex.modules.society.SocietyFactory;
import me.weekbelt.runningflex.modules.society.SocietyService;
import me.weekbelt.runningflex.modules.tag.Tag;
import me.weekbelt.runningflex.modules.tag.TagRepository;
import me.weekbelt.runningflex.modules.tag.form.TagForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class UpdateSocietyTagControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired AccountRepository accountRepository;
    @Autowired AccountService accountService;
    @Autowired SocietyService societyService;
    @Autowired ObjectMapper objectMapper;
    @Autowired TagRepository tagRepository;
    @Autowired SocietyFactory societyFactory;

    @DisplayName("소모임 태그 수정 폼")
    @WithAccount("joohyuk")
    @Test
    public void updateTagForm() throws Exception {
        // 소모임 생성
        Society society = getSociety();

        String requestUrl = "/society/" + society.getEncodedPath() + "/settings/tags";
        mockMvc.perform(get(requestUrl))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("society"))
                .andExpect(model().attributeExists("tags"))
                .andExpect(model().attributeExists("whitelist"))
                .andExpect(status().isOk())
                .andExpect(view().name("society/settings/tags"));
    }


    @DisplayName("소모임에 태그 추가")
    @WithAccount("joohyuk")
    @Test
    public void addTag() throws Exception {
        Society society = getSociety();

        TagForm tagForm = new TagForm();
        tagForm.setTagTitle("test");

        String requestUrl = "/society/" + society.getEncodedPath() + "/settings/tags/add";
        mockMvc.perform(post(requestUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagForm))
                .with(csrf()))
                .andExpect(status().isOk());
    }

    @DisplayName("소모임에 태그 삭제")
    @WithAccount("joohyuk")
    @Test
    public void removeTag() throws Exception {
        Society society = getSociety();
        Tag newTag = tagRepository.save(Tag.builder().title("test").build());
        societyService.addTag(society, newTag);

        assertThat(society.getTags().contains(newTag)).isTrue();

        TagForm tagForm = new TagForm();
        tagForm.setTagTitle("test");

        String requestUrl = "/society/" + society.getEncodedPath() + "/settings/tags/remove";
        mockMvc.perform(post(requestUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagForm))
                .with(csrf()))
                .andExpect(status().isOk());

        assertThat(society.getTags().contains(newTag)).isFalse();
    }

    private Society getSociety() {
        Account joohyuk = accountService.getAccount("joohyuk");
        return societyFactory.createSociety("test", joohyuk);
    }
}
