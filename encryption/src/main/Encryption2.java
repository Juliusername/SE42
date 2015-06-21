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
public class Encryption2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        /*
        De tweede applicatie leest een file die een private sleutel bevat en een andere file (die we even INPUT.EXT zullen noemen).
        Verder wordt de naam van de ondertekenaar (bijv. Lk) opgegeven.
        Als output wordt een file met de naam INPUT(SIGNEDBYLK).EXT geproduceerd met de volgende inhoud: de lengte (in bytes) van de digitale handtekening,
        de digitale handtekening (gemaakt met het algoritme 'SHA1withRSA') en de inhoud van file INPUT.EXT.
        */
        RSATest.main(new String[] {"", "c://JavaEncryption//INPUT.ext", "c://JavaEncryption//INPUT(SIGNEDBYLK).ext", "c://JavaEncryption//privatekey.ext"});
    }
    
}
