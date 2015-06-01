/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auction.service;

import auction.domain.Bid;
import auction.domain.Category;
import auction.domain.Item;
import auction.domain.User;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import nl.fontys.util.Money;

/**
 *
 * @author Twan
 */
@WebService
public class Registration
{
    private final RegistrationMgr registrationMgr = new RegistrationMgr();
    
    @WebMethod
    public User registerUser(String email) {
        return this.registrationMgr.registerUser(email);
    }
    
    @WebMethod
    public User getUser(String user) {
        return this.registrationMgr.getUser(user);
    }
}