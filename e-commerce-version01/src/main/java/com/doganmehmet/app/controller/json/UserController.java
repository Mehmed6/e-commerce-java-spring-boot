package com.doganmehmet.app.controller.json;

import com.doganmehmet.app.bean.json.JSONBeanName;
import com.doganmehmet.app.exception.ApiException;
import com.doganmehmet.app.exception.MyError;
import com.doganmehmet.app.repositories.IUserRepository;
import com.doganmehmet.app.services.SecurityControl;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(JSONBeanName.JSON_USER_CONTROLLER)
@RequestMapping("json/user")
public class UserController {

    private final IUserRepository m_userRepository;
    private final SecurityControl m_securityControl;

    public UserController(IUserRepository userRepository, SecurityControl securityControl)
    {
        m_userRepository = userRepository;
        m_securityControl = securityControl;
    }

    @DeleteMapping("/delete")
    @Transactional
    public String deleteUserById(@RequestParam Long id)
    {
        var user = m_userRepository.findById(id)
                .orElseThrow(() -> new ApiException(MyError.USER_NOT_FOUND));

        m_securityControl.checkTokenUserMatch(user.getUsername());

        m_userRepository.delete(user);

        return "User deleted";
    }


}
