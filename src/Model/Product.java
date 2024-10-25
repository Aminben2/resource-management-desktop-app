package Model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "product")
public class Product implements Serializable,Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private Double price;
    private Long quantity;
    private Long sdr;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Product(String description, Double price, Long quantity, Category category, Long sdr) {
        super();
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.sdr = sdr;
    }
    public Product(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getSdr() {
        return sdr;
    }

    public void setSdr(Long sdr) {
        this.sdr = sdr;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", sdr=" + sdr +
                ", category=" + category +
                '}';
    }
}
