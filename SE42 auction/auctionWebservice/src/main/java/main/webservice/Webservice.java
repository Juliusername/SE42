/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.webservice;

import auction.service.Auction;
import auction.service.Registration;
import javax.xml.ws.Endpoint;

/**
 *
 * @author Twan
 */
public class Webservice {
     public static void main(String[] args) {
        System.out.println("Starting webservices");
        Endpoint.publish("http://localhost:8080/RegistrationService", new Registration());
        System.out.println("-- RegistrationService deployed");
        Endpoint.publish("http://localhost:8080/AuctionService", new Auction());
        System.out.println("-- AuctionService deployed");
        
    }
   
}
