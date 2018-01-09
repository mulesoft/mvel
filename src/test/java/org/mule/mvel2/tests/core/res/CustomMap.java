package org.mule.mvel2.tests.core.res;

import org.mule.mvel2.optimizers.AccessorOptimizer;

/**
 * Represents an object that has the same {@link CustomMap#remove(Object)} method signature that {@link java.util.Map}. This class
 * is useful for testing chained calls where two (or more) objects in the chain possess the same method signature but doesn't
 * implement the same interface. In that way is possible to verify that the return type of the {@link AccessorOptimizer} is
 * updated correctly and avoid reflection errors.
 */
public class CustomMap {

  private int removeInvocations = 0;

  public void remove(Object key) {
    removeInvocations++;
  }

  public int getRemoveInvocations() {
    return removeInvocations;
  }
}
