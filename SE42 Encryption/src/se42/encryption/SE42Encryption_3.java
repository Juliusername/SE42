/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se42.encryption;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Twan
 */
public class SE42Encryption_3
{ 
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        ObjectInputStream inputStream;
        try {
            // lees gesigneerd bestand
            inputStream = new ObjectInputStream(new FileInputStream(SE42Encryption_2.OUTPUT_FILE));
            String signer  = (String) inputStream.readObject();
            int    length  = inputStream.readInt();
            byte[] signed  = Base64.decode((String) inputStream.readObject());
            String content = (String) inputStream.readObject();
            
            // lees public key
            inputStream = new ObjectInputStream(new FileInputStream(SE42Encryption_1.PUBLIC_KEY_FILE));
            final PublicKey publicKey = (PublicKey) inputStream.readObject();
            
            // controleer signature
            Signature signature = Signature.getInstance(SE42Encryption_2.ALGORITHM);
            signature.initVerify(publicKey);
            signature.update(content.getBytes());
            
            System.out.println("signer: " + signer);
            System.out.println("length: " + length);
            System.out.println("signature: " + String.valueOf(signed));
            System.out.println("content: " + content);
            
            System.out.println("check: " + signature.verify(signed));
            
        } catch (IOException | ClassNotFoundException | Base64DecodingException | NoSuchAlgorithmException | InvalidKeyException | SignatureException ex) {
            Logger.getLogger(SE42Encryption_3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
