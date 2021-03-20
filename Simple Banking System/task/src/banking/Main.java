package banking;

import org.sqlite.SQLite;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        c c = new c();
        CoolJDBC connect = new CoolJDBC();
        connect.connect(args[1]);
        //Query query = new Query();

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
                Query.addCard(connect.conn, c.PIN,c.card);

            }
            if (choice == 2) {
                System.out.println("Enter your card number:");
                long card = scanner.nextLong();
                System.out.println("Enter your PIN:");
                int PIN = scanner.nextInt();

                    try {
                        if (String.valueOf(PIN).equals(Query.PIN(connect.conn, String.valueOf(card)))) {
                            System.out.println("You have successfully logged in!");
                            while (true) {
                                System.out.println("1. Balance");
                                System.out.println("2. Add income");
                                System.out.println("3. Do transfer");
                                System.out.println("4. Close account");
                                System.out.println("5. Log out");
                                System.out.println("0. Exit");
                                int ch = scanner.nextInt();
                                if (ch == 1) {
                                    System.out.println("Your balance: " + Query.Balance(connect.conn, String.valueOf(card)));
                                } else if (ch == 2) {
                                    System.out.println("Enter income:");
                                    int income = scanner.nextInt();
                                    Query.AddIncome(connect.conn, String.valueOf(card),income);
                                        System.out.println("Income was added!");
                                } else if (ch == 3) {

                                    System.out.println("Transfer");
                                    System.out.println("Enter card number:");
                                    Long Another_Card = scanner.nextLong();
                                    long Another_Card_BIN =  Another_Card / 10;
                                    int Check_Sum = (int) (Another_Card % 10);
                                    if(Lunh.lunh(String.valueOf(Another_Card_BIN)).equals(String.valueOf(Check_Sum))) {

                                        if (Query.AnotherCard(connect.conn, String.valueOf(Another_Card))) {
                                            System.out.println("Enter how much money you want to transfer:");
                                            int amount = scanner.nextInt();

                                            if (Query.Balance(connect.conn, String.valueOf(card)) > amount) {
                                                Query.transfer(connect.conn,String.valueOf(card),
                                                        amount,String.valueOf(Another_Card));
                                                System.out.println("Success!");
                                            } else {
                                                System.out.println("Not enough money!");
                                            }
                                        } else {
                                            System.out.println("Such a card does not exist.");
                                        }
                                    }
                                    else{
                                        System.out.println("Probably you made a mistake in the card number.");
                                        System.out.println("Please try again!");
                                    }

                                } else if (ch == 4) {
                                    Query.closeAccount(connect.conn, String.valueOf(card));
                                    System.out.println("The account has been closed!");
                                    break;
                                } else if (ch == 5) {
                                    System.out.println("You have successfully logged out!");
                                    break;
                                } else if (ch == 0) {
                                    System.out.println("Bye!");
                                    connect.conn.close();
                                    return;
                                }
                            }
                        } else {
                            System.out.println("Wrong card number or PIN!");
                        }
                    }
                    catch (Exception e){
                        System.out.println("Wrong card number or PIN!");
                    }
            }
            if(choice == 0){
                System.out.println("Bye!");
                connect.conn.close();
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
        String url = "jdbc:sqlite:" + db;
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

class  Query {

    public static void addCard(Connection conn, String PIN, String number) {
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


    public static int  Balance(Connection conn,String card) throws SQLException {

        Statement statement = conn.createStatement();

        try (ResultSet balance = statement.executeQuery("SELECT balance FROM card WHERE number = " + card)) {

            // Retrieve column values
            return balance.getInt("balance");

        }

    }
    public static String PIN(Connection conn, String card) throws SQLException {

        Statement statement = conn.createStatement();

        try (ResultSet PIN = statement.executeQuery("SELECT pin FROM card WHERE number = " + card)) {

            // Retrieve column values
            return PIN.getString("pin");
        }

    }
    public static boolean  AnotherCard(Connection conn,String AnotherCard) throws SQLException {

        Statement statement = conn.createStatement();

        try (ResultSet Card = statement.executeQuery("SELECT number FROM card WHERE number = " + AnotherCard)) {

            // Retrieve column values
             String number = Card.getString("number");
            return !number.equals("");

        }catch (Exception e){
            return false;
        }
    }

    public static void AddIncome (Connection conn, String Card, int income) throws SQLException {



         String SQL = "UPDATE card SET balance = "
                +"balance + ?"
                +"WHERE number = ?";

        PreparedStatement statement = conn.prepareStatement(SQL);
        statement.setInt(1, income);
        statement.setString(2, Card);
        statement.executeUpdate();
    }

    public static void transfer (Connection conn, String Card, int income, String AnotherCard) throws SQLException {
        conn.setAutoCommit(false);
        String SQL = "UPDATE card SET balance = "
                +"balance + ?"
                +"WHERE number = ?";
        String SQL2 = "UPDATE card SET balance = "
                +"balance - ?"
                +"WHERE number = ?";
        try(PreparedStatement statement = conn.prepareStatement(SQL);
            PreparedStatement statement2 = conn.prepareStatement(SQL2)) {

            statement.setInt(1, income);
            statement.setString(2, AnotherCard);

            statement2.setInt(1, income);
            statement2.setString(2, Card);

            statement.executeUpdate();
            statement2.executeUpdate();

            conn.commit();

        }catch (Exception e){

            System.out.println("Что то не то");

        }



    }
    public static void closeAccount(Connection conn,String card) throws SQLException {
        String SQL = "DELETE FROM card WHERE number = ?";

        PreparedStatement pstmt = conn.prepareStatement(SQL);

            // set the corresponding param
            pstmt.setString(1, card);
            // execute the delete statement
            pstmt.executeUpdate();

        }
    }














