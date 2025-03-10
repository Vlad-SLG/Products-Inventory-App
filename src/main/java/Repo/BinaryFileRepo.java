package Repo;

import Domain.Product;

import java.io.*;
import java.util.List;


public class BinaryFileRepo extends RepoMemory implements AutoCloseable {

    private final String FILENAME = "produse.bin";

    public BinaryFileRepo() {

        loadData();
    }


    private void loadData() {
        File file = new File(FILENAME);


        if (!file.exists() || file.length() == 0) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {

            List<Product> productsFromFile = (List<Product>) ois.readObject();
            if (productsFromFile != null) {
                this.repo.clear();
                this.repo.addAll(productsFromFile);
            }
        } catch (EOFException e) {

            System.out.println("Fișierul este gol sau corupt. Nu se încarcă date.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RepoEx("Eroare la încărcarea datelor din fișierul binar: " + e.getMessage());
        }
    }


    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            oos.writeObject(this.repo);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RepoEx("Eroare la salvarea datelor în fișierul binar: " + e.getMessage());
        }
    }


    public void addaugaBD(Product p) throws RepoEx {

        saveData();
    }

    @Override
    public void update(Product product) {
        super.update(product);
        saveData();
    }

    @Override
    public void close() {

        saveData();
    }
}
