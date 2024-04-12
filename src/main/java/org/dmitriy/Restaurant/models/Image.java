package org.dmitriy.Restaurant.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "images")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String originalFileName;
    private Long size;
    private String contentType;
    @Column(columnDefinition = "bytea")  // это обязательно (меняет тип данных в бд)
    private byte[] bytes;

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER) // мы связываем продукт и фотографию. Если мы получим фотографию, то легко сможем получить и продукт. На один товар приходится много фотографий
    private Product product;
}
