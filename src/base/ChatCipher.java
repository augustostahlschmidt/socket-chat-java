import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import static java.lang.System.exit;

public class ChatCipher {
    private SecretKey secretKey;
    private String base64EncodedSecretKey;
    private Cipher encryptCipher;
    private Cipher decryptCipher;
    static String CIPHER_MODE = "AES";

    public ChatCipher() {
        SecretKey secretKey = null;
        try {
            secretKey = KeyGenerator.getInstance(CIPHER_MODE).generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            exit(-1);
        }
        this.initCiphers(secretKey);
        this.setBase64EncodedSecretKey(Base64.getEncoder().encodeToString(secretKey.getEncoded()));
    }

    public ChatCipher(String base64EncodedSecretKey) {
        byte[] base64EncodedSecretKeyBytes = base64EncodedSecretKey.getBytes();
        byte[] base64DecodedSecretKey = Base64.getDecoder().decode(base64EncodedSecretKeyBytes);
        SecretKey secretKey = new SecretKeySpec(base64DecodedSecretKey, 0, base64DecodedSecretKey.length, ChatCipher.CIPHER_MODE);
        this.initCiphers(secretKey);
        this.setBase64EncodedSecretKey(base64EncodedSecretKey);
    }

    private void initCiphers(SecretKey secretKey){
        try {
            this.encryptCipher = Cipher.getInstance(CIPHER_MODE);
            this.encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey);
            this.decryptCipher = Cipher.getInstance(CIPHER_MODE);
            this.decryptCipher.init(Cipher.DECRYPT_MODE, secretKey);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e){
            e.printStackTrace();
            exit(-1);
        }
    }

    public String getBase64EncodedSecretKey(){
        return this.base64EncodedSecretKey;
    }

    public void setBase64EncodedSecretKey(String base64EncodedSecretKey){
        this.base64EncodedSecretKey = base64EncodedSecretKey;
    }

    public String encrypt(String plainText) {
        try {
            byte[] plainTextBytes = plainText.getBytes();
            byte[] encryptedTextBytes = encryptCipher.doFinal(plainTextBytes);
            String encodedBase64String = new String(Base64.getEncoder().encode(encryptedTextBytes));
            return encodedBase64String;
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decrypt(String encryptedText) {
        byte[] encryptedTextBytes = Base64.getDecoder().decode(encryptedText);
        byte[] plainTextBytes;
        try {
            plainTextBytes = decryptCipher.doFinal(encryptedTextBytes);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            return null;
        }
        String decodedBase64String = new String(plainTextBytes);
        return decodedBase64String;
    }
}