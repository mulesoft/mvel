package org.mvel2.tests.perftests;

import static org.mvel2.tests.perftests.utils.EchoFunction.ECHO;
import org.mule.mvel2.ParserContext;
import org.mule.mvel2.ast.Function;
import org.mule.mvel2.compiler.CompiledExpression;
import org.mule.mvel2.compiler.ExpressionCompiler;
import org.mule.mvel2.integration.impl.ClassImportResolverFactory;
import org.mule.mvel2.integration.impl.DefaultLocalVariableResolverFactory;
import org.mule.mvel2.util.CompilerTools;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.TestCase;
import org.mvel2.tests.perftests.utils.EchoFunction;

public class NewObjectExpressionConcurrencyTest extends TestCase {

	private static int NUMBER_OF_THREADS= 1000;
	private static String FUNCTION_DEFINITION = "def echo(echoMessage) {return new EchoFunction().echo(echoMessage);}";

	private final CountDownLatch startLatch = new CountDownLatch(1);
	private final CountDownLatch latch = new CountDownLatch(NUMBER_OF_THREADS);
	private final Throwable[] exception = {null};
	private final ParserContext context = new ParserContext();
	private final Object [] functionParams = new String [] {"hello"};
	private  Function echoFunction;
	private CompiledExpression compilerExpression;
	private Map<String, Function> functionMap;
	private final AtomicInteger correctResults = new AtomicInteger(0);


	@Override
	public void setUp() throws Exception {
		compilerExpression = new ExpressionCompiler(FUNCTION_DEFINITION).compile();
		functionMap =  CompilerTools.extractAllDeclaredFunctions(compilerExpression);
		context.addImport(EchoFunction.class);
		echoFunction = functionMap.get("echo");
	}

	public void testConcurrentNewObjectExpression() throws InterruptedException {
		for (int i = 0; i < NUMBER_OF_THREADS; i ++) {
			Thread thread = new Thread(new Runnable() {
				public void run() {
					try {
						startLatch.await();
						Object result =  echoFunction.call(context, null, new ClassImportResolverFactory(context.getParserConfiguration(), new DefaultLocalVariableResolverFactory(), true), functionParams);
						if (result.equals(ECHO + functionParams[0])){
							correctResults.incrementAndGet();
						}
					}
					catch (Throwable t) {
						exception[0] = t;
					}
					finally {
						latch.countDown();
					}
				}
			});
			thread.start();
		}
		startLatch.countDown();
		latch.await();
		assertNull("Exception should be null but it was " + exception[0], exception[0]);
		assertTrue("Incorrect results were found", correctResults.get() == NUMBER_OF_THREADS);
	}

}
