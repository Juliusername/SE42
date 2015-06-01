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

    public Bid() {}
    
    public Bid(User buyer, Money amount) {
        this.buyer = buyer;
        this.amount = amount;
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
    
    public void setId(Long id)
    {
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
}
