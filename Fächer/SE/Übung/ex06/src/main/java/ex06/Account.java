package ex06;

public interface Account {
    boolean checkAmount(int amount);

    void charge(int amount);

    void credit(int amount);
}
