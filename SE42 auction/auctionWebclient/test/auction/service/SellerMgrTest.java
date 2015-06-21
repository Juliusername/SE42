package auction.service;

import java.util.List;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;



public class SellerMgrTest {

    auction.service.RegistrationService registrationService = new auction.service.RegistrationService();
    auction.service.Registration registrationMgr = registrationService.getRegistrationPort();
    
    auction.service.AuctionService auctionService = new auction.service.AuctionService();
    auction.service.Auction auctionMgr = auctionService.getAuctionPort();
    auction.service.Auction sellerMgr  = auctionService.getAuctionPort();
  
    @Before
    public void cleanDatabase() {
        registrationMgr.cleanDatabase();
    }
    
    /**
     * Test of offerItem method, of class SellerMgr.
     */
    @Test
    public void testOfferItem() {
        String omsch = "omsch";

        User user1 = registrationMgr.registerUser("xx@nl");
        Category cat = new Category();
        cat.setDescription("cat1");
        Item item1 = sellerMgr.offerItem(user1, cat, omsch);
        assertEquals(omsch, item1.getDescription());
        assertNotNull(item1.getId());
    }

    /**
     * Test of revokeItem method, of class SellerMgr.
     */
    @Test
    public void testRevokeItem() {
        String omsch = "omsch";
        String omsch2 = "omsch2";
        
    
        User seller = registrationMgr.registerUser("sel@nl");
        User buyer = registrationMgr.registerUser("buy@nl");
        Category cat = new Category();
        cat.setDescription("cat1");
        
            // revoke before bidding
        Item item1 = sellerMgr.offerItem(seller, cat, omsch);
        boolean res = sellerMgr.revokeItem(item1);
        assertTrue(res);
        int count = auctionMgr.findItemByDescription(omsch).size();
        assertEquals(0, count);

        // revoke after bid has been made
        Item item2 = sellerMgr.offerItem(seller, cat, omsch2);
        
        Money amount = new Money();
        amount.setCents(100);
        amount.setCurrency("Euro");
        auctionMgr.newBid(item2, buyer, amount);
        boolean res2 = sellerMgr.revokeItem(item2);
        assertFalse(res2);
        int count2 = auctionMgr.findItemByDescription(omsch2).size();
        assertEquals(1, count2);
    }

}
