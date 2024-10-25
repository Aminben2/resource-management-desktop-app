package Controller;

import Model.Category;
import Service.CategoryDaoImp;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;

public class CategoryController {
  private final CategoryDaoImp categoryDaoImp;

  public CategoryController() {
    this.categoryDaoImp = new CategoryDaoImp();
  }

  public List<Category> getAllCategories() {
    return categoryDaoImp.getAll();
  }

  public Category getCategoryById(Long id) {
    return categoryDaoImp
        .getById(id)
        .orElseThrow(() -> new EntityNotFoundException("Category not found"));
  }

  public Category updateCategory(Long id, Category category) {
    return categoryDaoImp
        .update(id, category)
        .orElseThrow(() -> new EntityNotFoundException("Category not found"));
  }

  public Category createCategory(Category category) {
    return categoryDaoImp.create(category);
  }

  public int deteleCategory(Long id) {
    return categoryDaoImp.deleteByid(id);
  }
}
