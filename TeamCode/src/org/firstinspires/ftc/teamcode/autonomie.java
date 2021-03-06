package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


@Autonomous(name = "Autonomie", group = "Autonomie")

public class autonomie extends LinearOpMode {
    // pozitie robot
    public int leftPos = 0;
    public int rightPos = 0;
    HMap robot = new HMap();

    @Override
    public void runOpMode() {

        // initializare hardware map robot
        robot.init(hardwareMap);

        waitForStart();

        telemetry.update();
    }

    public void set_robot_speed(double speed) {
        robot.back_left.setPower(speed);
        robot.back_right.setPower(speed);
        robot.front_left.setPower(speed);
        robot.front_right.setPower(speed);
    }

    public void set_robot_direction(String direction) {
        /* TODO: adaugat directii noi (https://gm0.org/en/latest/_images/mecanum-drive-directions.png) */
        switch (direction) {
            default:
                return;
            case "forward": {
                robot.front_right.setDirection(DcMotor.Direction.REVERSE);
                robot.back_right.setDirection(DcMotor.Direction.REVERSE);
                robot.back_left.setDirection(DcMotor.Direction.FORWARD);
                robot.front_left.setDirection(DcMotor.Direction.FORWARD);
            }
            case "backward": {
                robot.front_right.setDirection(DcMotor.Direction.FORWARD);
                robot.back_right.setDirection(DcMotor.Direction.FORWARD);
                robot.back_left.setDirection(DcMotor.Direction.REVERSE);
                robot.front_left.setDirection(DcMotor.Direction.REVERSE);
            }
            case "right": {
                robot.front_left.setDirection(DcMotor.Direction.FORWARD);
                robot.back_left.setDirection(DcMotor.Direction.REVERSE);
                robot.back_right.setDirection(DcMotor.Direction.REVERSE);
                robot.front_right.setDirection(DcMotor.Direction.FORWARD);
            }
            case "left": {
                robot.front_left.setDirection(DcMotor.Direction.REVERSE);
                robot.back_left.setDirection(DcMotor.Direction.FORWARD);
                robot.back_right.setDirection(DcMotor.Direction.FORWARD);
                robot.front_right.setDirection(DcMotor.Direction.REVERSE);
            }
        }
    }

    public void drive(String direction, int leftTarget, int rightTarget, double speed) {
        leftPos += leftTarget;
        rightPos += rightTarget;

        set_robot_direction(direction);

        robot.front_left.setTargetPosition(leftPos);
        robot.back_left.setTargetPosition(leftPos);
        robot.back_right.setTargetPosition(rightPos);
        robot.front_right.setTargetPosition(rightPos);

        robot.front_left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.back_left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.back_right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.front_right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        set_robot_speed(speed);

        while (opModeIsActive() && robot.front_left.isBusy() && robot.back_left.isBusy() && robot.back_right.isBusy() && robot.front_right.isBusy()) {
            idle();
        }

    }
}