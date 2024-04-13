package org.dmitriy.Restaurant.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private String category;
    private int price;

    @OneToOne(cascade = {CascadeType.ALL, CascadeType.REMOVE}, fetch = FetchType.LAZY, mappedBy = "product") // у одного товара одна картинка
    private Image image;

    public void addImage(Image image) {
        this.image = null;
        image.setProduct(this);
        this.image = image;
    }

}
