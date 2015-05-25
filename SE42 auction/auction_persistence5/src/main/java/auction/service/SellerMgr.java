package auction.service;

import auction.dao.ItemDAO;
import auction.dao.ItemDAOJPAImpl;
import auction.domain.Category;
import auction.domain.Furniture;
import auction.domain.Item;
import auction.domain.Painting;
import auction.domain.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class SellerMgr {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("nl.fhict.se42_auction_jar_1.0-SNAPSHOTPU");
    
    /**
     * @param seller
     * @param cat
     * @param description
     * @return het item aangeboden door seller, behorende tot de categorie cat
     *         en met de beschrijving description
     */
    public Item offerItem(User seller, Category cat, String description) {
        EntityManager em = emf.createEntityManager();
        ItemDAO itemDAO = new ItemDAOJPAImpl(em);
        
        Item item = new Item(seller, cat, description);
        
        em.getTransaction().begin();
        try {
            
            itemDAO.create(item);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        
        return item;
    }
    
    public Furniture offerFurniture(User seller, Category cat, String description, String material) {
        EntityManager em = emf.createEntityManager();
        ItemDAO itemDAO = new ItemDAOJPAImpl(em);
        
        Furniture furniture = new Furniture(seller, cat, description, material);
        
        em.getTransaction().begin();
        try {
            
            itemDAO.create(furniture);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        
        return furniture;
    }
    
    public Painting offerPainting(User seller, Category cat, String description, String title, String painter) {
        EntityManager em = emf.createEntityManager();
        ItemDAO itemDAO = new ItemDAOJPAImpl(em);
        
        Painting painting = new Painting(seller, cat, description, title, painter);
        
        em.getTransaction().begin();
        try {
            
            itemDAO.create(painting);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        
        return painting;
    }
    
     /**
     * @param item
     * @return true als er nog niet geboden is op het item. Het item word verwijderd.
     *         false als er al geboden was op het item.
     */
    public boolean revokeItem(Item item) {
        if (item.getHighestBid() == null) {
            
            EntityManager em = emf.createEntityManager();
            ItemDAO itemDAO = new ItemDAOJPAImpl(em);

            String description = item.getDescription();
            
            em.getTransaction().begin();
            try {
                itemDAO.remove(item);
                em.getTransaction().commit();
                
                System.out.println("Item with description: " + description + "has been revoked");
            } catch (Exception e) {
                e.printStackTrace();
                em.getTransaction().rollback();
            } finally {
                em.close();
            }
        
            return true;
        }
        return false;
    }
}
