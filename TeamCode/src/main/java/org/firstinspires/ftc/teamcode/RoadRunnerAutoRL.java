package org.firstinspires.ftc.teamcode;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryAccelerationConstraint;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import java.util.Arrays;
import java.util.List;

/*
 * Op mode for preliminary tuning of the follower PID coefficients (located in the drive base
 * classes). The robot drives in a DISTANCE-by-DISTANCE square indefinitely. Utilization of the
 * dashboard is recommended for this tuning routine. To access the dashboard, connect your computer
 * to the RC's WiFi network. In your browser, navigate to https://192.168.49.1:8080/dash if you're
 * using the RC phone or https://192.168.43.1:8080/dash if you are using the Control Hub. Once
 * you've successfully connected, start the program, and your robot will begin driving in a square.
 * You should observe the target position (green) and your pose estimate (blue) and adjust your
 * follower PID coefficients such that you follow the target position as accurately as possible.
 * If you are using SampleTankDrive, you should be tuning TRANSLATIONAL_PID and HEADING_PID.
 * If you are using SampleTankDrive, you should be tuning AXIAL_PID, CROSS_TRACK_PID, and HEADING_PID.
 * These coefficients can be tuned live in dashboard.
 */

@Config
@Autonomous(group = "drive")
public class RoadRunnerAutoRL extends LinearOpMode {
    /*
    TensorFlow Stuff.
     */
    private static final String TFOD_MODEL_ASSET = "model_ycup.tflite";

    private static final String[] LABELS = {
            "ycup",
            "Ball",
            "Cube",
            "Duck",
            "Marker"
    };

    private static final char[] POSITION_KEY = {
            'L',
            'C',
            'R',
            'N'
    };

    private static final String VUFORIA_KEY =
            "Ac5+EOX/////AAABmYZoDckxSUV7iTj4MtRwiJpvftO8hpoEtcxmCLDolUOn81SCtt0u8igYx5S9Gz9UpcHI66Vto0Wy1IhFoZ4J36MXjIwxdHCb/" +
                    "81+4+4DhbQ2tWq9Xisz4NvAu2l1IN8uj6qvmjmg02YfS7+REk+/NxVrS15d2fvFh7lE9RMlLtMwgkK9903e2mgxf48yL9IQMXoTfBJhY3" +
                    "X4cSKSzz4XGKDjqvIXnAd47NYB8TXuOwY0N8bL9+jPNPaw3E2SgOU2imUU6kCAQvrUPF24AI1FqtvlhZbeYLe/EQVJaC2fqcODw2Xp5px" +
                    "j1h4lS6tGXQqRZ1we0i4Wf/S+1/A4GDcb3B7hfpkRJP6AYvLxisBlP2qj";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    private final boolean debugMode = false;
    /*
    End TensorFlow Stuff.
     */

    private HardwarePushbot drive = new HardwarePushbot();

    @Override
    public void runOpMode() throws InterruptedException {

        drive.init(hardwareMap);

//        initVuforia();
//        initTfod();
//        if (tfod != null) {
//            tfod.activate();
//            tfod.setZoom(1.3, 16.0/9.0);
//        }
//
//        // Declare positions variables
//        char positionDetected = 'N';
//        while (positionDetected == 'N') {
//            positionDetected = detectPosition();
//        }
//
//        char positionR = positionDetected;
//        telemetry.addData("Pos Detected: ", positionR);
//        telemetry.update();
        String robotPositionR = "RL";
//
//        // Set path values
//        String POSITION_KEY = "LCR";
        String[] ROBOT_POSITION_KEY = {
                "BL", "BR",
                "RL", "RR"
        };
//        int position = POSITION_KEY.indexOf(positionR) - 1;
        int robotPosition = Arrays.asList(ROBOT_POSITION_KEY).indexOf(robotPositionR);
//        double shippingAngleTurnCalc;
//        double shippingDistCalc;
//        double elementTurnCalc;
//        double elementDistCalc;
//
//        Pose2d startPos;
//        int s = 1;
//
//        switch (robotPosition) {
//            case 0:
//                startPos = new Pose2d(-42, 61.75, Math.toRadians(270));
//                s = -1;
//                break;
//            case 1:
//                startPos = new Pose2d(11, 61.75, Math.toRadians(270));
//                position *= -1;
//                break;
//            case 2:
//                startPos = new Pose2d(-42, -61.75, Math.toRadians(90));
//                break;
//            case 3:
//                startPos = new Pose2d(11, -61.75, Math.toRadians(90));
//                s = -1;
//                position *= -1;
//                break;
//            default:
//                startPos = new Pose2d(0, 0, Math.toRadians(90));
//                break;
//        }
//
//        int sign = s;
//
//        switch (position) {
//            case 1:
//                shippingAngleTurnCalc = -74;
//                shippingDistCalc = 24.0;
//                elementTurnCalc = 13;
//                elementDistCalc = 29;
//                break;
//            case 0:
//                shippingAngleTurnCalc = -62.0;
//                shippingDistCalc = 20.0;
//                elementTurnCalc = 5;
//                elementDistCalc = 24;
//                break;
//            default:
//                shippingAngleTurnCalc = -36.0;
//                shippingDistCalc = 15.0;
//                elementTurnCalc = -19;
//                elementDistCalc = 26;
//                break;
//        }
//
//        double shippingAngleTurn = shippingAngleTurnCalc;
//        double shippingDist = shippingDistCalc;
//        double elementTurn = elementTurnCalc;
//        double elementDist = elementDistCalc;
//        int pos = position;
//
//        drive.setPoseEstimate(startPos);
//
//        telemetry.addData("Ready", "to start");
//        telemetry.update();
//
//        waitForStart();
//
//        if (isStopRequested()) return;
//
//        TrajectorySequence trajSeq0 = drive.trajectorySequenceBuilder(startPos)
//                .forward(7)
//                .build();
//
//        TrajectorySequence trajSeq1 = drive.trajectorySequenceBuilder(startPos)
//                .turn(sign * Math.toRadians(elementTurn))
//                .forward(elementDist)
//                .build();
//
//        TrajectorySequence trajSeq2 = drive.trajectorySequenceBuilder(startPos)
//                .turn(Math.toRadians(90))
//                .forward(20)
//                .build();
//
//        TrajectorySequence trajSeq3 = drive.trajectorySequenceBuilder(startPos)
//                .back(20)
//                .turn(Math.toRadians(-23))
//                .build();
//
//        TrajectorySequence trajSeq4 = drive.trajectorySequenceBuilder(startPos)
//                .waitSeconds(1)
//                .turn(sign * Math.toRadians(shippingAngleTurn))
//                .build();
//
//        TrajectorySequence trajSeq5 = drive.trajectorySequenceBuilder(startPos)
//                .turn(Math.toRadians(225))
//                .back(shippingDist)
//                .build();
//
//        drive.closeArm();
//        sleep(1000);
//        drive.armLift.setPower(0.2);
//        drive.armLift.setTargetPosition(500);
//        while (drive.armLift.isBusy() && !opModeIsActive()) {}
//        drive.followTrajectorySequence(trajSeq0);
//        drive.followTrajectorySequence(trajSeq2);
//        drive.armLift.setPower(0.2);
//        drive.armLift.setTargetPosition(0);
//        while (drive.armLift.isBusy() && opModeIsActive()) {}
//        drive.openArm();
//        drive.followTrajectorySequence(trajSeq3);
//        drive.followTrajectorySequence(trajSeq1);
//        drive.closeArm();
//        drive.followTrajectorySequence(trajSeq4);
//        drive.armLift.setPower(0.2);
//        drive.armLift.setTargetPosition(2731);
//        while (drive.armLift.isBusy() && opModeIsActive()) {}
//        drive.followTrajectorySequence(trajSeq5);
//        drive.openArm();
//
//        sleep(1000);
////        drive.armLift.setPower(-0.4);
//        telemetry.addData("Step", "0 : " + pos);
//        telemetry.update();
////        drive.followTrajectorySequence(trajSeq2);
//        telemetry.addData("Done", "Finished");
//        telemetry.update();
//
//        drive.openArm();
//        drive.armLift.setPower(0.2);
//        drive.armLift.setTargetPosition(0);
//        while (drive.armLift.isBusy() && opModeIsActive()) {}

        waitForStart();

        if (isStopRequested()) return;


//        drive.closeArm();
//        sleep(1000);
//        switch (robotPositionR) {
//            case "BL": {
//                int dist = 47;
//                int turn = 105;
//                int dist2 = 37;
//                TrajectorySequence traj0 = drive.trajectorySequenceBuilder(new Pose2d(0, 0, 0))
//                        .forward(dist)
//                        .turn(-Math.toRadians(turn))
//                        .forward(dist2)
//                        .build();
//                drive.armLift.setPower(0.2);
//                drive.armLift.setTargetPosition(500);
//                while (drive.armLift.isBusy() && opModeIsActive()) {
//                }
//                drive.followTrajectorySequence(traj0);
//                break;
//            }
//            case "BR": {
//                int dist = 37;
//                int turn = 100;
//                int dist2 = 90;
//                TrajectorySequence traj0 = drive.trajectorySequenceBuilder(new Pose2d(1, 0, 0))
//                        .forward(dist)
//                        .turn(Math.toRadians(turn))
//                        .forward(dist2)
//                        .build();
//                drive.armLift.setPower(0.2);
//                drive.armLift.setTargetPosition(500);
//                while (drive.armLift.isBusy() && opModeIsActive()) {
//                }
//                drive.followTrajectorySequence(traj0);
//                break;
//            }
//            case "RR": {
//                int dist = 47;
//                int turn = 105;
//                int dist2 = 37;
//                TrajectorySequence traj0 = drive.trajectorySequenceBuilder(new Pose2d(0, 0, 0))
//                        .forward(dist)
//                        .turn(-Math.toRadians(turn))
//                        .forward(dist2)
//                        .turn(-10)
//                        .build();
//                drive.armLift.setPower(0.2);
//                drive.armLift.setTargetPosition(500);
//                while (drive.armLift.isBusy() && opModeIsActive()) {
//                }
//                drive.followTrajectorySequence(traj0);
//                break;
//            }
//            default: {
//                int dist = 47;
//                int turn = 85;
//                int dist2 = 37;
//                TrajectorySequence traj0 = drive.trajectorySequenceBuilder(new Pose2d(0, 0, 0))
//                        .forward(dist)
//                        .turn(Math.toRadians(turn))
//                        .forward(dist2)
//                        .build();
//                drive.armLift.setPower(0.2);
//                drive.armLift.setTargetPosition(500);
//                while (drive.armLift.isBusy() && opModeIsActive()) {
//                }
//                drive.followTrajectorySequence(traj0);
//                break;
//            }
//        }
        telemetry.addData("STRING BOI", "done");
    }

    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "ddcam");

        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.9f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }

    @SuppressLint("DefaultLocale")
    private char detectPosition() {
        int pos = 3;
        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
        if (updatedRecognitions != null) {
            if (debugMode) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());
            }
            // step through the list of recognitions and display boundary info.
            int i = 0;
            for (Recognition recognition : updatedRecognitions) {
                if (debugMode) {
                    telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                    telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                            recognition.getLeft(), recognition.getTop());
                    telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                            recognition.getRight(), recognition.getBottom());
                }
                i++;


                pos = Math.round(((recognition.getLeft() + recognition.getWidth() / 2) / 100 + 1) / 2) - 2;
            }
            if (debugMode) {
                telemetry.update();
            }
        }

        if (debugMode) {
            telemetry.addData("Element Pos", pos);
            telemetry.update();
        }

        if (pos == -1) {
            pos = 1;
        }

        return POSITION_KEY[pos];
    }

    private void moveForward(double inches, double speed) {
        int ticksToTravel = (int) ((int) (inches / 11.8737122661) * 537.7);

        if (speed > 0) {
            double leftPower = -speed;
            double rightPower = -speed;
            drive.frontRightDrive.setTargetPosition(drive.frontRightDrive.getCurrentPosition() + ticksToTravel);
            drive.frontRightDrive.setPower(rightPower);
            drive.frontLeftDrive.setTargetPosition(drive.frontLeftDrive.getCurrentPosition() + ticksToTravel);
            drive.frontLeftDrive.setPower(leftPower);
            drive.backRightDrive.setTargetPosition(drive.backRightDrive.getCurrentPosition() + ticksToTravel);
            drive.backRightDrive.setPower(rightPower);
            drive.backLeftDrive.setTargetPosition(drive.backLeftDrive.getCurrentPosition() + ticksToTravel);
            drive.backLeftDrive.setPower(leftPower);
        } else if (speed < 0) {
            double leftPower = -speed;
            double rightPower = -speed;
            drive.frontRightDrive.setTargetPosition(drive.frontRightDrive.getCurrentPosition() + ticksToTravel);
            drive.frontRightDrive.setPower(rightPower);
            drive.frontLeftDrive.setTargetPosition(drive.frontLeftDrive.getCurrentPosition() + ticksToTravel);
            drive.frontLeftDrive.setPower(leftPower);
            drive.backRightDrive.setTargetPosition(drive.backRightDrive.getCurrentPosition() + ticksToTravel);
            drive.backRightDrive.setPower(rightPower);
            drive.backLeftDrive.setTargetPosition(drive.backLeftDrive.getCurrentPosition() + ticksToTravel);
            drive.backLeftDrive.setPower(leftPower);
        }
    }



    private void turnDegrees(double degrees, double speed) {
        int ticksToTravel = (int) (((int) degrees) * 6.91574444491);
        double leftPower = -speed;
        double rightPower = -speed;
        drive.frontRightDrive.setTargetPosition(drive.frontRightDrive.getCurrentPosition() + ticksToTravel);
        drive.frontRightDrive.setPower(rightPower);
        drive.frontLeftDrive.setTargetPosition(drive.frontLeftDrive.getCurrentPosition() - ticksToTravel);
        drive.frontLeftDrive.setPower(leftPower);
        drive.backRightDrive.setTargetPosition(drive.backRightDrive.getCurrentPosition() + ticksToTravel);
        drive.backRightDrive.setPower(rightPower);
        drive.backLeftDrive.setTargetPosition(drive.backLeftDrive.getCurrentPosition() - ticksToTravel);
        drive.backLeftDrive.setPower(leftPower);
    }
}
