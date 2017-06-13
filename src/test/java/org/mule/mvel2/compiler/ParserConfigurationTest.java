package org.mule.mvel2.compiler;

import java.util.*;

import junit.framework.TestCase;

import org.mule.mvel2.MVEL;
import org.mule.mvel2.ParserConfiguration;
import org.mule.mvel2.ParserContext;
import org.mule.mvel2.PropertyAccessException;

/**
 * Asserts that the compiler respects the parser configuration.
 */
public class ParserConfigurationTest extends TestCase {

  /**
   * If the expression to compile involves a static method and the parser configuration
   * allows the verification that the expression should be attempted to resolve to 
   * an static class as target, it should be resolved successfully.
   */
  public final void testWhenTryStaticAccessAndStaticMethodThenResolveStaticMethod() {
      ParserConfiguration parserConfiguration = new ParserConfiguration();
      parserConfiguration.setTryStaticAccess(true);
      ParserContext context = new ParserContext(parserConfiguration);

      final CompiledExpression compiledExpression = new ExpressionCompiler("java.lang.Integer.parseInt(\"3\")")
              .compile(context);

      final Object val = MVEL.executeExpression(compiledExpression);
      assertEquals(val, 3);  
  }

  /**
   * If the expression to compile involves a static method and the parser configuration
   * does NOT allow the verification that the expression should be attempted to resolve to 
   * an static class as target, the test should raise an exception.
   */
  public final void testWhenTryStaticAccessAndStaticMethodThenRaiseException() {
      try {
          ParserConfiguration parserConfiguration = new ParserConfiguration();
          parserConfiguration.setTryStaticAccess(false);
          ParserContext context = new ParserContext(parserConfiguration);

          final CompiledExpression compiledExpression = new ExpressionCompiler("java.lang.Integer.parseInt(\"3\")")
              .compile(context);
      
          MVEL.executeExpression(compiledExpression);
          fail("Exception expected");
      } catch (PropertyAccessException e) {
          assertEquals(e.getClass(), PropertyAccessException.class);
      }
  }

}
