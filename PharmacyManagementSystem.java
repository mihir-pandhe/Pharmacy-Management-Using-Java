import java.util.ArrayList;
import java.util.List;

class Medicine {
    String name;
    int quantity;
    String expiryDate;

    Medicine(String name, int quantity, String expiryDate) {
        this.name = name;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
    }

    void updateQuantity(int quantity) {
        this.quantity = quantity;
    }

    void display() {
        System.out.println("Name: " + name + ", Quantity: " + quantity + ", Expiry Date: " + expiryDate);
    }
}

class Pharmacy {
    List<Medicine> inventory = new ArrayList<>();

    void addMedicine(String name, int quantity, String expiryDate) {
        inventory.add(new Medicine(name, quantity, expiryDate));
    }

    void updateMedicine(String name, int newQuantity) {
        for (Medicine med : inventory) {
            if (med.name.equalsIgnoreCase(name)) {
                med.updateQuantity(newQuantity);
                System.out.println("Updated " + name + " to " + newQuantity + " units.");
                return;
            }
        }
        System.out.println(name + " not found in inventory.");
    }

    void listMedicines() {
        System.out.println("Medicine Inventory:");
        for (Medicine med : inventory) {
            med.display();
        }
    }

    void checkLowStock() {
        System.out.println("Low Stock Medicines:");
        for (Medicine med : inventory) {
            if (med.quantity < 10) {
                System.out.println(med.name + " is running low with " + med.quantity + " units.");
            }
        }
    }

    void processOrder(String name, int quantity) {
        for (Medicine med : inventory) {
            if (med.name.equalsIgnoreCase(name)) {
                if (med.quantity >= quantity) {
                    med.updateQuantity(med.quantity - quantity);
                    System.out.println("Order processed for " + quantity + " units of " + name);
                } else {
                    System.out.println("Insufficient stock for " + name);
                }
                return;
            }
        }
        System.out.println(name + " not found in inventory.");
    }
}

public class PharmacyManagementSystem {
    public static void main(String[] args) {
        Pharmacy pharmacy = new Pharmacy();
        pharmacy.addMedicine("Ibuprofen", 8, "2024-12-15");
        pharmacy.addMedicine("Paracetamol", 50, "2025-06-01");
        pharmacy.listMedicines();
        pharmacy.checkLowStock();
        pharmacy.processOrder("Paracetamol", 5);
        pharmacy.updateMedicine("Ibuprofen", 20);
        pharmacy.listMedicines();
    }
}
