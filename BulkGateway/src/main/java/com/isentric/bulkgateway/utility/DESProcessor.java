//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.utility;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

public class DESProcessor {
    public static DESProcessor desProcessor;

    public String encrypt(String data, String k) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        // Ensure a DES key of length 8 bytes (truncate or pad as necessary)
        byte[] keyBytes = Arrays.copyOf(k.getBytes(StandardCharsets.UTF_8), 8);
        DESKeySpec spec = new DESKeySpec(keyBytes);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
        SecretKey secret = factory.generateSecret(spec);
        Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        desCipher.init(1, secret);
        byte[] cleartext = data.getBytes(StandardCharsets.UTF_8);
        byte[] ciphertext = desCipher.doFinal(cleartext);
        String stringData = Base64.getEncoder().encodeToString(ciphertext);
        return stringData;
    }

    public String decrypt(String data, String k) throws Exception {
        // Ensure a DES key of length 8 bytes (truncate or pad as necessary)
        byte[] keyBytes = Arrays.copyOf(k.getBytes(StandardCharsets.UTF_8), 8);
        DESKeySpec spec = new DESKeySpec(keyBytes);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
        SecretKey secret = factory.generateSecret(spec);
        Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        desCipher.init(Cipher.DECRYPT_MODE, secret);
        byte[] byteData = Base64.getDecoder().decode(data);
        byte[] clearText = desCipher.doFinal(byteData);
        return new String(clearText, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws Exception {
        DESProcessor desProcessor = new DESProcessor();
        String mySecretKey = "isentric88";
        String secret = "D5oJq2fjH4O4rRVeHD/jLw4+zTanCKIYnW0KAA58BvGBPCfiYAAGMg==";
        String a = desProcessor.encrypt(secret, mySecretKey);
        System.out.println("Encrypted Message          : ".concat(String.valueOf(String.valueOf(a))));
        String b = desProcessor.decrypt(a, mySecretKey);
        System.out.println("Decrypted Message          : ".concat(String.valueOf(String.valueOf(b))));
        String ea = URLEncoder.encode(a, "UTF-8");
        System.out.println("URL encoded encrypted data : ".concat(String.valueOf(String.valueOf(ea))));
        String db = URLDecoder.decode(ea, "UTF-8");
        System.out.println("URL decoded encrypted data : ".concat(String.valueOf(String.valueOf(db))));
        System.out.println("url encode :" + URLEncoder.encode("9TLt/dhx1z13UXGCaR4v9NwFTDHNTdg+2vAIzDAkNM4= "));
    }
}
