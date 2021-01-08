package com.catis.Controller.configuration;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

public class CryptoUtil {

private static final String salt = "e606bfd5cf9f198e"; //any random generated salt

        public static String encrypt(String plain, String password) {
            TextEncryptor textEncryptor = Encryptors.queryableText(password, salt);
            return textEncryptor.encrypt(plain);
        }

        public static String decrypt(String encrypted, String password) {
            TextEncryptor textEncryptor = Encryptors.queryableText(password, salt);
            return textEncryptor.decrypt(encrypted);
        }
 }