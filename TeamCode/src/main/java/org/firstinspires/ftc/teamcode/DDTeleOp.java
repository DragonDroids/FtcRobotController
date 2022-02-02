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

/*
    Dragon Droids Team #19643
    DDTeleOp
*/

@TeleOp(name="MainTeleOp", group="Linear Opmode")

public class DDTeleOp extends LinearOpMode {
    // Initialize robot parameters
    HardwarePushbot robot = new HardwarePushbot();

    int[] positions = {
            -575, // High Position
            -360, // Middle Position
            0,    // Reset Position (Bottom)
    };


    @Override
    public void runOpMode() throws InterruptedException {
        // Make sure your ID's match your configuration
        robot.init(hardwareMap);

        // Wait for the Start Button
        waitForStart();

        // If the user stops the program, cancel the OpMode loop
        if (isStopRequested()) return;

        int armTarget;
        double armSpeed;
        double armSpeedMove = 0.75;
        double carouselSpeed = 0.65;

        int currentHeight = 0;

        while (opModeIsActive()) {
            // If variable speed is required, apply it
            robot.getMovementSpeed(gamepad1);

            // Move Arm Lift to target position, based on button press
            if (gamepad2.y){
                armTarget = positions[0];
                armSpeed = armSpeedMove;
                robot.armLift.setTargetPosition(armTarget);
                robot.armLift.setPower(armSpeed);
                currentHeight = positions[0];
            } else if (gamepad2.b) {
                armTarget = positions[1];
                armSpeed = armSpeedMove;
                robot.armLift.setTargetPosition(armTarget);
                robot.armLift.setPower(armSpeed);
                currentHeight = positions[1];
            } else if (gamepad2.a) {
                armTarget = positions[2];
                armSpeed = armSpeedMove;
                robot.armLift.setTargetPosition(armTarget);
                robot.armLift.setPower(armSpeed);
                robot.armGripper.setPosition(0.5);
                currentHeight = positions[2];
            }

            // Run Intake in specified direction, based on button press
            if (gamepad1.a) {
                robot.motorTest.setPower(1);
            } else if (gamepad1.b) {
                robot.motorTest.setPower(-1);
            } else {
                robot.motorTest.setPower(0);
            }

            // Calculate the left and right powers to apply to the motors based on joystick
            double leftPower = (-gamepad1.left_stick_y + gamepad1.left_stick_x) * robot.speed;
            double rightPower = (-gamepad1.left_stick_y - gamepad1.left_stick_x) * robot.speed;

            // Spin carousel in specified direction, based on joystick
            if (gamepad1.right_stick_x > 0) {
                robot.carousel.setPower(-carouselSpeed);
            } else if (gamepad1.right_stick_x < 0) {
                robot.carousel.setPower(carouselSpeed);
            } else {
                robot.carousel.setPower(0.0);
            }

            // Fine-adjust Arm Lift, based on trigger
            if (gamepad2.right_trigger > 0.1) {
                armTarget = robot.armLift.getCurrentPosition() + 20;
                armSpeed = 0.35;
                robot.armLift.setTargetPosition(armTarget);
                robot.armLift.setPower(armSpeed);
            } else if (gamepad2.left_trigger > 0.1) {
                armTarget = robot.armLift.getCurrentPosition() - 20;
                armSpeed = 0.35;
                robot.armLift.setTargetPosition(armTarget);
                robot.armLift.setPower(armSpeed);
            }

            // Rotate drop-off box based on bumper
            if (gamepad2.right_bumper) {
                robot.armGripper.setPosition(0.5);
            } else if (gamepad2.left_bumper && currentHeight != 0) {
                if (currentHeight == -575) {
                    robot.armGripper.setPosition(0.0);
                } else {
                    robot.armGripper.setPosition(1.0);
                }
            }

            // Apply calculated powers to the robot
            robot.leftPower = leftPower;
            robot.rightPower = rightPower;

            robot.setMoveMotors();

            // Add data to telemetry, for debugging
            robot.debug(true, telemetry);
        }
    }
}