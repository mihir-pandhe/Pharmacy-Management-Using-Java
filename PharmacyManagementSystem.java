import java.util.ArrayList;
import java.util.List;

class Medicine {
    String name;
    int quantity;

    Medicine(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    void display() {
        System.out.println("Name: " + name + ", Quantity: " + quantity);
    }
}

class Pharmacy {
    List<Medicine> inventory = new ArrayList<>();

    void addMedicine(String name, int quantity) {
        inventory.add(new Medicine(name, quantity));
    }

    void listMedicines() {
        System.out.println("Medicine Inventory:");
        for (Medicine med : inventory) {
            med.display();
        }
    }
}

public class PharmacyManagementSystem {
    public static void main(String[] args) {
        Pharmacy pharmacy = new Pharmacy();
        pharmacy.addMedicine("Paracetamol", 50);
        pharmacy.addMedicine("Ibuprofen", 30);
        pharmacy.listMedicines();
    }
}
