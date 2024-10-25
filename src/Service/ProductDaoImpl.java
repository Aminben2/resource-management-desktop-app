package Service;

import Dao.Dao;
import Model.Category;
import Model.Product;
import Util.EntityManagerProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDaoImpl implements Dao<Product> {

  public List<Product> getProductsByCategory(Long categoryId) {
    List<Product> products = new ArrayList<>();
    if (categoryId == null) {
      return products;
    }

    EntityManager entityManager = EntityManagerProvider.getEntityManager();

    try {
      entityManager.getTransaction().begin();
      products = entityManager
              .createQuery("SELECT p FROM Product p WHERE p.category.id = :categoryId", Product.class)
              .setParameter("categoryId", categoryId)
              .getResultList();
      entityManager.getTransaction().commit();
    } catch (PersistenceException e) {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback();
      }
      throw new RuntimeException("Failed to fetch products by category ID: " + e.getMessage(), e);
    } finally {
      entityManager.close();
    }

    return products;
  }


  @Override
  public Optional<Product> getById(Long id) {
    if (id == null) {
      return Optional.empty();
    }

    EntityManager entityManager = EntityManagerProvider.getEntityManager();

    Product product = null;
    try {
      entityManager.getTransaction().begin();
      product = entityManager.find(Product.class, id);
      entityManager.getTransaction().commit();
    } catch (PersistenceException e) {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback();
      }
      throw new RuntimeException("Failed to fetch product by id: " + e.getMessage(), e);
    } finally {
      entityManager.close();
    }

    return Optional.ofNullable(product);
  }

  @Override
  public List<Product> getAll() {
    EntityManager entityManager = EntityManagerProvider.getEntityManager();

    List<Product> products = null;
    try {
      entityManager.getTransaction().begin();
      products =
          entityManager.createQuery("SELECT p FROM Product p", Product.class).getResultList();
      entityManager.getTransaction().commit();
    } catch (PersistenceException e) {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback();
      }
      throw new RuntimeException("Failed to fetch products: " + e.getMessage(), e);
    } finally {
      entityManager.close();
    }

    return products;
  }

  @Override
  public Optional<Product> update(Long id, Product obj) {
    if (obj == null || id == null) {
      return Optional.empty();
    }
    EntityManager entityManager = EntityManagerProvider.getEntityManager();
    try {
      entityManager.getTransaction().begin();

      Optional<Product> productOptional = getById(id);
      if (productOptional.isPresent()) {
        Product existingProduct = productOptional.get();
        existingProduct.setCategory(obj.getCategory());
        existingProduct.setDescription(obj.getDescription());
        existingProduct.setSdr(obj.getSdr());
        existingProduct.setPrice(obj.getPrice());
        existingProduct.setQuantity(obj.getQuantity());
        entityManager.merge(existingProduct);

        entityManager.getTransaction().commit();
        return Optional.of(existingProduct);
      } else {
        return Optional.empty();
      }
    } catch (PersistenceException e) {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback();
      }
      throw new RuntimeException("Failed to update product: " + e.getMessage(), e);
    } finally {
      entityManager.close();
    }
  }

  @Override
  public Product create(Product obj) {
    if (obj == null) {
      System.out.println("Product cannot be null.");
      return null;
    }

    EntityManager entityManager = EntityManagerProvider.getEntityManager();
    try {
      entityManager.getTransaction().begin();
      entityManager.persist(obj);
      entityManager.getTransaction().commit();
      return obj;
    } catch (PersistenceException e) {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback();
      }
      System.out.println("Failed to create product: " + e.getMessage());
      return null;
    } finally {
      entityManager.close();
    }
  }

  @Override
  public int deleteByid(Long id) {
    if (id == null) {
      return 0;
    }

    EntityManager entityManager = EntityManagerProvider.getEntityManager();
    try {
      entityManager.getTransaction().begin();
      Product product = entityManager.find(Product.class, id);
      if (product != null) {
        entityManager.remove(product);
        entityManager.getTransaction().commit();
        return 1;
      } else {
        return 0;
      }
    } catch (PersistenceException e) {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback();
      }
      System.out.println("Failed to delete product: " + e.getMessage());
      return 0;
    } finally {
      entityManager.close();
    }
  }
}
