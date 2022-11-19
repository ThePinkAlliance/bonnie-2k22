// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import frc.robot.Constants;
import frc.robot.RobotPreferences;
// import io.github.oblarg.oblog.Loggable;
// import io.github.oblarg.oblog.annotations.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Supplier;

import com.ThePinkAlliance.core.drivetrain.swerve.SwerveBase;

public class Base extends SwerveBase {

  public static final double DRIVE_WHEEL_CIRCUMFERENCE = 12.875;

  SwerveDrivePoseEstimator estimator;
  Pose2d currentPose2d = new Pose2d();

  /** Creates a new Base. */
  public Base() {
    super(Constants.DRIVETRAIN_TRACKWIDTH_METERS, Constants.DRIVETRAIN_WHEELBASE_METERS, "debug");

    this.configureMk4(Constants.gearRatio, Constants.frontLeftConfig, Constants.frontRightConfig,
        Constants.backRightConfig, Constants.backLeftConfig);

    /*
     * A SwerveDrivePoseEstimator is similar to the classic SwerveDriveOdometry
     * class however the estimator also includes a Kalman Filter to process all the
     * inputs being received from sensors.
     */
    this.estimator = new SwerveDrivePoseEstimator(new Rotation2d(), new Pose2d(), kinematics,
        VecBuilder.fill(0.05, 0.05, Units.degreesToRadians(
            5)),
        VecBuilder.fill(Units.degreesToRadians(0.01)),
        VecBuilder.fill(0.5, 0.5, Units.degreesToRadians(30)));
  }

  @Override
  public void setStates(SwerveModuleState... states) {
    estimator.update(getRotation(), states);

    this.currentPose2d = estimator.getEstimatedPosition();

    SwerveDriveKinematics.desaturateWheelSpeeds(
        states,
        RobotPreferences.getVelocityPreference());

    frontLeftModule.set(
        convertModuleSpeed(states[0].speedMetersPerSecond),
        states[0].angle.getRadians());

    frontRightModule.set(
        convertModuleSpeed(states[1].speedMetersPerSecond),
        states[1].angle.getRadians());

    backLeftModule.set(
        convertModuleSpeed(states[2].speedMetersPerSecond),
        states[2].angle.getRadians());

    backRightModule.set(
        convertModuleSpeed(states[3].speedMetersPerSecond),
        states[3].angle.getRadians());
  }

  /**
   * Converts module speed in meters per second to desired motor voltage.
   *
   * @param speedMetersPerSecond
   */
  public double convertModuleSpeed(double speedMetersPerSecond) {
    return ((speedMetersPerSecond / RobotPreferences.getVelocityPreference()) *
        Constants.MAX_VOLTAGE);
  }

  /**
   * Returns if the robot inverted.
   */
  public boolean isInverted() {
    return (getRotation().getDegrees() <= 190 &&
        getRotation().getDegrees() > 90 ||
        getRotation().getDegrees() >= 290 &&
            getRotation().getDegrees() < 90);
  }

  /**
   * Calculates the desired angle relative to the robot's heading.
   * 
   * @param desiredAngle The desired rotation in degress
   * @return The angle in degress the drivetrain needs to move.
   */
  public Rotation2d calculateDesiredAngle(Rotation2d desiredAngle) {
    return new Rotation2d(this.getSensorYaw() - desiredAngle.getDegrees());
  }

  /**
   * Returns the current direction of the robot.
   */
  public double getDirection() {
    if (isInverted()) {
      return 1.0;
    }

    return -1.0;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    SwerveModuleState[] states = kinematics.toSwerveModuleStates(chassisSpeeds);
    SwerveDriveKinematics.desaturateWheelSpeeds(states, RobotPreferences.getVelocityPreference());

    ArrayList<SwerveModuleState> newStates = new ArrayList<>(Arrays.asList(states));
    ArrayList<SwerveModuleState> currentStates = new ArrayList<>(Arrays.asList(this.states));

    boolean update = !newStates.containsAll(currentStates);

    if (update) {
      setStates(states);

      this.states = states;
    }
  }

  @Override
  public void resetOdometry(Pose2d pose) {
    estimator.resetPosition(pose, getRotation());
  }

  @Override
  public double getSensorYaw() {
    return gyro.getYaw();
  }

  @Override
  public Pose2d getPose() {
    return estimator.getEstimatedPosition();
  }

  @Override
  public Supplier<Pose2d> getPoseSupplier() {
    return () -> estimator.getEstimatedPosition();
  }

  @Override
  public void resetOdometry(Pose2d pose, Rotation2d rot) {
    estimator.resetPosition(pose, rot);
  }
}
