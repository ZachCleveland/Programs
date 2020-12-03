package edu.nmsu.cs.circles;

/***
 * Example JUnit testing class for Circle1 (and Circle)
 *
 * - must have your classpath set to include the JUnit jarfiles - to run the test do: java
 * org.junit.runner.JUnitCore Circle1Test - note that the commented out main is another way to run
 * tests - note that normally you would not have print statements in a JUnit testing class; they are
 * here just so you see what is happening. You should not have them in your test cases.
 ***/

import org.junit.*;

public class Circle2Test
{
	// Data you need for each test case
	private Circle2 circle2;

	//
	// Stuff you want to do before each test case
	//
	@Before
	public void setup()
	{
		System.out.println("\nTest starting...");
		circle2 = new Circle2(1, 2, 3);
	}

	//
	// Stuff you want to do after each test case
	//
	@After
	public void teardown()
	{
		System.out.println("\nTest finished.");
	}

	//
	// Test a simple positive move
	//
	@Test
	public void simpleMove()
	{
		Point p;
		System.out.println("Running test simpleMove.");
		p = circle2.moveBy(1, 1);
		Assert.assertTrue(p.x == 2 && p.y == 3);
	}

	//
	// Test a simple negative move
	//
	@Test
	public void simpleMoveNeg()
	{
		Point p;
		System.out.println("Running test simpleMoveNeg.");
		p = circle2.moveBy(-1, -1);
		Assert.assertTrue(p.x == 0 && p.y == 1);
	}
	
	//
	// Test scale up
	//
	@Test
	public void simpleScaleUp()
	{
		System.out.println("Running test simpleScaleUp.");
		circle2.scale(2.0);
		Assert.assertTrue(circle2.radius == 6);
	}


	//
	// Test scale down
	//
	@Test
	public void simpleScaleDown()
	{
		System.out.println("Running test simpleScaleUp.");
		circle2.scale(.5);
		Assert.assertTrue(circle2.radius == 1.5);
	}



	//
	// Test intersects
	//
	@Test
	public void simpleTouching()
	{
		//very close circle
		Circle2 circle2_1; 
		circle2_1 = new Circle2(3, 2, 3);
		System.out.println("Running test simpleTouching.");
		Assert.assertTrue(circle2.intersects(circle2_1));

	}

	
	
	//
	// Test out of bounds intersect
	//
	@Test
	public void notTouching()
	{	
		Circle2 circle2_1; 
		//circle out of bounds, by y axis
		circle2_1 = new Circle2(7, 2, 3);
		Assert.assertTrue(!circle2.intersects(circle2_1));

		//circle out of bounds, by x axis
		circle2_1 = new Circle2(3, 5, 3);
		Assert.assertTrue(!circle2.intersects(circle2_1));
	}
	
	/***
	 * NOT USED public static void main(String args[]) { try { org.junit.runner.JUnitCore.runClasses(
	 * java.lang.Class.forName("Circle1Test")); } catch (Exception e) { System.out.println("Exception:
	 * " + e); } }
	 ***/

}
