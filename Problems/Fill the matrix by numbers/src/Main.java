import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        String[][] matrix = new String[n][n];
        for(int i = 0; i < n; i++ ){
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = String.valueOf(Math.abs(i - j));
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}