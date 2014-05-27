package cc.mycode.realtest.screen.sample;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import cc.mycode.realtest.core.Screen;

public class TutorialPage extends Screen{
	
	@FindBy(css="#lesson1")
	private WebElement titleLesson;
	
	public void assertThatTitleIs(String expectedText){
		System.out.println(titleLesson);
		assert isElementContainsText(titleLesson,expectedText);
	}
	
}
