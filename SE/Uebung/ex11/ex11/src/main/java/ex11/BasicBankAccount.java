package ex11;

class BasicBankAccount extends BankAccount {
    private int amount;
    private static int idctr = 0;

    private static String getNextID() {
        return String.valueOf(idctr++);
    }

    BasicBankAccount(int initialAmount) {
        this.amount = initialAmount;
        this.ID = getNextID();
    }
    
    @Override
    public int getAmount() {
    	return amount;
    }

    @Override
    public void applyInterestRate(float interestRate) {
        float newAmount = ((float) amount) * interestRate;
        amount = (int) newAmount;
        notifyListeners();
    }
}
