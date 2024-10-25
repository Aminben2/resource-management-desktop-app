package Controller;

import Model.Product;
import Service.ProductDaoImpl;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;

public class ProductController {
  private final ProductDaoImpl productDaoImp;

  public ProductController() {
    this.productDaoImp = new ProductDaoImpl();
  }

  public List<Product> getAllProducts() {
    return productDaoImp.getAll();
  }

  public List<Product> getProductsByCategory(Long id){
    return productDaoImp.getProductsByCategory(id);
  }

  public Product getProductById(Long id) {
    return productDaoImp
        .getById(id)
        .orElseThrow(() -> new EntityNotFoundException("Product not found"));
  }

  public Product updateProduct(Long id, Product product) {
    return productDaoImp
        .update(id,product)
        .orElseThrow(() -> new EntityNotFoundException("Product not found"));
  }

  public Product createProduct(Product product) {
    return productDaoImp.create(product);
  }

  public int deleteProductById(Long id) {
    return productDaoImp.deleteByid(id);
  }
}
