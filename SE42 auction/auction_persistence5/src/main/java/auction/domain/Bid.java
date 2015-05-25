package auction.domain;

import java.io.Serializable;
import javax.persistence.*;
import nl.fontys.util.FontysTime;
import nl.fontys.util.Money;

@Entity
public class Bid implements Serializable {

    @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private FontysTime time;
    
    @ManyToOne
    private User buyer;
    
    private Money amount;
    
    @OneToOne
    @JoinColumn(nullable = false)
    private Item item;

    public Bid() {}
    
    public Bid(User buyer, Money amount, Item item) {
        this.buyer = buyer;
        this.amount = amount;
        this.item = item;
        this.time = FontysTime.now();
    }

    public Long getId() {
        return id;
    }
    
    public FontysTime getTime() {
        return time;
    }

    public User getBuyer() {
        return buyer;
    }

    public Money getAmount() {
        return amount;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setTime(FontysTime time) {
        this.time = time;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }
    
    public Item getItem() {
        return item;
    }
    
    public void setItem(Item item) {
        this.item = item;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Bid)) {
            return false;
        }
        
        Bid bid = (Bid) o;
        
        if (this.buyer.equals(bid.getBuyer())) {
            System.out.println("Buyer OK");
        }
        if (this.item.equals(bid.getItem())) {
            System.out.println("Item OK");
        }
        if (this.amount.equals(bid.getAmount())) {
            System.out.println("Amount OK");
        }
        //if (this.time.equals(bid.getTime())) {
        //    System.out.println("Time OK");
        //}
        
        return (this.buyer.equals(bid.getBuyer())
             && this.item.equals(bid.getItem())
             && this.amount.equals(bid.getAmount())
             //&& this.time.equals(bid.getTime())
        );
    }
}
