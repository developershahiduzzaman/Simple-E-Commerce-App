package com.example.ecmmerce.Model;

public class Product {

    private int id;
    private String name;
    private String description;
    private String price;
    private String image;
    private int category_id;
    private Category category;  // nested object

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getPrice() { return price; }
    public String getImage() { return image; }
    public int getCategory_id() { return category_id; }
    public Category getCategory() { return category; }
}
