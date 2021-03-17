package banking;

import org.sqlite.SQLite;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        c c = new c();
        CoolJDBC connect = new CoolJDBC();
        connect.connect(args[1]);
        Query query = new Query();

        String balance = "0";
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
                query.addCard(connect.conn, c.PIN,c.card);

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
 class c {
     public c() {
         createAccount();
     }
     public String PIN;
     public String BIN;
     public String card;
     public String getPIN() {
         return PIN;
     }

     public void createAccount() {
         Random random = new Random();
         StringBuilder bin = new StringBuilder();
         StringBuilder pin = new StringBuilder();
         bin.append("400000");

         for (int i = 0; i <= 8; i++) {
             bin.append(String.valueOf(random.nextInt(9)));
         }
         for (int i = 0; i <= 3; i++) {
             pin.append(String.valueOf(random.nextInt(9)));
         }

         this.PIN = String.valueOf(pin);
         this.BIN = String.valueOf(bin);
         this.card = this.BIN + Lunh.lunh(this.BIN);
     }
 }
 class Lunh {
     public static String lunh(String BIN) {
         double digit = 0;
         long num = Long.parseLong(BIN);
         int sum = 0;
         int i = 0;
         while (num > 0) {
             i++;
             digit = num % 10;
             num = num / 10;

             if (i % 2 != 0) {
                 digit *= 2;
             }
             if (digit > 9) {
                 digit = digit - 9;
             }
             sum += digit;
         }
         int checkSum = 0;
         while (sum % 10 != 0) {
             sum ++;
             checkSum++;
         }
         return String.valueOf(checkSum);
     }
 }

class CoolJDBC {
    protected Connection conn = null;

    public Connection connect(String db) {
        String url = "jdbc:sqlite:"+ db;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        try {
            this.conn = dataSource.getConnection();
            if (conn.isValid(5)) {
                //System.out.println("Connection is valid.");
                return conn;
            }
        } catch (SQLException e) {

            e.printStackTrace();
            return null;
        }
        return null;
    }
    public boolean tableExists(String tableName,String db){
        connect(db);
        try{
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs = md.getTables(null, null, tableName, null);
            rs.last();
            return rs.getRow() > 0;
        }catch(SQLException ex){
            Logger.getLogger(SQLite.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}

class Query {

    public void addCard(Connection conn, String PIN, String number) {
        // Создадим подготовленное выражение, чтобы избежать SQL-инъекций
        try (PreparedStatement statement = conn.prepareStatement(
                "INSERT INTO card( number,`pin`) " +
                        "VALUES(?, ?)")) {

            statement.setObject(1, number);
            statement.setObject(2, PIN);


            // Выполняем запрос
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}













