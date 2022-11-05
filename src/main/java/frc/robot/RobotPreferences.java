// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.List;

import edu.wpi.first.wpilibj.Preferences;

/**
 * RobotPreferences stores all the robot related preferences for subsystem
 * specific constants.
 */
public class RobotPreferences {
  private static String MAX_VELOCITY = "Max Velocity Meters Per Sec";
  private static String SWERVE_POD_RAMP_RATE = "Swerve Drive Ramp Rate";
  private static String MAX_ANGULAR_VELOCITY = "Max Angular Velocity Meters Per Sec";

  private static List<String> validKeys = List.of(MAX_ANGULAR_VELOCITY, MAX_VELOCITY, SWERVE_POD_RAMP_RATE);

  public static void initialize() {
    Preferences.initDouble(MAX_VELOCITY, Constants.MAX_VELOCITY_METERS_PER_SECOND);
    Preferences.initDouble(MAX_ANGULAR_VELOCITY, Constants.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND);
    Preferences.initDouble(SWERVE_POD_RAMP_RATE, Constants.GLOBAL_SWERVE_POD_RAMP_RATE);
  }

  /**
   * Removes all the keys that are not registered in the valid keys list.
   * 
   * This breaks the dashboard currently.
   */
  public static void cleanKeys() {
    for (String key : Preferences.getKeys()) {
      if (!validKeys.contains(key) && !".type".equals(key)) {
        Preferences.remove(key);
      }
    }
  }

  public static double getVelocityPreference() {
    return Preferences.getDouble(MAX_VELOCITY,
        Constants.MAX_VELOCITY_METERS_PER_SECOND);
  }

  public static double getSwervePodRampRate() {
    return Preferences.getDouble(SWERVE_POD_RAMP_RATE, Constants.GLOBAL_SWERVE_POD_RAMP_RATE);
  }

  public static double getAngularVelocityPreference() {
    return Preferences.getDouble(MAX_ANGULAR_VELOCITY,
        Constants.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND);
  }
}
