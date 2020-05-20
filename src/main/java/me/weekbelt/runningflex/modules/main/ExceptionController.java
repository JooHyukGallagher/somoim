package me.weekbelt.runningflex.modules.main;

import lombok.extern.slf4j.Slf4j;
import me.weekbelt.runningflex.modules.account.Account;
import me.weekbelt.runningflex.modules.account.CurrentAccount;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
public class ExceptionController {

    @ExceptionHandler
    public String handlerRuntimeException(@CurrentAccount Account account, HttpServletRequest req,
                                          RuntimeException e) {
        if (account != null) {
            log.info("'{}' requested '{}'", account.getNickname(), req.getRequestURI());
        } else {
            log.info("requested '{}'", req.getRequestURI());
        }
        log.error("bad request", e);
        return "error";
    }
}
