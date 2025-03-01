package com.doganmehmet.app.controller.json;

import com.doganmehmet.app.bean.json.JSONBeanName;
import com.doganmehmet.app.dto.account.AccountDTO;
import com.doganmehmet.app.dto.account.AccountSaveDTO;
import com.doganmehmet.app.services.json.AccountService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController(JSONBeanName.JSON_ACCOUNT_CONTROLLER)
@RequestMapping("json/api/account")
public class AccountController {
    private final AccountService m_accountService;

    public AccountController(AccountService accountService)
    {
        m_accountService = accountService;
    }

    @PostMapping("/save")
    public AccountDTO saveAccount(@Valid @RequestBody AccountSaveDTO accountSaveDTO)
    {
        return m_accountService.saveAccount(accountSaveDTO);
    }

    @GetMapping("/find/no")
    public AccountDTO findAccountByAccountNo(@RequestParam String accountNo)
    {
        return m_accountService.findAccountByAccountNo(accountNo);
    }

    @GetMapping("/find/username")
    public AccountDTO findAccountByUsername(@RequestParam String username)
    {
        return m_accountService.findAccountByUsername(username);
    }

    @GetMapping("/find/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AccountDTO> findAllAccount()
    {
        return m_accountService.findAllAccount();
    }

    @PostMapping("/add/balance")
    public AccountDTO addBalance(@RequestParam String username,@RequestParam BigDecimal balance)
    {
        return m_accountService.addBalance(username, balance);
    }

    @DeleteMapping("/delete")
    public void deleteAccountByUsername(@RequestParam String username)
    {
        m_accountService.deleteAccountByUsername(username);
    }

    @DeleteMapping("/delete/all")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAllAccount()
    {
        m_accountService.deleteAllAccount();
    }
}
