import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Medicine {
    String name;
    int quantity;
    String expiryDate;
    double price;

    Medicine(String name, int quantity, String expiryDate, double price) {
        this.name = name;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.price = price;
    }

    void updateQuantity(int quantity) {
        this.quantity = quantity;
    }

    void display() {
        System.out.println(
                "Name: " + name + ", Quantity: " + quantity + ", Expiry Date: " + expiryDate + ", Price: $" + price);
    }

    String toCSV() {
        return name + "," + quantity + "," + expiryDate + "," + price;
    }

    static Medicine fromCSV(String csv) {
        String[] parts = csv.split(",");
        return new Medicine(parts[0], Integer.parseInt(parts[1]), parts[2], Double.parseDouble(parts[3]));
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

    String toCSV() {
        return name + "," + contact;
    }

    static Customer fromCSV(String csv) {
        String[] parts = csv.split(",");
        return new Customer(parts[0], parts[1]);
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
            medicines.add(new Medicine(medicine.name, quantity, medicine.expiryDate, medicine.price));
            totalCost += quantity * medicine.price;
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

    String toCSV() {
        StringBuilder sb = new StringBuilder();
        sb.append(customer.toCSV()).append("\n");
        for (Medicine med : medicines) {
            sb.append(med.toCSV()).append("\n");
        }
        sb.append(totalCost);
        return sb.toString();
    }

    static Order fromCSV(String csv) {
        String[] parts = csv.split("\n");
        Customer customer = Customer.fromCSV(parts[0]);
        Order order = new Order(customer);
        for (int i = 1; i < parts.length - 1; i++) {
            order.medicines.add(Medicine.fromCSV(parts[i]));
        }
        order.totalCost = Double.parseDouble(parts[parts.length - 1]);
        return order;
    }
}

class Pharmacy {
    List<Medicine> inventory = new ArrayList<>();
    List<Customer> customers = new ArrayList<>();
    List<Order> orders = new ArrayList<>();
    private final String medicineFile = "medicines.csv";
    private final String customerFile = "customers.csv";
    private final String orderFile = "orders.csv";

    void addMedicine(String name, int quantity, String expiryDate, double price) {
        inventory.add(new Medicine(name, quantity, expiryDate, price));
        saveMedicines();
    }

    void addCustomer(String name, String contact) {
        customers.add(new Customer(name, contact));
        saveCustomers();
    }

    void updateMedicine(String name, int newQuantity) {
        for (Medicine med : inventory) {
            if (med.name.equalsIgnoreCase(name)) {
                med.updateQuantity(newQuantity);
                saveMedicines();
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

    void checkExpiringMedicines() {
        System.out.println("Expiring Medicines:");
        for (Medicine med : inventory) {
            if (med.expiryDate.compareTo("2024-12-31") <= 0) {
                System.out.println(med.name + " is expiring soon on " + med.expiryDate);
            }
        }
    }

    void createOrder(String customerName, String medicineName, int quantity) {
        Customer customer = findCustomerByName(customerName);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        Medicine medicine = findMedicineByName(medicineName);
        if (medicine == null) {
            System.out.println("Medicine not found.");
            return;
        }

        Order order = new Order(customer);
        order.addMedicine(medicine, quantity);
        orders.add(order);
        saveOrders();
        System.out.println("Order created for " + customerName);
    }

    void applyDiscountToOrder(String customerName, double discountPercentage) {
        for (Order order : orders) {
            if (order.customer.name.equalsIgnoreCase(customerName)) {
                double discountAmount = order.totalCost * discountPercentage / 100;
                order.totalCost -= discountAmount;
                saveOrders();
                System.out.println("Discount of " + discountPercentage + "% applied to " + customerName
                        + "'s order. New Total Cost: $" + order.totalCost);
                return;
            }
        }
        System.out.println("No order found for " + customerName);
    }

    void listOrders() {
        System.out.println("Order History:");
        for (Order order : orders) {
            order.display();
        }
    }

    Customer findCustomerByName(String name) {
        for (Customer customer : customers) {
            if (customer.name.equalsIgnoreCase(name)) {
                return customer;
            }
        }
        return null;
    }

    Medicine findMedicineByName(String name) {
        for (Medicine med : inventory) {
            if (med.name.equalsIgnoreCase(name)) {
                return med;
            }
        }
        return null;
    }

    void saveMedicines() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(medicineFile))) {
            for (Medicine med : inventory) {
                writer.println(med.toCSV());
            }
        } catch (IOException e) {
            System.out.println("Error saving medicines: " + e.getMessage());
        }
    }

    void loadMedicines() {
        try (BufferedReader reader = new BufferedReader(new FileReader(medicineFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                inventory.add(Medicine.fromCSV(line));
            }
        } catch (IOException e) {
            System.out.println("Error loading medicines: " + e.getMessage());
        }
    }

    void saveCustomers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(customerFile))) {
            for (Customer customer : customers) {
                writer.println(customer.toCSV());
            }
        } catch (IOException e) {
            System.out.println("Error saving customers: " + e.getMessage());
        }
    }

    void loadCustomers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(customerFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                customers.add(Customer.fromCSV(line));
            }
        } catch (IOException e) {
            System.out.println("Error loading customers: " + e.getMessage());
        }
    }

    void saveOrders() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(orderFile))) {
            for (Order order : orders) {
                writer.println(order.toCSV());
            }
        } catch (IOException e) {
            System.out.println("Error saving orders: " + e.getMessage());
        }
    }

    void loadOrders() {
        try (BufferedReader reader = new BufferedReader(new FileReader(orderFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                orders.add(Order.fromCSV(line));
            }
        } catch (IOException e) {
            System.out.println("Error loading orders: " + e.getMessage());
        }
    }
}

public class PharmacyManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Pharmacy pharmacy = new Pharmacy();

        pharmacy.loadMedicines();
        pharmacy.loadCustomers();
        pharmacy.loadOrders();

        while (true) {
            System.out.println("\nPharmacy Management System");
            System.out.println("1. Add Medicine");
            System.out.println("2. Add Customer");
            System.out.println("3. Create Order");
            System.out.println("4. List Medicines");
            System.out.println("5. List Customers");
            System.out.println("6. List Orders");
            System.out.println("7. Check Low Stock");
            System.out.println("8. Check Expiring Medicines");
            System.out.println("9. Apply Discount");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter medicine name: ");
                    String medName = scanner.nextLine();
                    System.out.print("Enter quantity: ");
                    int quantity = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter expiry date (YYYY-MM-DD): ");
                    String expiryDate = scanner.nextLine();
                    System.out.print("Enter price: ");
                    double price = scanner.nextDouble();
                    pharmacy.addMedicine(medName, quantity, expiryDate, price);
                    break;

                case 2:
                    System.out.print("Enter customer name: ");
                    String custName = scanner.nextLine();
                    System.out.print("Enter contact: ");
                    String contact = scanner.nextLine();
                    pharmacy.addCustomer(custName, contact);
                    break;

                case 3:
                    System.out.print("Enter customer name: ");
                    String customerName = scanner.nextLine();
                    System.out.print("Enter medicine name: ");
                    String medicineName = scanner.nextLine();
                    System.out.print("Enter quantity: ");
                    int orderQuantity = scanner.nextInt();
                    pharmacy.createOrder(customerName, medicineName, orderQuantity);
                    break;

                case 4:
                    pharmacy.listMedicines();
                    break;

                case 5:
                    pharmacy.listCustomers();
                    break;

                case 6:
                    pharmacy.listOrders();
                    break;

                case 7:
                    pharmacy.checkLowStock();
                    break;

                case 8:
                    pharmacy.checkExpiringMedicines();
                    break;

                case 9:
                    System.out.print("Enter customer name: ");
                    String discountCustomer = scanner.nextLine();
                    System.out.print("Enter discount percentage: ");
                    double discountPercentage = scanner.nextDouble();
                    pharmacy.applyDiscountToOrder(discountCustomer, discountPercentage);
                    break;

                case 0:
                    System.out.println("Exiting system...");
                    pharmacy.saveMedicines();
                    pharmacy.saveCustomers();
                    pharmacy.saveOrders();
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
