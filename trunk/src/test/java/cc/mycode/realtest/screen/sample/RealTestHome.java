package cc.mycode.realtest.screen.sample;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import cc.mycode.realtest.core.Screen;

public class RealTestHome extends Screen{
	
	@FindBy(css="#tutorial")
	private WebElement tutorialLink;
	
	
	public RealTestHome openHome(){
		open("http://realtest.mycode.cc/home");
		return nextPage(RealTestHome.class);
	}
	
	public TutorialPage clickTutorial(){
		click(tutorialLink);
		return nextPage(TutorialPage.class);
	}
	
}
