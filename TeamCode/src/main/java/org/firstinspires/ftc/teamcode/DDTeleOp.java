/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="DD TeleOp", group="Linear Opmode")

public class DDTeleOp extends LinearOpMode {
    // Declares the robot
    HardwarePushbot robot = new HardwarePushbot();

    boolean gripperIsHeld = false;
    boolean gripperResult = false;

    int[] positions = {
            2731,// high
            3283, // middle
            3692, // low
            0,  // reset
            500 // transportation mode
    };

    double targetArmPos = 15;

    @Override
    public void runOpMode() throws InterruptedException {
        // Make sure your ID's match your configuration
        robot.init(hardwareMap);

        /*
        This code is to stabilize the arm prior to the run.

        robot.resetArm();
        */

        // Waits for the start button to be pressed
        waitForStart();

        // Checks if stop is pressed by user
        if (isStopRequested()) return;

        int armTarget = 0;
        double armSpeed = 0;
        double armSpeedMove = 0.75;

        while (opModeIsActive()) {
            // Check if variable speed is required
            robot.getMovementSpeed(gamepad1);

            // Move arm lift to target position
            if (gamepad2.y){
                armTarget = positions[0];
                armSpeed = armSpeedMove;
                robot.armLift.setTargetPosition(armTarget);
                robot.armLift.setPower(armSpeed);
            } else if (gamepad2.b) {
                armTarget = positions[1];
                armSpeed = armSpeedMove;
                robot.armLift.setTargetPosition(armTarget);
                robot.armLift.setPower(armSpeed);
            } else if (gamepad2.a) {
                armTarget = positions[2];
                armSpeed = armSpeedMove;
                robot.armLift.setTargetPosition(armTarget);
                robot.armLift.setPower(armSpeed);
            } else if (gamepad2.x) {
                armTarget = positions[3];
                armSpeed = armSpeedMove;
                robot.armLift.setTargetPosition(armTarget);
                robot.armLift.setPower(armSpeed);
            } else if (gamepad2.start) {
                armTarget = positions[4];
                armSpeed = armSpeedMove;
                robot.armLift.setTargetPosition(armTarget);
                robot.armLift.setPower(armSpeed);
            }

            // Take in the left and right powers for the motors through joystick
            double leftPower = (-gamepad1.left_stick_y + gamepad1.left_stick_x) * robot.speed;
            double rightPower = (-gamepad1.left_stick_y - gamepad1.left_stick_x) * robot.speed;

            // Check if the robot has hit the carousel and if so, spin
//            if (!robot.carSw.getState()) {
//                robot.carousel.setPower(0.5);
//            } else {
//                robot.carousel.setPower(0.0);
//            }

            if (gamepad1.right_stick_x > 0) {
                robot.carousel.setPower(-0.5);
            } else if (gamepad1.right_stick_x < 0) {
                robot.carousel.setPower(0.5);
            } else {
                robot.carousel.setPower(0.0);
            }

            // Toggle arm gripper
//            if (gamepad2.left_bumper && !gripperIsHeld) {
//                gripperIsHeld = true;
//                gripperResult = !gripperResult;
//                robot.armLift.;
//            } else if (!gamepad2.left_bumper) {
//                gripperIsHeld = false;
//            }
            //Right button to open claw
            //Left button to close claw
            if (gamepad2.right_bumper) {
                robot.armGripper.setPosition(0.0);
            } else if (gamepad2.left_bumper) {
                robot.armGripper.setPosition(1.0);
            }

            if (gamepad2.right_trigger > 0.1) {
                armTarget = robot.armLift.getCurrentPosition() + 10;
                armSpeed = 0.35;
                robot.armLift.setTargetPosition(armTarget);
                robot.armLift.setPower(armSpeed);
            } else if (gamepad2.left_trigger > 0.1) {
                armTarget = robot.armLift.getCurrentPosition() - 10;
                armSpeed = 0.35;
                robot.armLift.setTargetPosition(armTarget);
                robot.armLift.setPower(armSpeed);
            }

            if (gamepad1.back) {
                robot.armLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                robot.armLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }

//            robot.armGripper.setPosition(gripperResult ? 1.0 : 0.0);

            // Move the robot according to the joystick powers
            robot.leftPower = leftPower;
            robot.rightPower = rightPower;

            robot.setMoveMotors();

            // Adds data to telemetry
            robot.debug(true, telemetry, gamepad1, gamepad2);
        }
    }
}