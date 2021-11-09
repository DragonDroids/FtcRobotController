package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.libs.Move;

import java.util.List;

@Autonomous(name = "DDAuto", group = "Linear Opmode")
public class WebCamAutonomous extends LinearOpMode {
    HardwarePushbot robot = new HardwarePushbot();

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
    /*
    End TensorFlow Stuff.
     */

    private char positionDetected = 'a';

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);

        initVuforia();
        initTfod();

        if (tfod != null) {
            tfod.activate();

            tfod.setZoom(1.3, 16.0/9.0);
        }

        telemetry.addData(">", "Press Play to start OP mode");
        telemetry.update();
        waitForStart();
        sleep(500);

        positionDetected = detectPosition();
        while (positionDetected == 'N') {
            positionDetected = detectPosition();
        }

        telemetry.addData("Positions", positionDetected);
        telemetry.update();

        int distTravelledCar;
        distTravelledCar = Move.moveMotors(0.0, -0.2, 0.0, 30, true);
        robot.carousel.setPower(-0.5);
        sleep(2500);
        robot.carousel.setPower(0);
        Move.moveMotors(0.0, 0.2, 0.0, distTravelledCar, false);
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

    private char detectPosition() {
        int pos = 3;
        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
        if (updatedRecognitions != null) {
            telemetry.addData("# Object Detected", updatedRecognitions.size());
            // step through the list of recognitions and display boundary info.
            int i = 0;
            for (Recognition recognition : updatedRecognitions) {
                telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                        recognition.getLeft(), recognition.getTop());
                telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                        recognition.getRight(), recognition.getBottom());
                i++;
                int position = Math.round(((recognition.getLeft() + recognition.getWidth() / 2) / 100 + 1) / 2) - 2;

                telemetry.addData("Element Pos", position);

                pos = position;
            }
            telemetry.update();
        }

        return POSITION_KEY[pos];
    }

    public void stopMotors() {
        robot.frontLeftDrive.setPower(0);
        robot.frontRightDrive.setPower(0);
        robot.backLeftDrive.setPower(0);
        robot.backRightDrive.setPower(0);
    }

    public void moveMotors(double x, double y, double rx, int time, boolean carousel) {
        double frontLeftPower = y + x + rx;
        double backLeftPower = y - x + rx;
        double frontRightPower = y - x - rx;
        double backRightPower = y + x - rx;

        //double positions = (robot.frontLeftDrive.getCurrentPosition() + robot.frontRightDrive.getCurrentPosition() + robot.backLeftDrive.getCurrentPosition() + robot.backRightDrive.getCurrentPosition()) / 4

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

        // Set Powers
        robot.frontLeftDrive.setPower(frontLeftPower);
        robot.backLeftDrive.setPower(backLeftPower);
        robot.frontRightDrive.setPower(frontRightPower);
        robot.backRightDrive.setPower(backRightPower);

        if (!carousel) {
            sleep(time);
        } else {
            while (robot.carSw.getState()) {
                sleep(30);
            }
        }

        stopMotors();
    }
}