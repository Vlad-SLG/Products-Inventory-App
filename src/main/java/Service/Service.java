package Service;

import Domain.Product;
import Repo.BinaryFileRepo;

public class Service {
    private BinaryFileRepo binaryFileRepo;

    public Service(BinaryFileRepo binaryFileRepo) {
        this.binaryFileRepo = binaryFileRepo;
    }

    public void add(Product p) {
        binaryFileRepo.addProdus(p);   // adăugare în memorie
        binaryFileRepo.addaugaBD(p);   // salvare în fișier
    }
}
