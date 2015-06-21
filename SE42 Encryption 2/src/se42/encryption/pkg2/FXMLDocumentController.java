/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se42.encryption.pkg2;

import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 *
 * @author Twan
 * @see http://docs.oracle.com/javase/1.5.0/docs/guide/security/jce/JCERefGuide.html
 */
public class FXMLDocumentController implements Initializable
{   
    private final static String ALGORITHM = "PBEWithMD5AndDES";
    private final static int    WORKLOAD  = 100;
    private final static int    SALT_SIZE = 8;
    
    @FXML private TextArea      taMessage;
    @FXML private PasswordField pfPassword;
   
    private byte[] salt;
    private PBEParameterSpec pbeParamSpec;
    private PBEKeySpec pbeKeySpec;
    private SecretKeyFactory keyFac;
    private Cipher pbeCipher;
    
    @FXML
    private void encode()
    {
        try {
            this.pbeKeySpec = new PBEKeySpec(pfPassword.getText().toCharArray());

            // clear password
            this.pfPassword.clear();

            // generate enctypted text
            SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
            pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
            
            byte[] message    = this.taMessage.getText().getBytes();
            byte[] ciphertext = pbeCipher.doFinal(message);
            
            this.taMessage.setText(new String(Base64.getEncoder().encode(ciphertext)));
            
        } catch (
            InvalidKeySpecException |
            InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException |
            BadPaddingException ex
        ) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    @FXML
    private void decode()
    {
        try {
            this.pbeKeySpec = new PBEKeySpec(pfPassword.getText().toCharArray());

            // clear password
            this.pfPassword.clear();

            // decrypt text
            SecretKey pbeKey = keyFac.generateSecret(this.pbeKeySpec);
            this.pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, this.pbeParamSpec);
            
            byte[] message    = Base64.getDecoder().decode(this.taMessage.getText());
            byte[] ciphertext = this.pbeCipher.doFinal(message);
            
            this.taMessage.setText(new String(ciphertext));
            
        } catch (
            InvalidKeySpecException |
            InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException |
            BadPaddingException ex
        ) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        SecureRandom sr = new SecureRandom();
        this.salt = new byte[SALT_SIZE];
        sr.nextBytes(salt);

        try {
            this.pbeParamSpec = new PBEParameterSpec(this.salt, WORKLOAD);
            this.keyFac       = SecretKeyFactory.getInstance(ALGORITHM);
            this.pbeCipher    = Cipher.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
