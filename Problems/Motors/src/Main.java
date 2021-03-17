import java.util.Scanner;

/* Product - Motor */
abstract class Motor {

    String model;
    long power;

    public Motor(String model, long power) {
        this.model = model;
        this.power = power;
    }

    @Override
    public String toString() {
        return "motor={model:" + model + ",power:" + power + "}";
    }
}

class PneumaticMotor extends Motor {

    public PneumaticMotor(String model, long power) {
        super(model, power);
        System.out.print("Pneumatic ");
    }
}

class HydraulicMotor extends Motor {
    public HydraulicMotor(String model, long power) {
        super(model, power);
        System.out.print("Hydraulic ");
    }
}

class ElectricMotor extends Motor {
    // write your code here ...

    public ElectricMotor(String model, long power) {
        super(model, power);
        System.out.print("Electric ");
    }
}

class WarpDrive extends Motor {
    // write your code here ...

    public WarpDrive(String model, long power) {

        super(model, power);
        System.out.print("Warp drive ");

    }
}

class MotorFactory {

    /**
     * It returns an initialized motor according to the specified type by the first character:
     * 'P' or 'p' - pneumatic, 'H' or 'h' - hydraulic, 'E' or 'e' - electric, 'W' or 'w' - warp.
     */
    public static Motor make(char type, String model, long power) {
        if (type == 'E'|| type == 'e') {

            return new ElectricMotor(model, power);

        } else if (type == 'p'|| type == 'P') {

            return new PneumaticMotor(model, power);

        } else if (type == 'w'|| type == 'W') {

            return new WarpDrive(model, power);
        }
        else if (type == 'h'|| type == 'H') {

            return new HydraulicMotor(model, power);
        }
        else {
            return null;
        }
    }
}

public class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final char type = scanner.next().charAt(0);     
        final String model = scanner.next();
        final long power = scanner.nextLong();

        Motor motor = MotorFactory.make(type, model, power);
        scanner.close();
        System.out.print(motor);
    }
}
