/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import rsa.RSATest;

/**
 *
 * @author Julius
 */
public class Encryption3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        /*
        De derde applicatie leest file INPUT(SIGNEDBYLK).EXT en de publieke sleutel van de ondertekenaar en verifieert de handtekening.
        Het resultaat (wel of niet goedgekeurd) wordt gemeld.
        Als de handtekening klopt wordt bovendien de oorspronkelijke file INPUT.EXT gereconstrueerd. 
        */
        RSATest.main(new String[] {"encrypt", "c://JavaEncryption//INPUT(SIGNEDBYLK).ext", "c://JavaEncryption//OUTPUT.ext", "c://JavaEncryption//publickey.ext"});
    }
    
}
