package com.fengxin.utils;

import com.fengxin.dto.UserDTO;

/**
 * @author FENGXIN
 */
public class UserHolder {
    private static final ThreadLocal<UserDTO> TL = new ThreadLocal<>();

    public static void saveUser(UserDTO user){
        TL.set(user);
    }

    public static UserDTO getUser(){
        return TL.get();
    }

    public static void removeUser(){ TL.remove();
    }
}
