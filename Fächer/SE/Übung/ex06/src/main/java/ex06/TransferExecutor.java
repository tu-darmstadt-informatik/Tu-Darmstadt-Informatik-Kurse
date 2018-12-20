package ex06;

public class TransferExecutor {
    public static void performTransfer(Account sourceAccount, Account targetAccount, int amount) {
        Tracer.trace();

        // SOLUTION START
        // TODO: Implement your solution here
        sourceAccount.charge(amount);
        targetAccount.credit(amount);

        // SOLUTION END

        Tracer.trace();
    }
}
