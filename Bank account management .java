import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.InputMismatchException; // Importing InputMismatchException

// Class representing a BankAccount
class CustomerAccount {
    private static final AtomicInteger idGenerator = new AtomicInteger(2000);
    private final int id;
    private final String holderName;
    private double balanceAmount;

    public CustomerAccount(String holderName) {
        this.holderName = holderName;
        this.id = idGenerator.getAndIncrement();
        this.balanceAmount = 0.0;
    }

    public int getId() {
        return id;
    }

    public String getHolderName() {
        return holderName;
    }

    public double getBalanceAmount() {
        return balanceAmount;
    }

    public void addFunds(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }
        balanceAmount += amount;
        System.out.printf("Successfully added: %.2f, New Balance: %.2f%n", amount, balanceAmount);
    }

    public void withdrawFunds(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (amount > balanceAmount) {
            throw new IllegalArgumentException("Insufficient balance. Current balance: " + balanceAmount);
        }
        balanceAmount -= amount;
        System.out.printf("Successfully withdrew: %.2f, New Balance: %.2f%n", amount, balanceAmount);
    }
}

// Class representing the Bank system
class BankingService {
    private final Map<Integer, CustomerAccount> accountRegistry = new HashMap<>();

    public CustomerAccount openNewAccount(String name) {
        CustomerAccount newAccount = new CustomerAccount(name);
        accountRegistry.put(newAccount.getId(), newAccount);
        System.out.println("New account opened for " + name + " with Account ID: " + newAccount.getId());
        return newAccount;
    }

    public CustomerAccount retrieveAccount(int id) {
        return accountRegistry.get(id);
    }

    public void listAllAccounts() {
        System.out.println("Listing all Customer Accounts:");
        for (CustomerAccount acc : accountRegistry.values()) {
            System.out.println("Account ID: " + acc.getId() +
                               ", Holder: " + acc.getHolderName() +
                               ", Balance: " + acc.getBalanceAmount());
        }
    }
}

// Main class to drive the application
public class BankingApplication {
    public static void main(String[] args) {
        BankingService bankingService = new BankingService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Open Account\n2. Deposit Funds\n3. Withdraw Funds\n4. Check Balance\n5. Show All Accounts\n6. Exit");
            System.out.print("Select an option: ");

            int option;
            try {
                option = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Clear the invalid input
                continue;
            }
            
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    System.out.print("Enter the account holder's name: ");
                    String name = scanner.nextLine();
                    bankingService.openNewAccount(name);
                    break;
                case 2:
                    System.out.print("Enter Account ID for deposit: ");
                    int depositId = scanner.nextInt();
                    System.out.print("Enter deposit amount: ");
                    double depositAmount = scanner.nextDouble();
                    try {
                        CustomerAccount depositAccount = bankingService.retrieveAccount(depositId);
                        if (depositAccount != null) {
                            depositAccount.addFunds(depositAmount);
                        } else {
                            System.out.println("Account not found.");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    System.out.print("Enter Account ID for withdrawal: ");
                    int withdrawalId = scanner.nextInt();
                    System.out.print("Enter withdrawal amount: ");
                    double withdrawalAmount = scanner.nextDouble();
                    try {
                        CustomerAccount withdrawalAccount = bankingService.retrieveAccount(withdrawalId);
                        if (withdrawalAccount != null) {
                            withdrawalAccount.withdrawFunds(withdrawalAmount);
                        } else {
                            System.out.println("Account not found.");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    System.out.print("Enter Account ID to check balance: ");
                    int balanceId = scanner.nextInt();
                    CustomerAccount balanceAccount = bankingService.retrieveAccount(balanceId);
                    if (balanceAccount != null) {
                        System.out.println("Current Balance: " + balanceAccount.getBalanceAmount());
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;
                case 5:
                    bankingService.listAllAccounts();
                    break;
                case 6:
                    System.out.println("Exiting application...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
