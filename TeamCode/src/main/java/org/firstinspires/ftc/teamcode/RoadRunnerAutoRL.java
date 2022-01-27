package org.firstinspires.ftc.teamcode;

import android.annotation.SuppressLint;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import java.util.List;

/*
 * Op mode for preliminary tuning of the follower PID coefficients (located in the drive base
 * classes). The robot drives in a DISTANCE-by-DISTANCE square indefinitely. Utilization of the
 * dashboard is recommended for this tuning routine. To access the dashboard, connect your computer
 * to the RC's WiFi network. In your browser, navigate to https://192.168.49.1:8080/dash if you're
 * using the RC phone or https://192.168.43.1:8080/dash if you are using the Control Hub. Once
 * you've successfully connected, start the program, and your robot will begin driving in a square.
 * You should observe the target position (green) and your pose estimate (blue) and
 *  adjust your
 * follower PID coefficients such that you follow the target position as accurately as possible.
 * If you are using SampleTankDrive, you should be tuning TRANSLATIONAL_PID and HEADING_PID.
 * If you are using SampleTankDrive, you should be tuning AXIAL_PID, CROSS_TRACK_PID, and HEADING_PID.
 * These coefficients can be tuned live in dashboard.
 */

@Config

@Autonomous(group = "drive")
public class RoadRunnerAutoRL extends LinearOpMode {
    public int[] armPositions = {
            -320,// bottom
            -450, // middle
            -545, // high
    };

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

    private final boolean debugMode = true;
    /*
    End TensorFlow Stuff.
     */

    @Override
    public void runOpMode() throws InterruptedException {
        RoadRunnerAutoHardware drive = new RoadRunnerAutoHardware(hardwareMap);

        initVuforia();
        initTfod();
        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(1.3, 16.0/9.0);
        }

        // Declare positions variables
        char positionDetected = 'N';
        while (positionDetected == 'N' && (opModeIsActive() || !isStopRequested())) {
            positionDetected = detectPosition();
        }

        telemetry.addData("Pos Detected: ", positionDetected);
        // telemetry.update();

        int index = "RCL".indexOf(positionDetected);


        waitForStart();

        if (isStopRequested()) return;


        sleep(1000);


        drive.setPoseEstimate(new Pose2d(12,-61,Math.toRadians(270)));

        TrajectorySequence trajectorySequence0 = drive.trajectorySequenceBuilder(new Pose2d(12,-61,Math.toRadians(270)))
                .forward(6)
                .turn(-Math.toRadians(162))
                .back(18)
                .build();

        TrajectorySequence trajectorySequence1 = drive.trajectorySequenceBuilder(trajectorySequence0.end())
                .back(3 + ((index % 2) * 3))
                .build();

        TrajectorySequence trajectorySequence2 = drive.trajectorySequenceBuilder(trajectorySequence1.end())
                .forward(12)
                .turn(Math.toRadians(78))
                .forward(70)
                .build();

        drive.followTrajectorySequence(trajectorySequence0);

        drive.armLift.setTargetPosition(armPositions[index]);
        drive.armLift.setPower(0.75);
        while (drive.armLift.isBusy() && opModeIsActive()) {}
        DriveConstants.MAX_ACCEL = 80;
        drive.followTrajectorySequence(trajectorySequence1);

        if (armPositions[index] == -545) {
            drive.armClamp.setPosition(0.0);
        } else if(armPositions[index] != 0) {
            drive.armClamp.setPosition(1.0);
        }

        sleep(2000);

        drive.armClamp.setPosition(0.5);
        drive.armLift.setTargetPosition(0);
        drive.armLift.setPower(0.75);

        DriveConstants.MAX_ACCEL = 90;

        while (drive.armLift.isBusy() && opModeIsActive()) {}

        drive.followTrajectorySequence(trajectorySequence2);

        telemetry.addData("Run", "Done!");
        telemetry.update();
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

                int centerX = Math.round(recognition.getLeft() + (recognition.getWidth() / 2));

                if (centerX >= 0 && centerX < 200) {
                    pos = 0;
                } else if (centerX >= 200 && centerX < 400) {
                    pos = 1;
                } else if (centerX >= 400 && centerX <= 600) {
                    pos = 2;
                }
            }
            if (debugMode) {
                telemetry.update();
            }
        }

        if (debugMode) {
            telemetry.addData("Element Pos", pos);
            telemetry.update();
        }

        return POSITION_KEY[pos];
    }
}
