/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se42.encryption;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Twan
 */
public class SE42Encryption_1
{
    public static final String ALGORITHM        = "RSA";
    public static final String PRIVATE_KEY_FILE = "C:/keys/private.key";
    public static final String PUBLIC_KEY_FILE  = "C:/keys/public.key";
  
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        try {
            // genereer sleutels
            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
            keyGen.initialize(1024);
            final KeyPair key = keyGen.generateKeyPair();

            // schrijf private key weg naar bestand
            File privateKeyFile = new File(PRIVATE_KEY_FILE);
            privateKeyFile.getParentFile().mkdirs();
            privateKeyFile.createNewFile();
            
            try (ObjectOutputStream privateKeyOS = new ObjectOutputStream(
                new FileOutputStream(privateKeyFile))
            ) {
                privateKeyOS.writeObject(key.getPrivate());
            }
            
            // schrijf public key weg naar bestand
            File publicKeyFile = new File(PUBLIC_KEY_FILE);
            publicKeyFile.getParentFile().mkdirs();
            publicKeyFile.createNewFile();

            try (ObjectOutputStream publicKeyOS = new ObjectOutputStream(
                new FileOutputStream(publicKeyFile))
            ) {
                publicKeyOS.writeObject(key.getPublic());
            }   
        } catch (NoSuchAlgorithmException | IOException ex) {
            Logger.getLogger(SE42Encryption_1.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
}



/**
 * @author JavaDigest
 * 
 
public class EncryptionUtil {



  

  /**
   * The method checks if the pair of public and private key has been generated.
   * 
   * @return flag indicating if the pair of keys were generated.
   */
 


  


  /**
   * Test the EncryptionUtil
   
  public static void main(String[] args) {

   
  }
}*/