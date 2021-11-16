package org.firstinspires.ftc.teamcode.libs;

import org.firstinspires.ftc.robotcore.internal.system.NativeObject;
import org.firstinspires.ftc.teamcode.HardwarePushbot;
import static java.lang.Thread.sleep;

public class Move {
    private static int moveTimeout = 5000;
    private static HardwarePushbot robot;

    public Move (HardwarePushbot _robot) {
        robot = _robot;
    }

    public static void stopMotors() {
        robot.frontLeftDrive.setPower(0);
        robot.frontRightDrive.setPower(0);
        robot.backLeftDrive.setPower(0);
        robot.backRightDrive.setPower(0);
    }

    public static void moveMotors(double x, double y, double rx, RobotPosition position) {
        double frontLeftPower = y + x + rx;
        double backLeftPower = y - x + rx;
        double frontRightPower = y - x - rx;
        double backRightPower = y + x - rx;

        // double positions = (robot.frontLeftDrive.getCurrentPosition() + robot.frontRightDrive.getCurrentPosition() + robot.backLeftDrive.getCurrentPosition() + robot.backRightDrive.getCurrentPosition()) / 4

        // Put powers in the range of -1 to 1 only if they aren't already
        // Not checking would cause us to always drive at full speed
        if (Math.abs(frontLeftPower) > 1 || Math.abs(backLeftPower) > 1 ||
                Math.abs(frontRightPower) > 1 || Math.abs(backRightPower) > 1) {
            // Find the largest power
            double max = 0;
            max = Math.max(Math.abs(frontLeftPower), Math.abs(backLeftPower));
            max = Math.max(Math.abs(frontRightPower), max);
            max = Math.max(Math.abs(backRightPower), max);

            // Divide everything by max (it's positive so we don't need to worry
            // about signs)
            frontLeftPower /= max;
            backLeftPower /= max;
            frontRightPower /= max;
            backRightPower /= max;
        }

        // Set Targets
        robot.frontLeftDrive.setTargetPosition(position.frontLeftTarget);
        robot.frontRightDrive.setTargetPosition(position.frontRightTarget);
        robot.backLeftDrive.setTargetPosition(position.backLeftTarget);
        robot.backRightDrive.setTargetPosition(position.backRightTarget);

        // Set Powers
        robot.frontLeftDrive.setPower(frontLeftPower);
        robot.backLeftDrive.setPower(backLeftPower);
        robot.frontRightDrive.setPower(frontRightPower);
        robot.backRightDrive.setPower(backRightPower);

        try {
            sleep(moveTimeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopMotors();
    }
}