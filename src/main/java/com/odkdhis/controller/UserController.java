/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.controller;


import com.odkdhis.model.User;
import com.odkdhis.util.CommonUtil;
import com.odkdhis.util.Response;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController extends CommonUtil {
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping(value = {"/changepassword"})
    public ModelAndView home() {
        ModelAndView modelAndView = getModelAndView();
        modelAndView.addObject("ourUser", getCurrentLoggedUser());
        modelAndView.setViewName("changepassword");
        return modelAndView;
    }

    @GetMapping("/backhome")
    public String saveServerConfig() {
        return "redirect:/";
    }

    @GetMapping(value = "/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

    @GetMapping(value = "/login")
    public String login() throws Exception {
        userDao.createDefaultUser();
        return "login";
    }
    
    @PostMapping(value = "/changePassword")
    public String changePassword(HttpServletRequest request, HttpServletResponse response,@Valid User user) {

        try {
            User u = getCurrentLoggedUser();
            
            if (u == null) {
                return "redirect:/changepassword?error=You must be logged in to change password!";
            }
            

            if (!bCryptPasswordEncoder.matches(user.getPassword(), u.getPassword())) {
                return "redirect:/changepassword?error=Wrong Current Password!";
            }

            if (!user.getUserPassword2().equals(user.getConfirmuserPassword2())) {
                return "redirect:/changepassword?error=Make sure new password matches its confirmation!";
            }
            if (bCryptPasswordEncoder.matches(user.getUserPassword2(), u.getPassword())) {
                return "redirect:/changepassword?error=You can not use same password as current!";
            }
            u.setUserPassword2(user.getUserPassword2());
            userDao.save(u);
            logoutPage(request, response);
            return "redirect:/?change=Password Changed!!";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/changepassword?error=An error Occured!!!";
        }

    }
    
}
