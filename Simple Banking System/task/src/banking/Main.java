package banking;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        c c = new c();
        while(true) {
            System.out.println("1. Create an account");
            System.out.println("2. Log into account");
            System.out.println("0. Exit");
            int choice = scanner.nextInt();
            if (choice == 1) {
                c = new c();
                System.out.println("Your card has been created");
                System.out.println("Your card number:");
                System.out.println(c.card);
                System.out.println("Your card PIN:");
                System.out.println(c.PIN);
            }
            if (choice == 2) {
                System.out.println("Enter your card number:");
                long card = scanner.nextLong();
                System.out.println("Enter your PIN:");
                int PIN = scanner.nextInt();

                if(String.valueOf(card).equals(c.card) && String.valueOf(PIN).equals(c.PIN)){
                    System.out.println("You have successfully logged in!");

                    while(true){
                        System.out.println("1. Balance");
                        System.out.println("2. Log out");
                        System.out.println("0. Exit");
                        int ch = scanner.nextInt();
                        if(ch == 1){
                            System.out.println("Balance: 0");
                        } else if(ch == 2){
                            System.out.println("You have successfully logged out!");
                            break;
                        }
                        else if(ch == 0){
                            return;
                        }
                    }
                }else {
                    System.out.println("Wrong card number or PIN!");
                }

            }
            if(choice == 0){
                System.out.println("Bye!");
                return;
            }
        }

    }
}
 class c{

    public c() {
        createAccount();
    }
    public String PIN;
    public String card;
    public String getCard() {
        return card;
    }
    public String getPIN() {
        return PIN;
    }
    public void createAccount(){
        Random random = new Random();
        StringBuilder card = new StringBuilder();
        StringBuilder pin = new StringBuilder();
        card.append("400000");

        for(int i = 0; i <= 9; i++){
            card.append(String.valueOf(random.nextInt(9)));
        }
        for(int i = 0; i <= 3; i++){
            pin.append(String.valueOf(random.nextInt(9)));
        }
        this.PIN = String.valueOf(pin);
        this.card = String.valueOf(card);
    }
}



