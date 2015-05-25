/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auction.domain;

import javax.persistence.*;

/**
 *
 * @author Julius
 */
@Entity
public class Painting extends Item {
    
    private String title;
    private String painter;
    
    public Painting() {
    }

    public Painting(User seller, Category category, String description, String title, String painter) {
        super(seller, category, description);
        this.title = title;
        this.painter = painter;
    }

    public String getTitle() {
        return title;
    }

    public String getPainter() {
        return painter;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPainter(String painter) {
        this.painter = painter;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Furniture)) {
            return false;
        }
        
        Painting painting = (Painting) o;
        
        return (super.getSeller().equals(painting.getSeller())
             && super.getCategory().equals(painting.getCategory())
             && super.getDescription().equals(painting.getDescription())
             && this.title.equals(painting.getTitle())
             && this.painter.equals(painting.getPainter())
        );
    }
}
