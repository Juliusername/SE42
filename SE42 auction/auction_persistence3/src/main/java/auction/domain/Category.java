package auction.domain;

import java.io.Serializable;
import javax.persistence.*;

public class Category implements Serializable {
  
    private String description;
    
    private Category() {
        description = "undefined";
    }

    public Category(String description) {
        this.description = description;
    }
    
    public String getDiscription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
            
}
