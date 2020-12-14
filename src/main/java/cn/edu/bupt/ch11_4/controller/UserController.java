package cn.edu.bupt.ch11_4.controller;

import cn.edu.bupt.ch11_4.dao.SysRoleRepository;
import cn.edu.bupt.ch11_4.dao.SysUserRepository;
import cn.edu.bupt.ch11_4.entity.SysRole;
import cn.edu.bupt.ch11_4.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    SysUserRepository sysUserRepository;
    @Autowired
    SysRoleRepository sysRoleRepository;

    @GetMapping("/login")
    String login(Model model){
        return "user/login";
    }

    @GetMapping("/register")
    String register(){
        return "user/register";
    }

    @PostMapping("/register")
    String registerDone(Model model, String username, String password){
        SysUser sysUser = sysUserRepository.findByUsername(username);
        if(sysUser != null){
            model.addAttribute("msg","用户名已存在！");
            return "user/register";
        }else {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            sysUser = new SysUser();
            sysUser.setUsername(username);
            sysUser.setPassword(passwordEncoder.encode(password));
            if(username.equals("admin")){
                SysRole sysRole = new SysRole();
                sysRole.setName("ROLE_ADMIN");
                sysRoleRepository.save(sysRole);
                List<SysRole> roles = new ArrayList<SysRole>();
                roles.add(sysRole);
                sysUser.setRoles(roles);
            }
            sysUserRepository.save(sysUser);
            return "redirect:/user/login";

        }


    }

}
