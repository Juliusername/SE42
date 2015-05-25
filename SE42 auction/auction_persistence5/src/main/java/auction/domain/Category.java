package auction.domain;

import java.io.Serializable;
import java.util.Objects;
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
            
    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Category)) {
            return false;
        }
        
        Category category = (Category) o;
        
        return (this.description.equals(category.getDiscription())
        );
    }
}
