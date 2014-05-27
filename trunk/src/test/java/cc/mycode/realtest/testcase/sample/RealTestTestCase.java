package cc.mycode.realtest.testcase.sample;
import org.testng.annotations.Test;

import cc.mycode.realtest.core.TestCase;
import cc.mycode.realtest.screen.sample.RealTestHome;


public class RealTestTestCase extends TestCase {
	
	  
	@Test(groups = { "frontend" , "outher" } )
	public void testTitleOfTutorialPage() {
		startFrom(RealTestHome.class).openHome().clickTutorial().assertThatTitleIs("1 - Instalando o TestNg no eclipse ");
	}
	
	
	
}
