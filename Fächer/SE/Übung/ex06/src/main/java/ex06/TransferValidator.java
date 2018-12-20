package ex06;

public class TransferValidator {
    public static void performTransfer(Transfer transfer) {
        Tracer.trace();

        // SOLUTION START
        // TODO: Implement your solution here
        Account sourceAccount = Transfer.getSourceAccount();
        Account targetAccount = Transfer.getTargetAccount();
        int amount = Transfer.getAmount();

        boolean IsBacked = sourceAccount.checkAmount(amount);
        if(IsBacked==true)
        {
          TransferExecutor.performTransfer(sourceAccount,targetAccount,amount);
          requestTransfer.displaySuccess();
        }
        else
          requestTransfer.displayError();


        // SOLUTION END

        Tracer.trace();
    }
}
