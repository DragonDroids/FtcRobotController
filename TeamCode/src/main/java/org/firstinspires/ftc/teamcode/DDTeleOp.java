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

@TeleOp(name="DD TeleOp", group="Linear Opmode")

public class DDTeleOp extends LinearOpMode {
    // Declares the robot
    HardwarePushbot robot = new HardwarePushbot();

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

        while (opModeIsActive()) {
            // Check if variable speed is required
            robot.getMovementSpeed(gamepad1);

            // Move arm lift if operator pressed the right trigger or left trigger
            if (gamepad2.right_trigger != 0) {
                robot.armLift.setPower(gamepad2.right_trigger);
            } else if (gamepad2.left_trigger != 0) {
                robot.armLift.setPower(-gamepad2.left_trigger);
            } else {
                robot.armLift.setPower(0.0);
            }

            // Add button so that the robot can apply it's "brakes"
            if (gamepad1.right_bumper) {
                robot.move = !robot.move;
            }

            // Take in the left and right powers for the motors through joystick
            double leftPower = gamepad1.left_stick_y * robot.speed;
            double rightPower = gamepad1.right_stick_y * robot.speed;

            // Check if the robot has hit the carousel and if so spin
            if (!robot.carSw.getState()) {
                robot.carousel.setPower(-0.5);
            } else {
                robot.carousel.setPower(0.0);
            }

            // Move the robot according to the joystick powers
            if (robot.move) {
                robot.leftPower = leftPower;
                robot.rightPower = rightPower;
            } else {
                robot.leftPower = 0;
                robot.rightPower = 0;
            }

            robot.setMoveMotors();

            // Adds data to telemetry
            robot.debug(true, telemetry);
        }
    }
}