package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.Arrays;


@Autonomous()
public class MoveAutoTest extends LinearOpMode {

    public double p = 0.8;
    public double i = 5;

    private HardwarePushbot drive = new HardwarePushbot();

    @Override
    public void runOpMode() throws InterruptedException {

        drive.init(hardwareMap);
        drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        waitForStart();

        if (isStopRequested()) return;

        moveForward(60, 0.5);

        telemetry.addData("", "done");
    }

    private void moveForward(double inches, double speed) {
        int ticksToTravel = (int) (inches * 45.284910729659373);

        double startAngle = drive.getAngle();

        if (speed == 0) {
            return;
        }

        drive.frontRightDrive.setTargetPosition(drive.frontRightDrive.getCurrentPosition() + ticksToTravel);
        drive.frontLeftDrive.setTargetPosition(drive.frontLeftDrive.getCurrentPosition() + ticksToTravel);
        drive.backRightDrive.setTargetPosition(drive.backRightDrive.getCurrentPosition() + ticksToTravel);
        drive.backLeftDrive.setTargetPosition(drive.backLeftDrive.getCurrentPosition() + ticksToTravel);

        drive.frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        drive.frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        drive.backRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        drive.backLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        drive.frontRightDrive.setPower(speed);
        drive.frontLeftDrive.setPower(speed);
        drive.backRightDrive.setPower(speed);
        drive.backLeftDrive.setPower(speed);

        while (drive.isMoving(537.7)) {
            if (drive.deadzone(drive.getAngle() - startAngle, i) != 0) {
                if ((drive.getAngle() - startAngle > 0 && speed > 0) || (drive.getAngle() - startAngle < 0 && speed < 0)) {
                    drive.frontRightDrive.setPower(speed * p);
                    drive.backRightDrive.setPower(speed * p);
                } else {
                    drive.frontLeftDrive.setPower(speed * p);
                    drive.backLeftDrive.setPower(speed * p);
                }
            } else {
                drive.frontRightDrive.setPower(speed);
                drive.frontLeftDrive.setPower(speed);
                drive.backRightDrive.setPower(speed);
                drive.backLeftDrive.setPower(speed);
            }

            telemetry.addData("Error:",drive.getAngle() - startAngle);
            telemetry.update();

            sleep(10);
        }

        drive.frontRightDrive.setPower(0);
        drive.frontLeftDrive.setPower(0);
        drive.backLeftDrive.setPower(0);
        drive.backRightDrive.setPower(0);
    }



    private void turnDegrees(double degrees) {
        double ticksToTravel = (degrees / 360 * (Math.PI * 17.5)) / 11.8737122661 * 537.7;
        if (degrees > 0) {

        } else if (degrees < 0) {

        }
    }
}
