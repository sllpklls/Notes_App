package com.rick.notes.security;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import android.util.Base64;
import android.widget.Toast;

public class AESUtil {
    private static final String AES_ALGORITHM = "AES";
    private static final String AES_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String CHARSET = "UTF-8";

    // Mã hóa dữ liệu
    public static String encrypt(String key, String initVector, String data) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(CHARSET));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(CHARSET), AES_ALGORITHM);

            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(data.getBytes());

            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Giải mã dữ liệu
    public static String decrypt(String key, String initVector, String encryptedData) {
        try {//Toast.makeText(null, "vclll", Toast.LENGTH_SHORT).show();
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(CHARSET));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(CHARSET), AES_ALGORITHM);

            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decode(encryptedData, Base64.DEFAULT));

            return new String(original, CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
