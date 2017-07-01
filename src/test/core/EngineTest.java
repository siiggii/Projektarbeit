package core;

//import org.junit.Test;
//import org.junit.*;


import org.junit.Assert;
import org.junit.Test;

/**
 * Created by siegf on 17.06.2017.
 */
public class EngineTest {
    Engine engine = new Engine();
    @Test
    public void compareSolutions() throws Exception {
    }

    @Test
    public void getResult() throws Exception {
    }

    @Test
    public void test1() throws Exception {
        boolean solution = engine.compareSolutions("2*x*x-4*x-16=0","2*x*x-4*x-16=0");
        Assert.assertTrue(solution );
        int a = 0;
        a++;

    }
    @Test
    public void test2() throws Exception {
        boolean solution = engine.compareSolutions("2*x*x-4*x-16=0","x=4");
        Assert.assertTrue(solution );
    }
    @Test
    public void test3() throws Exception {
        boolean solution = engine.compareSolutions("2*x*x-4*x-16=0","x=-2");
        Assert.assertTrue(solution );
    }
    @Test
    public void test3p() throws Exception {
        boolean solution = engine.compareSolutions("2*x*x-4*x-16=0","x=(-4±((((-4)^2)-4*2*(-16))^(1/2)))/(2*2)");
        Assert.assertTrue(solution );
    }
    @Test
    public void test4() throws Exception {
        boolean solution = engine.compareSolutions("2*x*x-4*x-16=0","x=(-(-4)+((-4)^2-4*2*(-16))^(1/2))/(2*2)");
        Assert.assertTrue(solution );
    }

    @Test
    public void test5() throws Exception {
        boolean solution = engine.compareSolutions("x*x=4","x=2)");
        Assert.assertTrue(solution );
    }
    @Test
    public void test6() throws Exception {
        boolean solution = engine.compareSolutions("x*x+x*x=8","x=2)");
        Assert.assertTrue(solution );
    }
    @Test
    public void test7() throws Exception {
        boolean solution = engine.compareSolutions("1*x*x-7*x+3=2","x=-((3*(5^(1/2))-7)/(2))");
        Assert.assertTrue(solution );
    }
    @Test
    public void test8() throws Exception {
        boolean solution = engine.compareSolutions("1*x*x-7*x+3=2","x=((3*(5^(1/2))+7)/(2))");
        Assert.assertTrue(solution );
    }
    /*
    @Test
    public void test9() throws Exception {
        boolean solution = engine.compareSolutions("x*x*x*x-3*x*x+2=0","x=1");
        Assert.assertTrue(solution );
    }
    @Test
    public void test10() throws Exception {
        boolean solution = engine.compareSolutions("2*x*x*x*x-4*x*x-16=0","x=1");
        Assert.assertTrue(solution );
    }
    */

}