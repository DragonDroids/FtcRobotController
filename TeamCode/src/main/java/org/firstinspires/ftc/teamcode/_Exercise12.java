package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous()
public class _Exercise12 extends LinearOpMode {
    enum State {
        START,
        QUARTER_SPEED,
        HALF_SPEED,
        THREE_SPEED,
        FULL_SPEED,
        DONE
    }

    _ProgrammingBoard12 board = new _ProgrammingBoard12();
    State state = State.START;
    double lastTime;

    @Override
    public void runOpMode() {
        board.init(hardwareMap);

        state = State.START;


        waitForStart();
        while (opModeIsActive()) {
            telemetry.addData("State", state);
            switch (state) {
                case START:
                    board.setMotorSpeed(0.250);
                    state = State.QUARTER_SPEED;
                    lastTime = getRuntime();
                    break;
                case QUARTER_SPEED:
                    if (getRuntime() > lastTime + .250) {
                        board.setMotorSpeed(0.500);
                        state = State.HALF_SPEED;
                        lastTime = getRuntime();
                    }
                    break;
                case HALF_SPEED:
                    if (getRuntime() > lastTime + .250) {
                        board.setMotorSpeed(0.750);
                        state = State.THREE_SPEED;
                        lastTime = getRuntime();
                    }
                    break;
                case THREE_SPEED:
                    if (getRuntime() > lastTime + .250) {
                        board.setMotorSpeed(1.00);
                        state = State.FULL_SPEED;
                        lastTime = getRuntime();
                    }
                    break;
                case FULL_SPEED:
                    if (board.isTouchSensorPressed()) {
                        board.setMotorSpeed(0.0);
                        state = State.DONE;
                        lastTime = getRuntime();
                    }
                    break;
                default:
                    telemetry.addData("Auto", "Finished");
            }
        }
    }
}