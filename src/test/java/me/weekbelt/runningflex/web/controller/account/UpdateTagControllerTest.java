package me.weekbelt.runningflex.web.controller.account;

import me.weekbelt.runningflex.domain.account.Account;
import me.weekbelt.runningflex.domain.accountTag.AccountTag;
import me.weekbelt.runningflex.domain.tag.Tag;
import me.weekbelt.runningflex.domain.tag.TagRepository;
import me.weekbelt.runningflex.domain.tag.TagService;
import me.weekbelt.runningflex.web.BasicControllerTest;
import me.weekbelt.runningflex.web.controller.WithAccount;
import me.weekbelt.runningflex.web.dto.tag.TagForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UpdateTagControllerTest extends BasicControllerTest {

    @Autowired
    TagService tagService;
    @Autowired
    TagRepository tagRepository;

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
        Account joohyuk = accountService.findByNickname("joohyuk");
        List<AccountTag> accountTags = accountTagRepository.findByAccountId(joohyuk.getId());
        assertThat(accountTags.get(0).getTag()).isEqualTo(newTag);
        assertThat(accountTags.get(0).getAccount()).isEqualTo(joohyuk);
    }

    @WithAccount("joohyuk")
    @DisplayName("계정에 태그 삭제")
    @Test
    public void removeTag() throws Exception {
        // 정상적으로 태그가 추가되었는지 확인
        Account joohyuk = accountService.findByNickname("joohyuk");
        Tag newTag = tagRepository.save(Tag.builder().title("newTag").build());
        accountService.addTag(joohyuk, newTag);

        assertThat(newTag.getTitle()).isEqualTo("newTag");

        List<AccountTag> accountTags = accountTagRepository.findByAccountId(joohyuk.getId());
        assertThat(accountTags.get(0).getTag()).isEqualTo(newTag);
        assertThat(accountTags.get(0).getAccount()).isEqualTo(joohyuk);

        TagForm tagForm = new TagForm();
        tagForm.setTagTitle("newTag");

        // 계정에 태그 삭제 요청
        mockMvc.perform(post("/settings/tags/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagForm))
                .with(csrf()))
                .andExpect(status().isOk());

        // 삭제 요청 후 검증
        List<AccountTag> deletedAccountTag = accountTagRepository.findByAccountId(joohyuk.getId());
        assertThat(deletedAccountTag.size()).isEqualTo(0);

    }
}