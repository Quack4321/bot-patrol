/*
Copyright 2023 FIRST Tech Challenge Team 9505

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp

public class Drive extends OpMode {
    /* Declare OpMode members. */

    DcMotor rightFront;
    DcMotor leftFront;
    DcMotor rightBack;
    DcMotor leftBack;
    DcMotor grabExtend;

    DcMotor grabPivot;
    DcMotor pullPivot;
    DcMotor pullExtend;

    Servo wrist;
    CRServo spinny;

    int speedIndex;
    float[] speed;

    // Predefined positions of the motors:
    String grabArmPosition;
    int pullPivotStart;
    int pullPivotRest;
    int pullPivotPull;
    int pullPivotClimb;
    int pullExtendIn;
    int pullExtendOut;
    int grabPivotRest;
    int grabPivotGrab;
    int grabPivotScore;
    int grabExtendIn;
    int grabExtendMid;
    int grabExtendOut;
    double wristRest;
    double wristGrab;
    double wristParallel;
    double wristScore;
    long preTime;
    boolean driverMode;

    // Controller 1 Variables:
    boolean rightBumperLastTime;
    boolean leftBumperLastTime;
    boolean aLastTime;
    boolean yLastTime;
    boolean xLastTime;
    boolean bLastTime;
    boolean dpadUpLastTime;
    boolean dpadDownLastTime;
    boolean dpadLeftLastTime;
    boolean dpadRightLastTime;
    boolean rightStickButtonLastTime;
    boolean leftStickButtonLastTime;

    // Controller 2 Variables:
    boolean rightBumperLastTime2;
    boolean leftBumperLastTime2;
    boolean aLastTime2;
    boolean yLastTime2;
    boolean xLastTime2;
    boolean bLastTime2;
    boolean dpadUpLastTime2;
    boolean dpadDownLastTime2;
    boolean dpadLeftLastTime2;
    boolean dpadRightLastTime2;
    boolean rightStickButtonLastTime2;
    boolean leftStickButtonLastTime2;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        speed = new float[]{.25f, .5f, .75f, 1.0f};
        speedIndex = 3;

        // Initialize hardware values
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");                  // Control Hub 3
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");                // Control Hub 2
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");                // Control Hub 1
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");              // Control Hub 0

        grabExtend = hardwareMap.get(DcMotor.class, "grabExtend");              // Expansion Hub 0
        grabPivot = hardwareMap.get(DcMotor.class, "grabPivot");                // Expansion Hub 1
        pullPivot = hardwareMap.get(DcMotor.class, "pullPivot");                // Expansion Hub 2
        pullExtend = hardwareMap.get(DcMotor.class, "pullExtend");              // Expansion Hub 3

        wrist = hardwareMap.get(Servo.class, "wrist");                          // Control Hub 0
        spinny = hardwareMap.get(CRServo.class, "spinny");                      // Control Hub 1

        // Set motors to brake upon zero power:
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        pullExtend.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        grabExtend.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        pullPivot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        grabPivot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Sets these motors to run in correct direction:
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        spinny.setDirection(DcMotorSimple.Direction.FORWARD);

        driverMode = false;

        // Predefined motor positions:
        pullPivotStart = 6300;
        pullPivotRest = 5075;
        pullPivotPull = 0;
        pullPivotClimb = 0;
        pullExtendIn = 0;
        pullExtendOut = 0;
        grabPivotRest = 1700;
        grabPivotGrab = 600;
        grabPivotScore = 2500;
        grabExtendIn = 0;
        grabExtendMid = 1080;
        grabExtendOut = 2100;
        wristRest = 0.05;
        wristGrab = 0.5;
        wristParallel = 0.6;
        wristScore = 0.8;


        grabArmPosition = "rest";
        if (gamepad1.left_bumper && gamepad1.right_bumper) {
            wrist.setPosition(0.65);
            rest();
        }
        switchToAuto();
    }


    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        preTime = System.currentTimeMillis();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        // Displays info on Driver Hub:
        telemetry.addData("Speed", speed[speedIndex] * 100 + "%"); // Tells us the wheel motors' current speed
        telemetry.addData("Wrist Position", wrist.getPosition());
        telemetry.addData("grabExtend Target Position: ", grabExtend.getTargetPosition());
        telemetry.addData("pullExtend Target Position: ", pullExtend.getTargetPosition());
        telemetry.addData("grabPivot Target Position: ", grabPivot.getTargetPosition());
        telemetry.addData("pullPivot Target Position: ", pullPivot.getTargetPosition());

        telemetry.addData("grabExtend Current Position: ", grabExtend.getCurrentPosition());
        telemetry.addData("pullExtend Current Position: ", pullExtend.getCurrentPosition());
        telemetry.addData("grabPivot Current Position: ", grabPivot.getCurrentPosition());
        telemetry.addData("pullPivot Current Position: ", pullPivot.getCurrentPosition());


        // CONTROLLER 1

        if (gamepad1.y && !yLastTime /*&& System.currentTimeMillis() - preTime > 90000*/) {
            hang();
        }

        if (gamepad1.right_bumper && !rightBumperLastTime) {
            if (speedIndex != speed.length - 1) {
                speedIndex++;
            }
        }

        if (gamepad1.left_bumper && !leftBumperLastTime) {
            if (speedIndex != 0) {
                speedIndex--;
            }
        }

        if (gamepad1.dpad_up && !dpadUpLastTime) {
            wrist.setPosition(wrist.getPosition() + 0.05);
        }
        if (gamepad1.dpad_down && !dpadDownLastTime) {
            wrist.setPosition(wrist.getPosition() - 0.05);
        }
        if (gamepad1.dpad_right && !dpadRightLastTime) {
            grabPivot.setTargetPosition(grabPivot.getCurrentPosition() + 50);
        }
        if (gamepad1.dpad_left && !dpadLeftLastTime) {
            grabPivot.setTargetPosition(grabPivot.getCurrentPosition() - 50);
        }

        // Robot strafes if driver1 holds left/right trigger. Drives normally with joysticks if triggers are not pressed
        leftFront.setPower((gamepad1.left_stick_y + (gamepad1.left_trigger - gamepad1.right_trigger)) * speed[speedIndex]);
        leftBack.setPower((gamepad1.left_stick_y + (gamepad1.right_trigger - gamepad1.left_trigger)) * speed[speedIndex]);
        rightFront.setPower((gamepad1.right_stick_y + (gamepad1.right_trigger - gamepad1.left_trigger)) * speed[speedIndex]);
        rightBack.setPower((gamepad1.right_stick_y + (gamepad1.left_trigger - gamepad1.right_trigger)) * speed[speedIndex]);


        // CONTROLLER 2

        // WHEEL SPIN
        if (gamepad2.dpad_up) {
            spinny.setPower(1.0);
        } else if (gamepad2.dpad_down) {
            spinny.setPower(-1.0);
        } else {
            spinny.setPower(0);
        }

        // A - RESTING POSITIONS
        if (gamepad2.a && !aLastTime2) {
            rest();
        }

        if (gamepad2.x && !xLastTime2) {
            grab(true);
        }

        if (gamepad2.b && !bLastTime2) {
            score();
        }

        // Y - HANGING SEQUENCE
        if (gamepad2.y && !yLastTime2 /*&& System.currentTimeMillis() - preTime > 90000*/) {
            grab(false);
        }


        if (gamepad2.left_bumper) {
            switchToAuto();
            pullPivot.setTargetPosition(pullPivot.getCurrentPosition() - 150);
        }

        if (gamepad2.right_bumper) {
            switchToAuto();
            pullPivot.setTargetPosition(pullPivot.getCurrentPosition() + 150);
        }

        if (gamepad1.left_stick_button && !leftStickButtonLastTime) {
            resetPositions();
        }

        if (gamepad2.left_stick_button && !leftStickButtonLastTime2) {
            resetPositions();
        }

        if (gamepad2.right_trigger > .5) {
            switchToDriver();
        }

        if (gamepad2.left_trigger > .5) {
            switchToAuto();
        }

        if (driverMode) {
            pullExtend.setPower(gamepad2.left_stick_y);
            pullPivot.setPower(gamepad2.right_stick_y);
        }


        aLastTime = gamepad1.a;
        bLastTime = gamepad1.b;
        xLastTime = gamepad1.x;
        yLastTime = gamepad1.y;
        dpadUpLastTime = gamepad1.dpad_up;
        dpadDownLastTime = gamepad1.dpad_down;
        dpadLeftLastTime = gamepad1.dpad_left;
        dpadRightLastTime = gamepad1.dpad_right;
        leftBumperLastTime = gamepad1.left_bumper;
        rightBumperLastTime = gamepad1.right_bumper;
        leftStickButtonLastTime = gamepad1.left_stick_button;
        rightStickButtonLastTime = gamepad1.right_stick_button;

        aLastTime2 = gamepad2.a;
        bLastTime2 = gamepad2.b;
        xLastTime2 = gamepad2.x;
        yLastTime2 = gamepad2.y;
        dpadUpLastTime2 = gamepad2.dpad_up;
        dpadDownLastTime2 = gamepad2.dpad_down;
        dpadLeftLastTime2 = gamepad2.dpad_left;
        dpadRightLastTime2 = gamepad2.dpad_right;
        leftBumperLastTime2 = gamepad2.left_bumper;
        rightBumperLastTime2 = gamepad2.right_bumper;
        leftStickButtonLastTime2 = gamepad2.left_stick_button;
        rightStickButtonLastTime2 = gamepad2.right_stick_button;
    }


    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

    }

    public void switchToAuto() {
        pullPivot.setTargetPosition(pullPivot.getCurrentPosition());
        grabPivot.setTargetPosition(grabPivot.getCurrentPosition());
        pullExtend.setTargetPosition(pullExtend.getCurrentPosition());
        grabExtend.setTargetPosition(grabExtend.getCurrentPosition());

        pullPivot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        grabPivot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        pullExtend.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        grabExtend.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        pullExtend.setPower(1.0);
        grabExtend.setPower(1.0);
        pullPivot.setPower(1.0);
        grabPivot.setPower(1.0);

        driverMode = false;
    }

    public void switchToDriver() {
        pullPivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        pullExtend.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        pullPivot.setPower(0);
        pullPivot.setPower(0);

        driverMode = true;
    }

    private void resetPositions() {
        grabExtend.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        pullExtend.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        grabPivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        pullPivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        grabExtend.setTargetPosition(grabExtend.getCurrentPosition());
        pullExtend.setTargetPosition(grabExtend.getCurrentPosition());
        grabPivot.setTargetPosition(grabExtend.getCurrentPosition());
        pullPivot.setTargetPosition(grabExtend.getCurrentPosition());

        grabExtend.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        pullExtend.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        grabPivot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        pullPivot.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        grabPivot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        grabExtend.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        pullPivot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        pullExtend.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void hang() {
        switchToAuto();
        pullExtend.setTargetPosition(12000);
        waitForMotors();
        pullPivot.setTargetPosition(6000);
    }

    public void rest() {
        switchToAuto();

        grabPivot.setPower(0.5);
        grabExtend.setPower(0.5);

        if (grabArmPosition.equals("score")) {
            grabPivot.setPower(0.2);
            grabExtend.setPower(0.5);
        }

        wrist.setPosition(wristParallel);
        pullPivot.setTargetPosition(pullPivotRest);
        pullExtend.setTargetPosition(0);
        grabPivot.setTargetPosition(grabPivotRest);
        grabExtend.setTargetPosition(grabExtendIn);

        waitForMotors();
        wrist.setPosition(wristRest);

        grabArmPosition = "rest";
    }

    public void grab(boolean isFar) {
        switchToAuto();

        if (grabArmPosition.equals("rest")) {
            grabPivot.setPower(0.6);
            grabExtend.setPower(0.6);

            wrist.setPosition(wristParallel);
            wait(.5);
            grabPivot.setTargetPosition(grabPivotGrab);
            if (isFar) {
                grabExtend.setTargetPosition(grabExtendMid);
            } else {
                grabExtend.setTargetPosition(grabExtendIn);
            }

            waitForMotors();
            wrist.setPosition(wristGrab);
        }

        if (grabArmPosition.equals("grab")) {
            grabPivot.setPower(0.6);
            grabExtend.setPower(0.6);

            grabPivot.setTargetPosition(grabPivotGrab);
            if (isFar) {
                grabExtend.setTargetPosition(grabExtendMid);
            } else {
                grabExtend.setTargetPosition(grabExtendIn);
            }
            waitForMotors();
            wrist.setPosition(wristGrab);
        }

        if (grabArmPosition.equals("score")) {
            grabPivot.setPower(0.2);
            grabExtend.setPower(0.4);

            grabPivot.setTargetPosition(grabPivotGrab);
            if (isFar) {
                grabExtend.setTargetPosition(grabExtendMid);
            } else {
                grabExtend.setTargetPosition(grabExtendIn);
            }
            waitForMotors();
            wrist.setPosition(wristGrab);
        }


        grabArmPosition = "grab";
    }

    private void score() {
        switchToAuto();
        grabPivot.setPower(0.5);    // was 0.3 before 1/17/2025
        grabExtend.setPower(1.0);   // was 0.6 before 1/17/2025

        grabExtend.setTargetPosition(200);
        wrist.setPosition(wristParallel);
        grabPivot.setTargetPosition(grabPivotScore);
        waitForMotors();
        wrist.setPosition(wristScore);
        grabExtend.setTargetPosition(grabExtendOut);

        grabArmPosition = "score";
    }

    public void wait(double time) {
        double initTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - initTime < time * 1000) {
            // Update motor power for the drivetrain based on gamepad inputs
            leftFront.setPower((gamepad1.left_stick_y + (gamepad1.left_trigger - gamepad1.right_trigger)) * speed[speedIndex]);
            leftBack.setPower((gamepad1.left_stick_y + (gamepad1.right_trigger - gamepad1.left_trigger)) * speed[speedIndex]);
            rightFront.setPower((gamepad1.right_stick_y + (gamepad1.right_trigger - gamepad1.left_trigger)) * speed[speedIndex]);
            rightBack.setPower((gamepad1.right_stick_y + (gamepad1.left_trigger - gamepad1.right_trigger)) * speed[speedIndex]);

            // Update telemetry periodically
            telemetry.addData("Speed", speed[speedIndex] * 100 + "%");
            telemetry.addData("grabExtend Target Position", grabExtend.getTargetPosition());
            telemetry.addData("pullExtend Target Position", pullExtend.getTargetPosition());
            telemetry.addData("grabPivot Target Position", grabPivot.getTargetPosition());
            telemetry.addData("pullPivot Target Position", pullPivot.getTargetPosition());
            telemetry.addData("grabExtend Current Position", grabExtend.getCurrentPosition());
            telemetry.addData("pullExtend Current Position", pullExtend.getCurrentPosition());
            telemetry.addData("grabPivot Current Position", grabPivot.getCurrentPosition());
            telemetry.addData("pullPivot Current Position", pullPivot.getCurrentPosition());
            telemetry.update();
        }
    }

    public void waitForMotors() {
        boolean motorsBusy = true;

        while (motorsBusy) {
            // Update motor power for the drivetrain based on gamepad inputs
            leftFront.setPower((gamepad1.left_stick_y + (gamepad1.left_trigger - gamepad1.right_trigger)) * speed[speedIndex]);
            leftBack.setPower((gamepad1.left_stick_y + (gamepad1.right_trigger - gamepad1.left_trigger)) * speed[speedIndex]);
            rightFront.setPower((gamepad1.right_stick_y + (gamepad1.right_trigger - gamepad1.left_trigger)) * speed[speedIndex]);
            rightBack.setPower((gamepad1.right_stick_y + (gamepad1.left_trigger - gamepad1.right_trigger)) * speed[speedIndex]);

            // Check if all motors have reached their target positions
            motorsBusy = Math.abs(pullPivot.getCurrentPosition() - pullPivot.getTargetPosition()) > 50 ||
                    Math.abs(pullExtend.getCurrentPosition() - pullExtend.getTargetPosition()) > 50 ||
                    Math.abs(grabPivot.getCurrentPosition() - grabPivot.getTargetPosition()) > 50 ||

                    Math.abs(grabExtend.getCurrentPosition() - grabExtend.getTargetPosition()) > 50;

            // Update telemetry periodically
            telemetry.addData("Speed", speed[speedIndex] * 100 + "%");
            telemetry.addData("grabExtend Target Position", grabExtend.getTargetPosition());
            telemetry.addData("pullExtend Target Position", pullExtend.getTargetPosition());
            telemetry.addData("grabPivot Target Position", grabPivot.getTargetPosition());
            telemetry.addData("pullPivot Target Position", pullPivot.getTargetPosition());
            telemetry.addData("grabExtend Current Position", grabExtend.getCurrentPosition());
            telemetry.addData("pullExtend Current Position", pullExtend.getCurrentPosition());
            telemetry.addData("grabPivot Current Position", grabPivot.getCurrentPosition());
            telemetry.addData("pullPivot Current Position", pullPivot.getCurrentPosition());
            telemetry.update();
        }
    }
}
