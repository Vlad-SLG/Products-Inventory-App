package com.example.test___;

import Domain.Product;
import Repo.BinaryFileRepo;
import Service.Service;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    public HelloApplication() throws IOException {
    }

    @Override
    public void start(Stage stage) throws IOException {
        BinaryFileRepo binaryFileRepo = new BinaryFileRepo();

        Product product1 = new Product(100, "Lenovo", "ThinkPadS100", 9500, "14");
        Product product2 = new Product(101, "Asus", "Strix 45", 7700, "4");
        Product product3 = new Product(102, "Ariston", "WSL-1003", 2240, "2");
        Product product4 = new Product(103, "Bosch", "Series 4", 1900, "11");
        Product product5 = new Product(104, "Whirlpool", "SuperFridge 100LE", 3200, "10");
        Product product6 = new Product(99, "Whirlpool1", "SuperFridg11e 100LE", 300, "8");

        Service serv = new Service(binaryFileRepo);
        serv.add(product1);
        serv.add(product2);
        serv.add(product3);
        serv.add(product4);
        serv.add(product5);
        serv.add(product6);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 869, 650);

        HelloController controller = fxmlLoader.getController();
        controller.init();
        controller.setRepository(binaryFileRepo);

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
