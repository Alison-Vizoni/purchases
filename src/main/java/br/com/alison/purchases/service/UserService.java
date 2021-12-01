package br.com.alison.purchases.service;

import br.com.alison.purchases.security.UserSpringSecurity;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserService {

    public static UserSpringSecurity getUserAuthenticated(){
        try {
            return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e){
            return null;
        }
    }
}
