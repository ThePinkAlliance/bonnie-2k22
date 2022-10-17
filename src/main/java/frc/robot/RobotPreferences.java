// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Preferences;

/**
 * RobotPreferences stores all the robot related prefrences for subsystem
 * specific constants.
 */
public class RobotPreferences {
  private static String MAX_VELOCITY = "Max Velocity m/s";
  private static String SWERVE_POD_RAMP_RATE = "Swerve Drive Ramp Rate";
  private static String MAX_ANGULAR_VELOCITY = "Max Angular Velocity m/s";

  public static void initialize() {
    Preferences.initDouble(MAX_VELOCITY, Constants.MAX_VELOCITY_METERS_PER_SECOND);
    Preferences.initDouble(SWERVE_POD_RAMP_RATE, Constants.GLOBAL_SWERVE_POD_RAMP_RATE);
  }

  public static double getVelocityPreference() {
    return Preferences.getDouble(MAX_VELOCITY,
        Constants.MAX_VELOCITY_METERS_PER_SECOND);
  }

  public static double getAngularVelocityPreference() {
    return Preferences.getDouble(MAX_ANGULAR_VELOCITY,
        Constants.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND);
  }
}
