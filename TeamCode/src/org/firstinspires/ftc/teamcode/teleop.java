package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@TeleOp(name = "TeleOP", group = "TeleOP")

/*
CONTROALE:
---------
Gamepad 1:
* left stick: miscare robot in XOY
* right stick: rotire robot
* up: marire viteza
* down: micsorare viteza

Gamepad 2:
* Left trigger: da afara din cabina cu 1/7 din cat apesi
* right trigger: baga in cabina cu 100% din putere
* up: brat sus
* down: brat jos
* buton a: rotire roata carusel
 */


public class teleop extends LinearOpMode {

    public double horizontal = 0,
            vertical = 0,
            pivot = 0,
            speed_percent = 100,
            arm_pos = 0,
            carousel_active = 0;

    BNO055IMU imu; // inertial measurment unit
    Orientation angles;

    @Override
    public void runOpMode() {
        HMap robot = new HMap();
        robot.init(hardwareMap);

        vertical = -gamepad1.left_stick_y;
        horizontal = gamepad1.left_stick_x;
        pivot = gamepad1.right_stick_x;

        robot.back_right.setDirection(DcMotorSimple.Direction.REVERSE);
        robot.front_right.setDirection(DcMotorSimple.Direction.REVERSE);

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;

        imu = hardwareMap.get(BNO055IMU.class, "imu");

        imu.initialize(parameters);

        waitForStart();

        while (opModeIsActive()) {

            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

            robot.front_right.setPower(((pivot + (vertical + horizontal)) * speed_percent) / 100);
            robot.front_left.setPower(((pivot + (vertical - horizontal)) * speed_percent) / 100);
            robot.back_left.setPower(((pivot + (vertical - horizontal)) * speed_percent) / 100);
            robot.back_right.setPower(((pivot + (vertical + horizontal)) * speed_percent) / 100);

            robot.Arm.setPower(0);
            robot.ramp.setPower(0);

            carousel_active = gamepad2.a ? 1 : 0;

            robot.carousel.setPower(carousel_active / 4);

            while (gamepad1.dpad_up && speed_percent <= 90)
                    speed_percent += 10;

            while (gamepad1.dpad_down && speed_percent >= 20)
                    speed_percent -= 10;

            while (gamepad2.right_trigger > 0) {
                robot.ramp.setDirection(DcMotorSimple.Direction.FORWARD);
                robot.ramp.setPower(gamepad2.right_trigger);
            }

            while (gamepad2.left_trigger > 0) {
                robot.ramp.setDirection(DcMotorSimple.Direction.REVERSE);
                robot.ramp.setPower(gamepad2.left_trigger / 7);
            }

            while (gamepad2.dpad_up) {
                robot.Arm.setDirection(DcMotorSimple.Direction.FORWARD);
                robot.Arm.setPower(0.5);
                arm_pos++;
            }

            while (gamepad2.dpad_down) {
                robot.Arm.setDirection(DcMotorSimple.Direction.REVERSE);
                robot.Arm.setPower(0.5);
                arm_pos--;
            }

            telemetry.addData("Robot speed percent: ", speed_percent);
            telemetry.addData("Arm position: ", arm_pos);
            telemetry.addData("Robot heading", angles.firstAngle); /*SPER SA MEARGA*/

            telemetry.update();
        }

    }
}