/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auction.domain;

import javax.persistence.Entity;

/**
 *
 * @author Julius
 */
@Entity
public class Furniture extends Item {
    
    private String material;

    public Furniture() {
    }

    public Furniture(User seller, Category category, String description, String material) {
        super(seller, category, description);
        this.material = material;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
    
    @Override
    public boolean equals(Object o) {
        System.out.println("Furniture Equals");
        
        if (o == null || !(o instanceof Furniture)) {
            return false;
        }
        
        System.out.println("Furniture is not null, and instance of Furniture");
        
        Furniture furniture = (Furniture) o;
        
        /*
        if (super.getSeller().equals(furniture.getSeller())) {
            System.out.println("Seller OK");
        }
        if (super.getCategory().equals(furniture.getCategory())) {
            System.out.println("Category OK");
        }
        if (super.getDescription().equals(furniture.getDescription())) {
            System.out.println("Description OK");
        }
        if (this.material.equals(furniture.getMaterial())) {
            System.out.println("Material OK");
        }
        */
        
        return (super.getSeller().equals(furniture.getSeller())
             && super.getCategory().equals(furniture.getCategory())
             && super.getDescription().equals(furniture.getDescription())
             && this.material.equals(furniture.getMaterial())
        );
    }
}
