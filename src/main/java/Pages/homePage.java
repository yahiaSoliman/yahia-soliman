package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class homePage {

	
	WebDriver driver;

	public homePage(WebDriver driver) {

		this.driver = driver;
	}// end Constructor
	
	
	By searchBox = By.name("q");
	By seachBtn = By.name("btnK");
	By languageBtn = By.xpath("//*[@id='SIvCob']/a");
	
	
	
	//insert value in search box
	public void insertSearchValue(String searchValue) {
		
		driver.findElement(searchBox).sendKeys(searchValue);
		
	}//end insertSearchValue
	
	//get the text in the search box
	public String  getSearchValue() {
		
		return driver.findElement(searchBox).getAttribute("value");
		
	}//end getSearchValue
	
	
	//click on search button
	public void clkSearch() {
		
		driver.findElement(seachBtn).click();
		
	}//end clkSearch
	
	//click on language button
	public void clkLanguage() {
		
		driver.findElement(languageBtn).click();
		
	}//end clkLanguage
	
	
	
	
}//end homePage
