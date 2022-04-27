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

package org.firstinspires.ftc.teamcode.TeleOp;

import androidx.annotation.RequiresPermission;

import com.acmerobotics.roadrunner.control.PIDFController;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Drive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.util.ReadJSON;
import org.firstinspires.ftc.teamcode.util.SaveJSON;
import org.json.JSONException;
import org.json.JSONObject;

/*
    Dragon Droids Team #19643
    DDTeleOp
*/

@TeleOp(name="MainTeleOp", group="Linear Opmode")

public class DDTeleOp extends LinearOpMode {
    // Initialize robot parameters
    Drive robot = null;

    int[] positions = {
            -587, // High Position
            -360, // Middle Position
            0,    // Reset Position (Bottom)
    };

    @Override
    public void runOpMode() throws InterruptedException {
        // Make sure your ID's match your configuration
        robot = new Drive(hardwareMap);
        String side = "n/a";

        // Wait for the Start Button
        waitForStart();

        JSONObject storedPos;

        Pose2d begin;

        try {
            ReadJSON readJSON = new ReadJSON("position.json");
            storedPos = new JSONObject(readJSON.value);
            begin = new Pose2d(storedPos.getDouble("x"), storedPos.getDouble("y"), storedPos.getDouble("heading"));
            robot.setPoseEstimate(begin);
            telemetry.addData("Position Data Retrieval", "Succeeded");
            side = begin.getY() > 0 ? "blue" : "red";
        } catch (JSONException e) {
            telemetry.addData("Position Data Retrieval", "Failed");
            telemetry.addData("Error", e.getMessage());
        }
        telemetry.update();

        // If the user stops the program, cancel the OpMode loop
        if (isStopRequested()) return;

        int armTarget;
        double armSpeed;
        double armSpeedMove = 0.75;
        double carouselSpeed = 0.65;

        int currentHeight = 0;

        PIDFController headingController = new PIDFController(Drive.HEADING_PID);

        headingController.setInputBounds(-Math.PI, Math.PI);

        Pose2d goal = new Pose2d(0,0,0);

        if (side.equals("blue")) {
            goal = new Pose2d(-11, 22.75, 0);
        } else if (side.equals("red")) {
            goal = new Pose2d(-11, -24.5, 0);
        }

        int distanceToAlign = 23; // (inches)

        while (opModeIsActive()) {
            robot.update();
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
                robot.armClamp.setPosition(0.5);
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

            int sensitivity = 1;

            if (gamepad1.left_trigger > 0.1) {
                sensitivity = 2;
            }

            // Turn to aim at
            if (gamepad1.right_bumper && !robot.isBusy()) {
                headingController.setTargetPosition((Math.atan2(goal.minus(robot.getPoseEstimate()).getY(), goal.minus(robot.getPoseEstimate()).getX()) - robot.getPoseEstimate().getHeading() + Math.PI) % (2 * Math.PI));
                headingController.update(robot.getPoseEstimate().getHeading());
                double distC = Math.hypot(goal.getX() - robot.getPoseEstimate().getX(), goal.getY() - robot.getPoseEstimate().getY());
                TrajectorySequence alignTo = robot.trajectorySequenceBuilder(robot.getPoseEstimate())
                        .turn(headingController.getTargetPosition() % (2*Math.PI))
                        .back(distC - distanceToAlign)
                        .build();
                robot.followTrajectorySequenceAsync(alignTo);
            }

            // Calculate the left and right powers to apply to the motors based on joystick
            double leftPower = (-gamepad1.left_stick_y + gamepad1.left_stick_x / sensitivity) * robot.speed;
            double rightPower = (-gamepad1.left_stick_y - gamepad1.left_stick_x / sensitivity) * robot.speed;

            // Spin carousel in specified direction, based on joystick
            if (gamepad1.right_stick_x > 0) {
                robot.carouselSpinner.setPower(-carouselSpeed);
            } else if (gamepad1.right_stick_x < 0) {
                robot.carouselSpinner.setPower(carouselSpeed);
            } else {
                robot.carouselSpinner.setPower(0.0);
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

            if ((gamepad2.left_bumper || gamepad2.right_bumper) && currentHeight != 0) {
                if (currentHeight == -575) {
                    robot.armClamp.setPosition(0.0);
                } else {
                    robot.armClamp.setPosition(1.0);
                }
            }

            if (gamepad1.left_bumper) {
                goal = robot.getPoseEstimate();
            }

            // Apply calculated powers to the robot
            if (!robot.isBusy()) {
                robot.setMotorPowers(leftPower, rightPower);
            }

            // Add data to telemetry, for debugging
            robot.debug(true, telemetry, goal);
        }

        JSONObject position = new JSONObject();

        try {
            position.put("x", robot.getPoseEstimate().getX());
            position.put("y", robot.getPoseEstimate().getY());
            position.put("heading", robot.getPoseEstimate().getHeading());
            new SaveJSON("position.json", position.toString());
            telemetry.addData("Position Save", "Succeeded");
        } catch (Exception e) {
            telemetry.addData("Position Save", "Failed");
        }
        telemetry.update();
    }
}