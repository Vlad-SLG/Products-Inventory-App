package com.example.test___;

import Domain.Product;
import Repo.BinaryFileRepo;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Comparator;

public class HelloController {

    public ListView<Product> listProduct;
    public Label labelId;
    public TextField textFieldId;
    public Label labelMarca;
    public TextField textFieldMarca;
    public Label labelName;
    public TextField textFieldName;
    public Label labelPrice;
    public TextField textFieldPrice;
    public Label labelQuantity;
    public TextField textFieldQuantity;
    public Button buttonAdd;
    public TableView<Product> tableProduct;
    public Button exitButton;
    public Button filterButton;
    public TextField filterTextFieldMin;
    public TextField filterTextFieldMax;
    public Label labelBuy;
    public Button buttonBuy;

    private ObservableList<Product> dataProduct = FXCollections.observableList(new ArrayList<>());
    private BinaryFileRepo binaryFileRepo;

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void init() {
        listProduct.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {

            @Override
            public void changed(ObservableValue<? extends Product> observableValue, Product oldVal, Product newVal) {
                if (newVal != null) {
                    textFieldId.setText(Integer.toString(newVal.getId()));
                    textFieldMarca.setText(newVal.getMarca());
                    textFieldName.setText(newVal.getName());
                    textFieldPrice.setText(Integer.toString(newVal.getPrice()));
                    textFieldQuantity.setText(newVal.getQuantity());
                }
            }
        });

        TableColumn<Product, String> columnId = new TableColumn<>("Product Id");
        columnId.setCellValueFactory(product -> new SimpleStringProperty(Integer.toString(product.getValue().getId())));

        TableColumn<Product, String> columnMarca = new TableColumn<>("Marca");
        columnMarca.setCellValueFactory(product -> new SimpleStringProperty(product.getValue().getMarca()));

        TableColumn<Product, String> columnName = new TableColumn<>("Name");
        columnName.setCellValueFactory(product -> new SimpleStringProperty(product.getValue().getName()));

        TableColumn<Product, String> columnPrice = new TableColumn<>("Price");
        columnPrice.setCellValueFactory(product -> new SimpleStringProperty(Integer.toString(product.getValue().getPrice())));

        TableColumn<Product, String> columnQuantity = new TableColumn<>("Quantity");
        columnQuantity.setCellValueFactory(product -> new SimpleStringProperty(product.getValue().getQuantity()));


        columnQuantity.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    try {
                        int quantity = Integer.parseInt(item);
                        setText(quantity == 0 ? "n/a" : item);
                    } catch (NumberFormatException e) {
                        setText(item);
                    }
                }
            }
        });

        SortedList<Product> sortedData = new SortedList<>(dataProduct);

        sortedData.setComparator(Comparator.comparingInt(Product::getId));


        tableProduct.setItems(sortedData);

        tableProduct.getColumns().add(columnId);
        tableProduct.getColumns().add(columnMarca);
        tableProduct.getColumns().add(columnName);
        tableProduct.getColumns().add(columnPrice);
        tableProduct.getColumns().add(columnQuantity);
    }

    public void setRepository(BinaryFileRepo piesaRepository) {
        this.binaryFileRepo = piesaRepository;
        listProduct.setItems(dataProduct);
        dataProduct.addAll(piesaRepository.getAll());
    }

    public void onAddButtonClick(ActionEvent actionEvent) {
        try {
            var produsId = Integer.parseInt(textFieldId.getText());
            var produsMarca = textFieldMarca.getText();
            var produsName = textFieldName.getText();
            var produsPrice = Integer.parseInt(textFieldPrice.getText());
            var produsQuantity = textFieldQuantity.getText();

            Product produs = new Product(produsId, produsMarca, produsName, produsPrice, produsQuantity);
            binaryFileRepo.addProdus(produs);
            binaryFileRepo.addaugaBD(produs);

            dataProduct.add(produs);
            tableProduct.setItems(dataProduct.sorted((p1, p2) -> Integer.compare(p1.getId(), p2.getId())));
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            textFieldId.clear();
            textFieldMarca.clear();
            textFieldName.clear();
            textFieldPrice.clear();
            textFieldQuantity.clear();
        }
    }

    public void onExitButtonClick() {
        System.exit(0);
    }

    public void onFilterButtonClick() {
        String filterTextmin = filterTextFieldMin.getText();
        String filterTextmax = filterTextFieldMax.getText();
        if (filterTextmin.isEmpty() && filterTextmax.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Introduceți un text pentru filtrare!", ButtonType.OK).show();
            return;
        }
        if (filterTextmin.isEmpty()) {
            ObservableList<Product> filteredData = FXCollections.observableList(new ArrayList<>());
            int max = Integer.parseInt(filterTextmax);
            for (Product prod : dataProduct) {
                if (prod.getPrice() <= max) {
                    filteredData.add(prod);
                }
            }
            tableProduct.setItems(filteredData);
        } else if (filterTextmax.isEmpty()) {
            ObservableList<Product> filteredData = FXCollections.observableList(new ArrayList<>());
            int min = Integer.parseInt(filterTextmin);
            for (Product prod : dataProduct) {
                if (prod.getPrice() >= min) {
                    filteredData.add(prod);
                }
            }
            tableProduct.setItems(filteredData);
        } else {
            ObservableList<Product> filteredData = FXCollections.observableList(new ArrayList<>());
            int min = Integer.parseInt(filterTextmin);
            int max = Integer.parseInt(filterTextmax);
            for (Product prod : dataProduct) {
                if (prod.getPrice() >= min && prod.getPrice() <= max) {
                    filteredData.add(prod);
                }
            }
            tableProduct.setItems(filteredData);
        }
    }

    public void onResetButtonClick() {
        filterTextFieldMin.clear();
        filterTextFieldMax.clear();
        tableProduct.setItems(dataProduct);
    }

    int totalPrice = 0;

    public void onBuyButtonClick() {
        Product selectedProduct = tableProduct.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            new Alert(Alert.AlertType.ERROR, "Nu ați selectat niciun produs!", ButtonType.OK).show();
            return;
        }


        if (Integer.parseInt(selectedProduct.getQuantity()) == 0) {
            new Alert(Alert.AlertType.ERROR, "Produsul selectat nu mai este disponibil (cantitate 0).", ButtonType.OK).show();
            return;
        }


        selectedProduct.setQuantity(String.valueOf(Integer.parseInt(selectedProduct.getQuantity()) - 1));


        totalPrice += selectedProduct.getPrice();
        labelBuy.setText("Total Price: " + totalPrice + " RON");


        tableProduct.refresh();


        try {
            binaryFileRepo.update(selectedProduct);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Eroare la actualizarea fișierului binar!", ButtonType.OK).show();
        }
    }
}
