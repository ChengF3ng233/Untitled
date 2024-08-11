package cn.feng.untitled.util.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Random;

/**
 * @author ChengFeng
 * @since 2024/8/11
 **/
public class DataUtil {
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static String encrypt(String text) {
        String secKey = createSecretKey(16);
        // Key
        String nonce = "0CoJUm6Qyw8W8jud";
        String encText = aesEncrypt(aesEncrypt(text, nonce), secKey);
        String modulus = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7"
                + "b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280"
                + "104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932"
                + "575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b" + "3ece0462db0a22b8e7";
        String pubKey = "010001";
        String encSecKey = rsaEncrypt(secKey, pubKey, modulus);
        return "params=" + URLEncoder.encode(encText, StandardCharsets.UTF_8) + "&encSecKey="
                + URLEncoder.encode(encSecKey, StandardCharsets.UTF_8);
    }

    public static String aesEncrypt(String text, String key) {
        try {
            IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(text.getBytes());

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            return "";
        }
    }

    public static String rsaEncrypt(String text, String pubKey, String modulus) {
        text = new StringBuilder(text).reverse().toString();
        BigInteger rs = new BigInteger(String.format("%x", new BigInteger(1, text.getBytes())), 16)
                .modPow(new BigInteger(pubKey, 16), new BigInteger(modulus, 16));
        StringBuilder r = new StringBuilder(rs.toString(16));
        if (r.length() >= 256) {
            return r.substring(r.length() - 256);
        } else {
            while (r.length() < 256) {
                r.insert(0, 0);
            }
            return r.toString();
        }
    }

    public static String createSecretKey(int length) {
        String shits = "0123456789abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            sb.append(shits.charAt(new Random().nextInt(shits.length())));
        }
        return sb.toString();
    }

    public static String toDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000') {
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }
}
