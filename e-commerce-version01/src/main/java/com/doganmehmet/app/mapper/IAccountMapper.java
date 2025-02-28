package com.doganmehmet.app.mapper;

import com.doganmehmet.app.dto.account.AccountDTO;
import com.doganmehmet.app.dto.account.AccountSaveDTO;
import com.doganmehmet.app.entity.Account;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(implementationName = "AccountMapperImpl", componentModel = "spring")
public interface IAccountMapper {

    Account toAccount(AccountSaveDTO accountSaveDTO);
    AccountDTO toAccountDTO(Account account);

    List<AccountDTO> toAccountDTOList(List<Account> accounts);
}
