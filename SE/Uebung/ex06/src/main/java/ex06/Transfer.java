package ex06;

public interface Transfer {
    Account getSourceAccount();

    Account getTargetAccount();

    int getAmount();
}
