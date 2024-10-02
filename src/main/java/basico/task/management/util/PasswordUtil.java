package basico.task.management.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class PasswordUtil {

    public static String decode(String encodedPassword, String key, String iv) {
        String decodedString = "";
        try {
//            String key = "SupplierMgmt2022Basico";
//            String iv = "SupplierMgmt2022Basico";
//            String iv = "1234567812345678";

            Base64.Decoder decoder = Base64.getDecoder();
            byte[] encrypted1 = decoder.decode(encodedPassword);

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            decodedString = originalString.trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decodedString;
    }

}
