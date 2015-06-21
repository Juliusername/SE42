/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se42.encryption;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Twan
 */
public class SE42Encryption_2
{
    public final static String SIGNER      = "Lk";
    public final static String ALGORITHM   = "SHA1withRSA";
    public final static String INPUT_FILE  = "C:/keys/INPUT.EXT";
    public final static String OUTPUT_FILE = "C:/keys/INPUT(SIGNEDBYLK).EXT";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        ObjectInputStream inputStream;
        try {
            // lees private key
            inputStream = new ObjectInputStream(new FileInputStream(SE42Encryption_1.PRIVATE_KEY_FILE));
            final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
            
            // lees te signeren bestand
            inputStream = new ObjectInputStream(new FileInputStream(INPUT_FILE));
            final String originalText = (String) inputStream.readObject();
            
            // signeer bestand
            Signature signature = Signature.getInstance(ALGORITHM);
            signature.initSign(privateKey);
            signature.update(originalText.getBytes());

            String signed = Base64.encode(signature.sign());
            
            // schrijf signer, signature lengte, signature en content naar nieuw bestand
            ObjectOutputStream fileOut = new ObjectOutputStream(new FileOutputStream(OUTPUT_FILE));
            fileOut.writeObject(SIGNER);
            fileOut.writeInt(signed.length());
            fileOut.writeObject(signed);
            fileOut.writeObject(originalText);
             
        } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException | InvalidKeyException | SignatureException ex) {
            Logger.getLogger(SE42Encryption_2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
