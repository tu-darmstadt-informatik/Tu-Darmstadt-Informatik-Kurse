package ex11;

import java.util.Date;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        float interestRate = 0.9f;

        // Creating a multi account
        MultiAccount myMultiAccount = new MultiAccount(new BasicBankAccount(100),
                new BasicBankAccount(1000));

        // Creating the customer and support interfaces
        CustomerInterface myInterface = new CustomerInterface(myMultiAccount);
        SupportInterface supportInterface = new SupportInterface(myMultiAccount);

        // The interfaces must be added to the listeners of the multi account to
        // receive updates
        myMultiAccount.addListener(myInterface);
        myMultiAccount.addListener(supportInterface);

        // This should trigger applyInterestRate and notifyListeners on both BasicBankAccount
        myMultiAccount.applyInterestRate(interestRate);

        // An example of how the interfaces can use the update data
        Map<String, Date> customerUpdateHistory = myInterface.getUpdateHistory();
        Map<String, Date> supportUpdateHistory = supportInterface.getUpdateHistory();

        System.out.println("Interest rate of " + interestRate + " applied.");
        System.out.println("Customer interface update history: ");
        System.out.println(customerUpdateHistory.toString());
        System.out.println("Support interface update history: ");
        System.out.println(supportUpdateHistory.toString());
        System.out.println("These dates should be nearly identical!");
    }
}
