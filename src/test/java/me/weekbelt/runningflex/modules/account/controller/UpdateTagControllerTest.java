package me.weekbelt.runningflex.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.weekbelt.runningflex.infra.MockMvcTest;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.account.AccountService;
import me.weekbelt.runningflex.modules.account.WithAccount;
import me.weekbelt.runningflex.modules.tag.Tag;
import me.weekbelt.runningflex.modules.tag.TagRepository;
import me.weekbelt.runningflex.modules.tag.TagService;
import me.weekbelt.runningflex.modules.tag.form.TagForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class UpdateTagControllerTest{

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired TagService tagService;
    @Autowired AccountService accountService;
    @Autowired TagRepository tagRepository;

    @WithAccount("joohyuk")
    @DisplayName("계정의 태그 수정 폼")
    @Test
    public void updateTagForm() throws Exception {
        mockMvc.perform(get("/settings/tags"))
                .andExpect(view().name("account/settings/tags"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("tags"))
                .andExpect(model().attributeExists("whitelist"));
    }

    @WithAccount("joohyuk")
    @DisplayName("계정에 태그 추가")
    @Test
    public void addTag() throws Exception {
        TagForm tagForm = new TagForm();
        tagForm.setTagTitle("newTag");

        mockMvc.perform(post("/settings/tags/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagForm))
                .with(csrf()))
                .andExpect(status().isOk());

        // Tag 저장 확인
        Tag newTag = tagService.findSavedTagByTitle(tagForm.getTagTitle());
        assertThat(newTag.getTitle()).isEqualTo(tagForm.getTagTitle());

        // AccountTag 저장 확인
        Account joohyuk = accountService.getAccount("joohyuk");
        assertThat(joohyuk.getTags().contains(newTag)).isTrue();
    }

    @WithAccount("joohyuk")
    @DisplayName("계정에 태그 삭제")
    @Test
    public void removeTag() throws Exception {
        // 정상적으로 태그가 추가되었는지 확인
        Account joohyuk = accountService.getAccount("joohyuk");
        Tag newTag = tagRepository.save(Tag.builder().title("newTag").build());
        accountService.addTag(joohyuk, newTag);

        assertThat(joohyuk.getTags().contains(newTag)).isTrue();

        TagForm tagForm = new TagForm();
        tagForm.setTagTitle("newTag");

        // 계정에 태그 삭제 요청
        mockMvc.perform(post("/settings/tags/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagForm))
                .with(csrf()))
                .andExpect(status().isOk());

        // 삭제 요청 후 검증
        assertThat(joohyuk.getTags().contains(newTag)).isFalse();
    }
}