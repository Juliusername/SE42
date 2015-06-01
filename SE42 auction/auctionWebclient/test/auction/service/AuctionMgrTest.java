package auction.service;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;

public class AuctionMgrTest {

    auction.service.RegistrationService registrationService = new auction.service.RegistrationService();
    auction.service.Registration registrationMgr = registrationService.getRegistrationPort();
    
    auction.service.AuctionService auctionService = new auction.service.AuctionService();
    auction.service.Auction auctionMgr = auctionService.getAuctionPort();
    auction.service.Auction sellerMgr  = auctionService.getAuctionPort();
    


    @Test
    public void getItem() {
        System.out.println("ActionMgrTest: getItem()");
        
        String email = "xx2@nl";
        String omsch = "omsch";

        User seller1 = registrationMgr.registerUser(email);
        Category cat = new Category();
        cat.setDescription("cat2");
        
        Item item1 = sellerMgr.offerItem(seller1, cat, omsch);
        Item item2 = auctionMgr.getItem(item1.getId());
        assertEquals(omsch, item2.getDescription());
        assertEquals(email, item2.getSeller().getEmail());
    }

    @Test
    public void findItemByDescription() {
        System.out.println("ActionMgrTest: findItemByDescription()");
        
        String email3 = "xx3@nl";
        String omsch = "omsch";
        String email4 = "xx4@nl";
        String omsch2 = "omsch2";

        User seller3 = registrationMgr.registerUser(email3);
        User seller4 = registrationMgr.registerUser(email4);
        Category cat = new Category();
        cat.setDescription("cat3");
        Item item1 = sellerMgr.offerItem(seller3, cat, omsch);
        Item item2 = sellerMgr.offerItem(seller4, cat, omsch);

        List<Item> res = auctionMgr.findItemByDescription(omsch2);
        assertEquals(0, res.size());

        res = auctionMgr.findItemByDescription(omsch);
        assertEquals(2, res.size());

    }

    @Test
    public void newBid() {

        System.out.println("ActionMgrTest: newBid()");
        
        String email = "ss2@nl";
        String emailb = "bb@nl";
        String emailb2 = "bb2@nl";
        String omsch = "omsch_bb";

        User seller = registrationMgr.registerUser(email);
        User buyer = registrationMgr.registerUser(emailb);
        User buyer2 = registrationMgr.registerUser(emailb2);
        // eerste bod
        Category cat = new Category();
        cat.setDescription("cat9");
        Item item1 = sellerMgr.offerItem(seller, cat, omsch);
        
        Money amount1 = new Money();
        amount1.setCents(10);
        amount1.setCurrency("eur");
        Bid new1 = auctionMgr.newBid(item1, buyer, amount1);
        assertEquals(emailb, new1.getBuyer().getEmail());

        Money amount2 = new Money();
        amount2.setCents(9);
        amount2.setCurrency("eur");
        // lager bod
        Bid new2 = auctionMgr.newBid(item1, buyer2, amount2);
        assertNull(new2);

        Money amount3 = new Money();
        amount3.setCents(11);
        amount3.setCurrency("eur");
        // hoger bod
        Bid new3 = auctionMgr.newBid(item1, buyer2, amount3);
        assertEquals(emailb2, new3.getBuyer().getEmail());
    }
}
