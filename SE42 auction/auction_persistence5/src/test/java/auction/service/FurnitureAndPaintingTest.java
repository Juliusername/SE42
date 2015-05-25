package auction.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import util.DatabaseCleaner;
import auction.domain.*;
import java.util.Iterator;
import nl.fontys.util.Money;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FurnitureAndPaintingTest {

    final EntityManagerFactory emf = Persistence.createEntityManagerFactory("nl.fhict.se42_auction_jar_1.0-SNAPSHOTPU");
    final EntityManager em = emf.createEntityManager();
    private AuctionMgr auctionMgr;
    private RegistrationMgr registrationMgr;
    private SellerMgr sellerMgr;

    public FurnitureAndPaintingTest() {
    }

    @Before
    public void setUp() throws Exception {
        registrationMgr = new RegistrationMgr();
        auctionMgr = new AuctionMgr();
        sellerMgr = new SellerMgr();
        new DatabaseCleaner(em).clean();
    }

    @Test
    public void newFurniture() {
        String omsch = "omsch1";
        String iemand1 = "iemand1@def";
        User u1 = registrationMgr.registerUser(iemand1);
        User u2 = registrationMgr.registerUser("iemand2@def");
        Category cat = new Category("cat2");

        Item furniture1 = sellerMgr.offerFurniture(u1, cat, "broodkast", "ijzer");
        assertEquals("seller of item correct", furniture1.getSeller(), u1);

        User foundUser = registrationMgr.getUser(iemand1);
        Iterator<Item> it = foundUser.getOfferedItems();
        Item firstItem = it.next();
   //        int xxx = 22;
        
        assertEquals("item added in offeredItems", furniture1, firstItem);
        Item item2 = sellerMgr.offerPainting(u1, cat, omsch, "Nachtwacht", "Rembrandt");
        it = registrationMgr.getUser(iemand1).getOfferedItems();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertFalse(it.hasNext());

        //de volgende code verwijderen als Item abstract is
        Item item3 = sellerMgr.offerItem(u1, new Category("boek"), "The science of Discworld");
        it = registrationMgr.getUser(iemand1).getOfferedItems();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertFalse(it.hasNext());

        assertNull(furniture1.getHighestBid());
        Bid bid = auctionMgr.newBid(furniture1, u2, new Money(150000, Money.EURO));
        assertNotNull(furniture1.getHighestBid());
        
        Item foundFurniture = auctionMgr.getItem(furniture1.getId());
        
        System.out.println("Bid: " + bid.getBuyer().getEmail() + ", " + bid.getAmount().getValue() + ", " + bid.getItem().getDescription());
        System.out.println("foundFurniture: " + foundFurniture.getHighestBid().getBuyer().getEmail() + ", " + foundFurniture.getHighestBid().getAmount().getValue() + ", " + foundFurniture.getHighestBid().getItem().getDescription());
        
        int i = 3;
        assertEquals(foundFurniture.getHighestBid(), bid);
        assertTrue(foundFurniture.getClass() == Furniture.class);
    }
}

/*
Vraag 5 - Noteer de inhoud van de database na de test

BID
ID    AMOUNT        TIME            BUYER_ID    ITEM_ID
1     [BLOB-95 B]   [BLOB-64 B]     2           1

ITEM
ID      DTYPE       CATEGORY    DESCRIPTION                 SELLER_ID   MATERIAL    PAINTER     TITLE
1       Furniture   [BLOB-86 B] broodkast                   1           ijzer       NULL        NULL
2       Painting    [BLOB-86 B] omsch1                      1           NULL        Rembrandt   Nachtwacht
3       Item        [BLOB-86 B] The science of Discworld    1           NULL        NULL        NULL

USER
ID      EMAIL
1       iemand1@def
2       iemand2@def
*/

/*
Vraag 6 - Noteer de inhoud van de database na de test

BID
ID    AMOUNT        TIME            BUYER_ID    ITEM_ID
1     [BLOB-95 B]   [BLOB-64 B]     2           1

ITEM
ID      DTYPE       CATEGORY    DESCRIPTION                 SELLER_ID
1       Furniture   [BLOB-86 B] broodkast                   1
2       Painting    [BLOB-86 B] omsch1                      1
3       Item        [BLOB-86 B] The science of Discworld    1 

FURNITURE
ID      MATERIAL
1       ijzer

PAINTING
ID      PAINTER     TITLE
2       Rembrandt   Nachtwacht

USER
ID      EMAIL
1       iemand1@def
2       iemand2@def
*/

/*

/*
Vraag 7 - Noteer de inhoud van de database na de test

BID
ID    AMOUNT        TIME            BUYER_ID    ITEM_ID
1     [BLOB-95 B]   [BLOB-64 B]     2           1

ITEM
ID      CATEGORY    DESCRIPTION                 SELLER_ID
3       [BLOB-86 B] The science of Discworld    1 

FURNITURE
ID      CATEGORY    DESCRIPTION     SELLER_ID   MATERIAL
1       [BLOB-86 B] broodkast       1           ijzer

PAINTING
ID      CATEGORY    DESCRIPTION     SELLER_ID   PAINTER     TITLE
2       [BLOB-86 B] omsch1          1           Rembrandt   Nachtwacht

USER
ID      EMAIL
1       iemand1@def
2       iemand2@def

SEQUENCE
SEQ_NAME        SEQ_COUNT
SEQ_GEN_TABLE   50
*/