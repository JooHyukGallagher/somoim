package me.weekbelt.runningflex.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.weekbelt.runningflex.domain.account.AccountRepository;
import me.weekbelt.runningflex.domain.account.AccountService;
import me.weekbelt.runningflex.domain.accountTag.AccountTagRepository;
import me.weekbelt.runningflex.domain.accountZone.AccountZoneRepository;
import me.weekbelt.runningflex.domain.tag.TagRepository;
import me.weekbelt.runningflex.domain.zone.ZoneRepository;
import me.weekbelt.runningflex.domain.zone.ZoneService;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
public abstract class BasicControllerTest {

    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;
    @Autowired protected PasswordEncoder passwordEncoder;

    @Autowired protected ZoneRepository zoneRepository;
    @Autowired protected TagRepository tagRepository;
    @Autowired protected AccountRepository accountRepository;
    @Autowired protected AccountTagRepository accountTagRepository;
    @Autowired protected AccountZoneRepository accountZoneRepository;

    @Autowired protected AccountService accountService;
    @Autowired protected ZoneService zoneService;

    @AfterEach
    public void afterEach() {
        accountTagRepository.deleteAll();
        accountZoneRepository.deleteAll();
        tagRepository.deleteAll();
        zoneRepository.deleteAll();
        accountRepository.deleteAll();
    }
}
