// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package base;

import static org.junit.Assert.assertEquals;

import java.util.List;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.Constants;
import frc.robot.subsystems.Base;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/** Add your docs here. */
public class BaseSubsystemTest {
  static Base base;

  @BeforeClass
  public static void setup() {
    assert HAL.initialize(500, 0);

    BaseSubsystemTest.base = new Base();
  }

  @Test
  public void testSpeed() {
    ChassisSpeeds speeds = new ChassisSpeeds(1 * Constants.MAX_VELOCITY_METERS_PER_SECOND,
        1 * Constants.MAX_VELOCITY_METERS_PER_SECOND, 0);

    base.drive(speeds);

    base.periodic();

    SwerveModuleState[] states = base.getModuleStates();

    for (SwerveModuleState state : states) {
      assertEquals(speeds.vyMetersPerSecond, state.speedMetersPerSecond, 0);
    }
  }

  @Test
  public void testAngle() {
    ChassisSpeeds speeds = new ChassisSpeeds(0,
        1 * Constants.MAX_VELOCITY_METERS_PER_SECOND, 0);

    base.drive(speeds);

    base.periodic();

    SwerveModuleState[] states = base.getModuleStates();

    for (SwerveModuleState state : states) {
      assertEquals(90, state.angle.getDegrees(), 0);
    }
  }
}
