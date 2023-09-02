package com.example.demo.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.bcrypt.BCrypt;

@UtilityClass
public class PasswordUtil {
  public static String encryptPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
  }

  public static boolean equalsPassword(String password, String encryptedPassword) {
    return BCrypt.checkpw(password, encryptedPassword);
  }
}
