package Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


import Pages.homePage;
import Pages.resultsPage;

public class TestMethods {
	
	WebDriver driver;
	WebDriverWait wait;
	homePage homePge;
	resultsPage resultsPage;
	
	String searchValue = "money";
	String searchValue2 = "water";
	
	@BeforeMethod
	public void setup() {
		
		System.setProperty("webdriver.chrome.driver","C:\\Users\\Yehya\\eclipse-workspace\\instabugProject\\chromedriver.exe");
		
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		wait = new WebDriverWait(driver,30);
		
		driver.manage().window().maximize();
		driver.get("https://www.google.com");
		
		homePge = new homePage(driver);
		resultsPage = new resultsPage(driver);
	}//end setup
	
	@AfterMethod
	public void After() {
		driver.quit();
	}//end After
	
	
	//verify if the website displays suggestions for the inserted search value
	@Test(priority = 1)
	public void displaySuggestions() throws InterruptedException {
		
		homePge.clkLanguage();
		homePge.insertSearchValue(searchValue);
		
		try {
			
		  //wait until the suggestions box gets displayed	
		  wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("aajZCb")));
		}//end try
		
		catch(TimeoutException e) {
			
			fail("the suggestions box has not been displayed");
		}//end catch 
		
		
	}//end displaySuggestions
	
	
	//verify that the website redirects to the page of the search results and
	//displays results related to the search value
	@Test(priority = 2)
	public void relatedResults() {
		
		
		driver.get("https://www.google.com");
		homePge.clkLanguage();
		homePge.insertSearchValue(searchValue);
		homePge.clkSearch();

		try {
			
		//verify that the website redirects to the page of the search results
		wait.until(ExpectedConditions.titleContains(searchValue));
		
		}//end try
		
		catch(TimeoutException e) {
			
			fail("the website didn't redirect to the results page");
			
		}//end catch
		
		//get all the web elements that represents the search results link titles
		List<WebElement> list =  driver.findElements(By.xpath("//h3[@class='LC20lb DKV0Md']"));
		
		// go through all the results to check if they are related to the search inserted text
		loopList(list, searchValue);
		 
	}//end relatedResults
	
	//Verify that all suggestions are related to the inserted value
	@Test(priority = 3)
	public void relatedSuggestions() {
		
		driver.get("https://www.google.com");
		homePge.clkLanguage();
		homePge.insertSearchValue(searchValue);
		
		List<WebElement> list = driver.findElements(By.xpath("//*[@class='G43f7e']/li"));
		
		
		loopList(list, searchValue);
	}//end relatedSuggestions
	
	
	//verify that user can search for a suggestions
	@Test(priority = 4)
	public void selectSuggestion() {
		
		driver.get("https://www.google.com");
		homePge.clkLanguage();
		homePge.insertSearchValue(searchValue);
		
		//get the text of the 4th suggestion
		String suggestionValue = driver.findElement(By.xpath("(//*[@class='G43f7e']/li)[3]")).getText();
		System.out.println(suggestionValue);
		String[] splittedSuggestionValue = suggestionValue.toLowerCase().split(" ", 3);
		
		//select the 4th suggestion
		driver.findElement(By.xpath("(//*[@class='G43f7e']/li)[3]")).click();
		
		//get all the web elements that represents the search results link titles
		List<WebElement> list =  driver.findElements(By.xpath("//h3[@class='LC20lb DKV0Md']"));
		
		// go through all the results to check if they are related to the search inserted text
		for(WebElement e: list) {
			
			//some web elements doesn't represent search result so we filter them by eliminating
			//any element that has no text
			
			if(e.getText() != "") 
			//convert the text to lower case characters to avoid the case sensitivity 
			//of method contains
			assertTrue(e.getText().toLowerCase().contains(splittedSuggestionValue[0]) ||
					e.getText().toLowerCase().contains(splittedSuggestionValue[1]) ||
					e.getText().toLowerCase().contains(splittedSuggestionValue[2]));
			
		}//end for
	}//end selectSuggestion
	
	
	//verify that the search box display X button after insertion of a search text 
	//verify that the search box is cleared by the X button
	@Test(priority = 5)
	public void clearOption(){
		
		driver.get("https://www.google.com");
		homePge.clkLanguage();
		homePge.insertSearchValue(searchValue);
		
		try {
			
			  //wait until the X button gets displayed	
			  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='ExCKkf z1asCe rzyADb']")));
			}//end try
			
			catch(TimeoutException e) {
				
				fail("the X button has not been displayed");
			}//end catch 
		
		driver.findElement(By.xpath("//*[@class='ExCKkf z1asCe rzyADb']")).click();
		assertEquals(homePge.getSearchValue(),"","the search box is not cleared by X button");;
		
	}//end clearOption
	
	//verify that the user can make new search through the result page
	@Test(priority = 6)
	public void resultPageSearch(){
		
		driver.get("https://www.google.com");
		homePge.clkLanguage();
		homePge.insertSearchValue(searchValue);
		homePge.clkSearch();
		resultsPage.clearSearchBox();
		resultsPage.insertSearchValue(searchValue2);
		resultsPage.clkMagnifyBtn();
		//get all the web elements that represents the search results link titles
		List<WebElement> list =  driver.findElements(By.xpath("//h3[@class='LC20lb DKV0Md']"));
		
		//assert that all result elements include/related to the search value
		loopList(list, searchValue2);
	}//end resultPageSearch
	
	//verify that the user can search for values that contains numbers, special characters or too long
	@Test(priority = 7, dataProvider = "dataProvider")
	public void differnetSeachData(String providerSearchValue){
		
		driver.get("https://www.google.com");
		homePge.clkLanguage();
		homePge.insertSearchValue(providerSearchValue);
		homePge.clkSearch();

		try {
			
		//verify that the website redirects to the page of the search results
		wait.until(ExpectedConditions.titleContains(providerSearchValue));
		
		}//end try
		
		catch(TimeoutException e) {
			
			fail("the website didn't redirect to the results page");
			
		}//end catch
		
		driver.quit();
	}//end differnetSeachData
	
	//Loop through a list of web elements
	private void loopList(List<WebElement> list, String key) {
		
		for(WebElement e: list) {
			
			//some web elements doesn't represent search result so we filter them by eliminating
			//any element that has no text
			if(e.getText() != "")
			//convert the text to lower case characters to avoid the case sensitivity 
			//of method contains	
			assertTrue(e.getText().toLowerCase().contains(key));
			
		}//end for
	}//end loopList
	
	@DataProvider
	private String[] dataProvider(){
	String[] searchValues = new String[3]; //eight rows and two columns
	
	searchValues[0] = "2020";
	searchValues[1] = "20$";
	searchValues[2] = "there is a time when the operation of the machine become so odious";

	return searchValues;
	}

	
}//end class testMethod
