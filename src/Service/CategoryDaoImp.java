package Service;

import Dao.Dao;
import Model.Category;
import Util.EntityManagerProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryDaoImp implements Dao<Category> {

  @Override
  public Optional<Category> getById(Long id) {
    if (id == null) {
      return Optional.empty();
    }
    EntityManager entityManager = EntityManagerProvider.getEntityManager();
    try {
      entityManager.getTransaction().begin();
      Category category = entityManager.find(Category.class, id);
      entityManager.getTransaction().commit();
      return Optional.ofNullable(category);
    } catch (Exception e) {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback();
      }
      return Optional.empty();
    } finally {
      entityManager.close();
    }
  }

  @Override
  public List<Category> getAll() {
    EntityManager entityManager = EntityManagerProvider.getEntityManager();
    try {
      entityManager.getTransaction().begin();
      List<Category> categories = entityManager.createQuery("SELECT c FROM Category c", Category.class).getResultList();
      entityManager.getTransaction().commit();
      return categories;
    } catch (Exception e) {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback();
      }
      return List.of();
    } finally {
      entityManager.close();
    }
  }

  @Override
  public Optional<Category> update(Long id,Category obj) {
    if (obj == null || obj.getId() == null) {
      return Optional.empty();
    }
    EntityManager entityManager = EntityManagerProvider.getEntityManager();
    try {
      entityManager.getTransaction().begin();
      Category category = entityManager.find(Category.class,id);
      if (category != null) {
        category.setName(obj.getName());
        category.setDescription(obj.getDescription());
        entityManager.merge(category);
        entityManager.getTransaction().commit();
        return Optional.of(category);
      }
      entityManager.getTransaction().commit();
      return Optional.empty();
    } catch (Exception e) {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback();
      }
      return Optional.empty();
    } finally {
      entityManager.close();
    }
  }

  @Override
  public Category create(Category obj) {
    if (obj == null) {
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
      Category category = entityManager.find(Category.class, id);
      if (category != null) {
        entityManager.remove(category);
        entityManager.getTransaction().commit();
        return 1;
      }
      entityManager.getTransaction().commit();
      return 0;
    } catch (Exception e) {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback();
      }
      return 0;
    } finally {
      entityManager.close();
    }
  }
}
