package com.doganmehmet.app.services.json;

import com.doganmehmet.app.bean.json.JSONBeanName;
import com.doganmehmet.app.dto.account.AccountDTO;
import com.doganmehmet.app.dto.account.AccountSaveDTO;
import com.doganmehmet.app.exception.ApiException;
import com.doganmehmet.app.exception.MyError;
import com.doganmehmet.app.mapper.IAccountMapper;
import com.doganmehmet.app.repositories.IAccountRepository;
import com.doganmehmet.app.repositories.IUserRepository;
import com.doganmehmet.app.services.SecurityControl;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service(JSONBeanName.JSON_ACCOUNT_SERVICE)
public class AccountService {
    private final IAccountRepository m_accountRepository;
    private final SecurityControl m_securityControl;
    private final IUserRepository m_userRepository;
    private final IAccountMapper m_accountMapper;
    private final BCryptPasswordEncoder m_passwordEncoder;

    public AccountService(IAccountRepository accountRepository,
                          SecurityControl securityControl,
                          IUserRepository userRepository,
                          IAccountMapper accountMapper,
                          BCryptPasswordEncoder passwordEncoder)
    {
        m_accountRepository = accountRepository;
        m_securityControl = securityControl;
        m_userRepository = userRepository;
        m_accountMapper = accountMapper;
        m_passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AccountDTO saveAccount(AccountSaveDTO accountSaveDTO)
    {
        var user = m_securityControl.isUser(accountSaveDTO.getUsername(), accountSaveDTO.getPassword());

        if (m_accountRepository.findByAccountNo(accountSaveDTO.getAccountNo()).isPresent())
            throw new ApiException(MyError.ACCOUNT_ALREADY_EXISTS);

        accountSaveDTO.setPassword(m_passwordEncoder.encode(accountSaveDTO.getPassword()));
        var account = m_accountMapper.toAccount(accountSaveDTO);
        account.setUser(user);
        var savedAccount = m_accountRepository.save(account);

        user.setAccount(savedAccount);
        m_userRepository.save(user);
        return m_accountMapper.toAccountDTO(savedAccount);
    }

    public AccountDTO findAccountByAccountNo(String accountNo)
    {
        return m_accountMapper.toAccountDTO(m_accountRepository.findByAccountNo(accountNo)
                .orElseThrow(() -> new ApiException(MyError.ACCOUNT_NOT_FOUND)));
    }

    public AccountDTO findAccountByUsername(String username)
    {
        m_securityControl.checkTokenUserMatch(username);

        return m_accountMapper.toAccountDTO(m_accountRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(MyError.ACCOUNT_NOT_FOUND)));
    }

    public List<AccountDTO> findAllAccount()
    {
        return m_accountMapper.toAccountDTOList(m_accountRepository.findAll());
    }

    public AccountDTO addBalance(String username, BigDecimal balance)
    {
        m_securityControl.checkTokenUserMatch(username);

        if (balance.compareTo(BigDecimal.ZERO) < 0)
            throw new ApiException(MyError.NEGATIVE_BALANCE);

        var account = m_accountRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(MyError.ACCOUNT_NOT_FOUND));

        account.setBalance(account.getBalance().add(balance));
        return m_accountMapper.toAccountDTO(m_accountRepository.save(account));

    }
    @Transactional
    public void deleteAccountByUsername(String username)
    {
        m_securityControl.checkTokenUserMatch(username);

        m_accountRepository.deleteAccountByUsername(username);
    }

    @Transactional
    public void deleteAllAccount()
    {
        m_accountRepository.deleteAll();
    }
}
