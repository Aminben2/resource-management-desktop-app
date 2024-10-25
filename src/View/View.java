package View;

import Controller.CategoryController;
import Controller.ProductController;
import Model.Category;
import Model.Product;
import Util.EntityManagerProvider;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class View extends JFrame {

  private final JTable productTable;
  private final JTable categoryTable;
  private final DefaultTableModel productTableModel;
  private final DefaultTableModel categoryTableModel;
  private final CardLayout cardLayout;
  private final JPanel mainPanel;
  JTextField descriptionField,
      priceField,
      quantityField,
      sdrField,
      categoryIdField,
      descNameField,
      descDescriptionField;
  private CategoryController categoryController;
  private ProductController productController;
  private List<Product> products = new ArrayList<>();
  private List<Category> categories = new ArrayList<>();
  private JComboBox<Category> categoryComboBox;
  private JComboBox<Category> categoryFilterList;

  public View() {
    super("Resource Management");

    productController = new ProductController();
    categoryController = new CategoryController();

    productTableModel =
        new DefaultTableModel(
            new Object[] {"ID", "Description", "Price", "Quantity", "Sdr", "Category"}, 0);
    categoryTableModel = new DefaultTableModel(new Object[] {"ID", "Name", "Description"}, 0);
    productTable = new JTable(productTableModel);
    categoryTable = new JTable(categoryTableModel);

    loadProductData();
    loadCategoryData();

    cardLayout = new CardLayout();
    mainPanel = new JPanel(cardLayout);

    JPanel productPanel = new JPanel(new BorderLayout());
    productPanel.add(new JScrollPane(productTable), BorderLayout.CENTER);
    productPanel.add(createButtonPanel("Product"), BorderLayout.SOUTH);

    JPanel categoryPanel = new JPanel(new BorderLayout());
    categoryPanel.add(new JScrollPane(categoryTable), BorderLayout.CENTER);
    categoryPanel.add(createButtonPanel("Category"), BorderLayout.SOUTH);

    mainPanel.add(productPanel, "Product");
    mainPanel.add(categoryPanel, "Category");

    JPanel topPanel = new JPanel(new BorderLayout());
    JPanel navigationPanel = new JPanel();
    JButton productViewButton = new JButton("View Products");
    JButton categoryViewButton = new JButton("View Categories");
    navigationPanel.add(productViewButton);
    navigationPanel.add(categoryViewButton);
    topPanel.add(navigationPanel, BorderLayout.WEST);
    categoryFilterList = new JComboBox<>();
    Category c = new Category();
    c.setName("All products");
    categoryFilterList.addItem(c);
    categoryFilterList.addActionListener(
        actionEvent -> {
          Category selectedCategory = (Category) categoryFilterList.getSelectedItem();
          if (selectedCategory != null && selectedCategory.getId() != null) {
            filterProducts(selectedCategory.getId());
          }else {
            loadProductData();
          }
        });
    categories.forEach(categoryFilterList::addItem);
    topPanel.add(categoryFilterList, BorderLayout.EAST);
    add(topPanel, BorderLayout.NORTH);

    productViewButton.addActionListener(
        e -> {
          cardLayout.show(mainPanel, "Product");
          categoryFilterList.setVisible(true);
        });
    categoryViewButton.addActionListener(
        e -> {
          cardLayout.show(mainPanel, "Category");
          categoryFilterList.setVisible(false);
        });

    setLayout(new BorderLayout());
    add(topPanel, BorderLayout.NORTH);
    add(mainPanel, BorderLayout.CENTER);

    setSize(600, 400);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    this.addWindowListener(
        new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            EntityManagerProvider.getEntityManager().close();
          }
        });
  }

  private void filterProducts(Long id) {
    productTableModel.setRowCount(0);
    products = productController.getProductsByCategory(id);
    products.forEach(
        product ->
            productTableModel.addRow(
                new Object[] {
                  product.getId(),
                  product.getDescription(),
                  product.getPrice(),
                  product.getQuantity(),
                  product.getSdr(),
                  product.getCategory().getId()
                }));
    ;
  }

  private JPanel createButtonPanel(String resourceType) {
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

    JButton addButton = new JButton("Add " + resourceType);
    JButton updateButton = new JButton("Update " + resourceType);
    JButton deleteButton = new JButton("Delete " + resourceType);
    JButton refreshButton = new JButton("Refresh " + resourceType);

    buttonPanel.add(addButton);
    buttonPanel.add(updateButton);
    buttonPanel.add(deleteButton);
    buttonPanel.add(refreshButton);

    addButton.addActionListener(e -> createResource(resourceType));
    updateButton.addActionListener(e -> this.updateResource(resourceType));
    deleteButton.addActionListener(e -> this.deleteResource(resourceType));
    refreshButton.addActionListener(
        e -> {
          loadProductData();
          loadCategoryData();
          categoryFilterList.setSelectedIndex(0);
        });

    return buttonPanel;
  }

  public void deleteResource(String resourceType) {
    int selectedRow;

    if (resourceType.equals("Product")) {
      selectedRow = productTable.getSelectedRow();
      if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a product to delete.");
        return;
      }

      Long id = (Long) productTableModel.getValueAt(selectedRow, 0);

      int confirmation =
          JOptionPane.showConfirmDialog(
              this,
              "Are you sure you want to delete this product?",
              "Delete Confirmation",
              JOptionPane.YES_NO_OPTION);

      if (confirmation == JOptionPane.YES_OPTION) {
        productController.deleteProductById(id);
        loadProductData();
      }

    } else if (resourceType.equals("Category")) {
      selectedRow = categoryTable.getSelectedRow();
      if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a category to delete.");
        return;
      }

      Long id = (Long) categoryTableModel.getValueAt(selectedRow, 0);

      int confirmation =
          JOptionPane.showConfirmDialog(
              this,
              "Are you sure you want to delete this category?",
              "Delete Confirmation",
              JOptionPane.YES_NO_OPTION);

      if (confirmation == JOptionPane.YES_OPTION) {
        categoryController.deteleCategory(id);
        loadCategoryData();
      }
    }
  }

  public JPanel updateProductModel(int selectedRow) {
    long productId = (long) productTableModel.getValueAt(selectedRow, 0);
    String description = (String) productTableModel.getValueAt(selectedRow, 1);
    double price = (double) productTableModel.getValueAt(selectedRow, 2);
    long quantity = (long) productTableModel.getValueAt(selectedRow, 3);
    long sdr = (long) productTableModel.getValueAt(selectedRow, 4);
    long categoryId = (long) productTableModel.getValueAt(selectedRow, 5);

    descriptionField = new JTextField(description);
    priceField = new JTextField(String.valueOf(price));
    sdrField = new JTextField(String.valueOf(sdr));
    quantityField = new JTextField(String.valueOf(quantity));
    categoryIdField = new JTextField(String.valueOf(categoryId));

    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.add(new JLabel("Description:"));
    panel.add(descriptionField);
    panel.add(new JLabel("Price:"));
    panel.add(priceField);
    panel.add(new JLabel("Quantity:"));
    panel.add(quantityField);
    panel.add(new JLabel("Category ID:"));
    panel.add(categoryIdField);

    return panel;
  }

  public JPanel updateCategoryModel(int selectedRow) {
    long categoryId = (long) categoryTableModel.getValueAt(selectedRow, 0);
    String name = (String) categoryTableModel.getValueAt(selectedRow, 1);
    String description = (String) categoryTableModel.getValueAt(selectedRow, 2);

    descNameField = new JTextField(name);
    descDescriptionField = new JTextField(description);

    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.add(new JLabel("Name:"));
    panel.add(descNameField);
    panel.add(new JLabel("Description:"));
    panel.add(descDescriptionField);
    return panel;
  }

  public void updateResource(String recourceType) {
    int selectedRow;
    if (recourceType.equals("Product")) {
      selectedRow = productTable.getSelectedRow();
      if (selectedRow != -1) {
        int result =
            JOptionPane.showConfirmDialog(
                this,
                updateProductModel(selectedRow),
                "Update Product",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
          String updatedDescription = descriptionField.getText();
          double updatedPrice = Double.parseDouble(priceField.getText());
          long updatedQuantity = Integer.parseInt(quantityField.getText());
          long updatedSdr = Long.parseLong(sdrField.getText());
          long id = (Long) productTableModel.getValueAt(selectedRow, 0);
          long updatedCategoryId = Long.parseLong(categoryIdField.getText());
          Category category = categoryController.getCategoryById(updatedCategoryId);
          Product updatedProduct =
              new Product(updatedDescription, updatedPrice, updatedQuantity, category, updatedSdr);
          productController.updateProduct(id, updatedProduct);
          loadProductData();
        }
      } else {
        JOptionPane.showMessageDialog(this, "Please select a product to update.");
      }
    } else {
      selectedRow = categoryTable.getSelectedRow();
      if (selectedRow != -1) {
        int result =
            JOptionPane.showConfirmDialog(
                this,
                updateCategoryModel(selectedRow),
                "Update Category",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
          String updatedName = descNameField.getText();
          String updatedDescription = descDescriptionField.getText();
          long id = (Long) categoryTableModel.getValueAt(selectedRow, 0);

          Category updatedCategory = new Category(updatedName, updatedDescription);
          categoryController.updateCategory(id, updatedCategory);
          loadCategoryData();
        }
      } else {
        JOptionPane.showMessageDialog(this, "Please select a category to update.");
      }
    }
  }

  public JPanel createProductModel() {
    descriptionField = new JTextField();
    priceField = new JTextField();
    quantityField = new JTextField();
    sdrField = new JTextField();

    categoryComboBox = new JComboBox<>();
    List<Category> categories = categoryController.getAllCategories();
    for (Category category : categories) {
      categoryComboBox.addItem(category);
    }

    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.add(new JLabel("Description:"));
    panel.add(descriptionField);
    panel.add(new JLabel("Price:"));
    panel.add(priceField);
    panel.add(new JLabel("Quantity:"));
    panel.add(quantityField);
    panel.add(new JLabel("Category:"));
    panel.add(categoryComboBox);
    panel.add(new JLabel("SDR:"));
    panel.add(sdrField);

    return panel;
  }

  public JPanel createCategoryModel() {
    descNameField = new JTextField();
    descDescriptionField = new JTextField();

    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.add(new JLabel("Name:"));
    panel.add(descNameField);
    panel.add(new JLabel("Description:"));
    panel.add(descDescriptionField);
    return panel;
  }

  public void createResource(String resourceType) {
    if (resourceType.equals("Product")) {
      int result =
          JOptionPane.showConfirmDialog(
              this,
              createProductModel(),
              "Create New Product",
              JOptionPane.OK_CANCEL_OPTION,
              JOptionPane.PLAIN_MESSAGE);

      if (result == JOptionPane.OK_OPTION) {
        String description = descriptionField.getText();
        double price = Double.parseDouble(priceField.getText());
        long quantity = Long.parseLong(quantityField.getText());
        long sdr = Long.parseLong(sdrField.getText());
        Category selectedCategory = (Category) categoryComboBox.getSelectedItem();

        Category category = categoryController.getCategoryById(selectedCategory.getId());
        Product newProduct = new Product(description, price, quantity, category, sdr);
        productController.createProduct(newProduct);

        loadProductData();
      }
    } else if (resourceType.equals("Category")) {
      int result =
          JOptionPane.showConfirmDialog(
              this,
              createCategoryModel(),
              "Create New Category",
              JOptionPane.OK_CANCEL_OPTION,
              JOptionPane.PLAIN_MESSAGE);

      if (result == JOptionPane.OK_OPTION) {
        String name = descNameField.getText();
        String description = descDescriptionField.getText();

        Category newCategory = new Category(name, description);
        categoryController.createCategory(newCategory);

        loadCategoryData();
      }
    }
  }

  private void loadProductData() {
    productTableModel.setRowCount(0);
    products = productController.getAllProducts();
    products.forEach(
        product ->
            productTableModel.addRow(
                new Object[] {
                  product.getId(),
                  product.getDescription(),
                  product.getPrice(),
                  product.getQuantity(),
                  product.getSdr(),
                  product.getCategory().getId()
                }));
  }

  private void loadCategoryData() {
    categoryTableModel.setRowCount(0);

    categories = categoryController.getAllCategories();
    categories.forEach(
        category ->
            categoryTableModel.addRow(
                new Object[] {category.getId(), category.getName(), category.getDescription()}));
  }
}
