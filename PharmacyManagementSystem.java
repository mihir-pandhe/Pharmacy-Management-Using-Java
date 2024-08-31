import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
            totalCost += quantity * 10; // Assuming a flat rate of $10 per unit
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
        System.out.println("Order created for " + customerName);
    }

    void applyDiscountToOrder(String customerName, double discountPercentage) {
        for (Order order : orders) {
            if (order.customer.name.equalsIgnoreCase(customerName)) {
                double discountAmount = order.totalCost * discountPercentage / 100;
                order.totalCost -= discountAmount;
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
}

public class PharmacyManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Pharmacy pharmacy = new Pharmacy();

        pharmacy.addMedicine("Paracetamol", 50, "2025-06-01");
        pharmacy.addMedicine("Ibuprofen", 8, "2024-12-15");

        pharmacy.addCustomer("John Doe", "123-456-7890");
        pharmacy.addCustomer("Jane Smith", "987-654-3210");

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
                    pharmacy.addMedicine(medName, quantity, expiryDate);
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
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
