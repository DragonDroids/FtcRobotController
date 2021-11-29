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

import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.control.PIDFController;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.acmerobotics.roadrunner.*;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@TeleOp(name="DD TeleOp", group="Linear Opmode")

public class DDTeleOp extends LinearOpMode {
    HardwarePushbot robot = new HardwarePushbot();
    double maxSpeed = 1;
    double minSpeed = 0.5;
    boolean variableSpeed = true;

    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration
        robot.init(hardwareMap);

        // Reverse the right side motors
        // Reverse left motors if you are using NeveRests
        robot.frontRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        robot.backRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        robot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        /*
        This code is to stabilize the arm prior to the run.
        */
//        robot.armLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        robot.armLift.setTargetPosition((int) Math.round((10 / 360) * 537.7));
//        robot.armLift.setPower(0.4);
        //sleep(100);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            if (variableSpeed) {
                robot.speed = minSpeed + (gamepad1.right_trigger * (maxSpeed - minSpeed));
            } else {
                robot.speed = gamepad1.right_bumper ? maxSpeed : minSpeed;
            }

            if (!robot.carSw.getState()) {
                robot.carousel.setPower(-0.5);
            } else {
                robot.carousel.setPower(0.0);
            }

            // Find each motor powers
            double y = gamepad1.left_stick_y * robot.speed; // Remember, this is reversed!
            double x = -gamepad1.left_stick_x * robot.speed; // Counteract imperfect strafing
            double rx = -gamepad1.right_stick_x + robot.lastError;

            robot.updateHeading();

            robot.setMoveMotors(x, y, rx);

            robot.debug(true, telemetry);
        }
    }
}