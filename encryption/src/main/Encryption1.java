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
public class Encryption1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        /*
        De eerste applicatie genereert een publieke sleutel van 1024 bits volgens het RSA-algoritme 
        en de bijbehorende private sleutel en schrijft beide sleutels in afzonderlijke files weg.
        */
        RSATest.main(new String[] {"-genkey", "c://JavaEncryption//publickey.ext", "c://JavaEncryption//privatekey.ext"});
    }
    
}
