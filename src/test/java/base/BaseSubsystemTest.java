// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package base;

import static org.junit.Assert.assertEquals;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.Constants;
import frc.robot.subsystems.Base;
import org.junit.Before;
import org.junit.Test;

/** Add your docs here. */
public class BaseSubsystemTest {
  Base base;

  @Before
  public void setup() {
    assert HAL.initialize(500, 0);

    base = new Base();
  }

  @Test
  public void commandPods() {
    ChassisSpeeds speeds = new ChassisSpeeds(1 * Constants.MAX_VELOCITY_METERS_PER_SECOND,
        1 * Constants.MAX_VELOCITY_METERS_PER_SECOND, 0);

    base.drive(speeds);

    base.periodic();

    SwerveModuleState[] states = base.getModuleStates();

    assertEquals(speeds.vyMetersPerSecond, states[0].speedMetersPerSecond, 0);
    assertEquals(speeds.vyMetersPerSecond, states[1].speedMetersPerSecond, 0);
    assertEquals(speeds.vyMetersPerSecond, states[2].speedMetersPerSecond, 0);
    assertEquals(speeds.vyMetersPerSecond, states[3].speedMetersPerSecond, 0);
  }
}