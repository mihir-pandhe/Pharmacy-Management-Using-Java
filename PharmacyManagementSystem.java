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

class Customer {
    String name;
    String contact;

    Customer(String name, String contact) {
        this.name = name;
        this.contact = contact;
    }

    void display() {
        System.out.println("Customer: " + name + ", Contact: " + contact);
    }
}

class Order {
    Customer customer;
    List<Medicine> medicines = new ArrayList<>();
    double totalCost;

    Order(Customer customer) {
        this.customer = customer;
    }

    void addMedicine(Medicine medicine, int quantity) {
        if (medicine.quantity >= quantity) {
            medicines.add(new Medicine(medicine.name, quantity, medicine.expiryDate));
            totalCost += quantity * 10;
            medicine.updateQuantity(medicine.quantity - quantity);
        } else {
            System.out.println("Insufficient stock for " + medicine.name);
        }
    }

    void display() {
        customer.display();
        System.out.println("Order details:");
        for (Medicine med : medicines) {
            med.display();
        }
        System.out.println("Total Cost: $" + totalCost);
    }
}

class Pharmacy {
    List<Medicine> inventory = new ArrayList<>();
    List<Customer> customers = new ArrayList<>();
    List<Order> orders = new ArrayList<>();

    void addMedicine(String name, int quantity, String expiryDate) {
        inventory.add(new Medicine(name, quantity, expiryDate));
    }

    void addCustomer(String name, String contact) {
        customers.add(new Customer(name, contact));
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

    void listCustomers() {
        System.out.println("Customer List:");
        for (Customer customer : customers) {
            customer.display();
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

    void createOrder(String customerName, String medicineName, int quantity) {
        Customer customer = null;
        for (Customer c : customers) {
            if (c.name.equalsIgnoreCase(customerName)) {
                customer = c;
                break;
            }
        }
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        Medicine medicine = null;
        for (Medicine m : inventory) {
            if (m.name.equalsIgnoreCase(medicineName)) {
                medicine = m;
                break;
            }
        }
        if (medicine == null) {
            System.out.println("Medicine not found.");
            return;
        }

        Order order = new Order(customer);
        order.addMedicine(medicine, quantity);
        orders.add(order);
        System.out.println("Order created for " + customerName);
    }

    void listOrders() {
        System.out.println("Order History:");
        for (Order order : orders) {
            order.display();
        }
    }
}

public class PharmacyManagementSystem {
    public static void main(String[] args) {
        Pharmacy pharmacy = new Pharmacy();

        pharmacy.addMedicine("Ibuprofen", 8, "2024-12-15");
        pharmacy.addMedicine("Paracetamol", 50, "2025-06-01");

        pharmacy.addCustomer("Jane Smith", "987-654-3210");
        pharmacy.addCustomer("John Doe", "123-456-7890");

        pharmacy.listMedicines();
        pharmacy.listCustomers();
        pharmacy.checkLowStock();
        pharmacy.createOrder("John Doe", "Paracetamol", 5);

        pharmacy.listOrders();
    }
}
