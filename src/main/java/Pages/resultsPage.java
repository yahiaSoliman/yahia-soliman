package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class resultsPage {

	WebDriver driver;

	public resultsPage(WebDriver driver) {

		this.driver = driver;
	}// end Constructor
	
	
	By searchBox = By.name("q");
	By seachBtn = By.name("btnK");
	By magnifyBtn = By.xpath("//*[@class='z1asCe MZy1Rb']");
	
	
	
	//insert value in search box
	public void insertSearchValue(String searchValue) {
		
		driver.findElement(searchBox).sendKeys(searchValue);
		
	}//end insertSearchValue
	
	//click on search button
	public void clkMagnifyBtn() {
		
		driver.findElement(magnifyBtn).click();
		
	}//end clkMagnifyBtn
	
	//clear search box
	public void clearSearchBox() {
		driver.findElement(searchBox).clear();
	}//end clearSearchBox
	
}//end resultsPage
