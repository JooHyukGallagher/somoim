package me.weekbelt.runningflex.modules.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.weekbelt.runningflex.infra.MockMvcTest;
import me.weekbelt.runningflex.modules.account.AccountRepository;
import me.weekbelt.runningflex.modules.account.AccountService;
import me.weekbelt.runningflex.modules.accountTag.AccountTagRepository;
import me.weekbelt.runningflex.modules.accountZone.AccountZoneRepository;
import me.weekbelt.runningflex.modules.tag.TagRepository;
import me.weekbelt.runningflex.modules.zone.ZoneRepository;
import me.weekbelt.runningflex.modules.zone.ZoneService;
import me.weekbelt.runningflex.infra.mail.EmailService;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@MockMvcTest
public abstract class AccountBasicControllerTest {

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
    @MockBean protected EmailService emailService;

    @AfterEach
    public void afterEach() {
        accountTagRepository.deleteAll();
        accountZoneRepository.deleteAll();
        tagRepository.deleteAll();
        zoneRepository.deleteAll();
        accountRepository.deleteAll();
    }
}
