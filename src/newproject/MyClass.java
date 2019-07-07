package newproject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.firefox.FirefoxDriver;
public class MyClass {
	
	static {
		System.setProperty("webdriver.gecko.driver", "C:\\Selenium\\geckodriver.exe");
		}

	final static WebDriver driver = new FirefoxDriver();
	final static int waitTimeValueInSeconds = 5; // general timeout in seconds
	final static WebDriverWait wait = new WebDriverWait(driver, waitTimeValueInSeconds);
    
	public static void main(String[] args) {
    	System.out.println("INFO: lets roll out this test set");
    	
    	//testSubcategoryNames();
    	shoppingSmoke();

    }
    


	public static void testSubcategoryNames() {
		/* testSubcategoryNames
			this test is supposed to check provided Category from main menu, open it 
			and verify there is predefined list of subcategories listed:
			for manual verification: 
			1. open browser and navigate to https://www.alza.cz/ - page should load correctly
			2. verify left menu contains "Komponenty" category - "Komponenty" should be present in the menu
			3. click on "Komponenty" category - "Komponenty category should load correcty"
			4. verify the list of presented subcategories in the upper part of the page
				4a. there should be list of the following:
				"Intel Optane", "Procesory", "GrafickÈ karty", "Disky", "PamÏti", "Z·kladnÌ desky",
				"Sk¯ÌnÏ a zdroje", "ChlazenÌ", "Overclocking", "OptickÈ mechaniky", "TeleviznÌ karty",
				"ZvukovÈ karty", "SÌùovÈ karty", "ÿadiËe a adaptÈry", "ProgramovatelnÈ stavebnice",
				"Tuning PC", "Z·znamov· za¯ÌzenÌ", "Kabely a konektory", "PC na mÌru", "Mining", 
				"Jak sestavit PC", "ProË nakupovat u n·s", "HW novinky a recenze"
			5. verify individual subcategories are present and that there is none missing, none extra
		*/
		
		// test data, the Key is name of the category, the values are the subcategory names in the category page
		Map <String, List<String>> mapOfCategories = new HashMap<>();
		mapOfCategories.put("Komponenty", new ArrayList<>(Arrays.asList(
				"Intel Optane", "Procesory", "GrafickÈ karty", "Disky", "PamÏti", "Z·kladnÌ desky",
				"Sk¯ÌnÏ a zdroje", "ChlazenÌ", "Overclocking", "OptickÈ mechaniky", "TeleviznÌ karty",
				"ZvukovÈ karty", "SÌùovÈ karty", "ÿadiËe a adaptÈry", "ProgramovatelnÈ stavebnice",
				"Tuning PC", "Z·znamov· za¯ÌzenÌ", "Kabely a konektory", "PC na mÌru", "Mining", 
				"Jak sestavit PC", "ProË nakupovat u n·s", "HW novinky a recenze")));
		mapOfCategories.put("Notebooky", new ArrayList<>(Arrays.asList("Notebooky za vysvÏdËenÌ", "BÏûnÈ uûitÌ", "HernÌ", "this is not there")));
			
		try {
			
			for (String category : mapOfCategories.keySet())  {
				driver.get("https://www.alza.cz/");
				wait.until(ExpectedConditions.titleIs("Alza.cz - nejvÏtöÌ obchod s poËÌtaËi a elektronikou | Alza.cz"));
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("fmenu")));
				Integer amountOfTestedSubcategories = mapOfCategories.get(category).size();
				System.out.println("INFO: About to check Category [" + category + "] for subcategories");
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(category)));
				WebElement menuCategory = driver.findElement(By.linkText(category));
				menuCategory.click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(("h1"))));
				WebElement h1tag = driver.findElement(By.tagName(("h1")));
				
				// checks whether its on the correct page
				if ((h1tag.getText()).equals(category)){
					System.out.println("INFO: Category [" + category + "] page is loaded.");
				} else {
					throw new Exception("ERROR: [" + category + "] was not in H1, this was there instead [" +  h1tag.getText() + "]");
				}
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector((".catlist.top"))));
				WebElement mainContent = driver.findElement(By.cssSelector(".catlist.top"));
				List<WebElement> listOfSubcategoriesOnPage = mainContent.findElements(By.className("name"));
				List<String> foundSubcategoriesOnPage = new ArrayList<>(); 
				Integer amountOfFoundSubcategories = listOfSubcategoriesOnPage.size()-1; // seems like there is always +1 extra empty

				// gather the text value from "subcategory" web elements on the category page 
				for ( WebElement subcategoryOnPage : listOfSubcategoriesOnPage)  {
					foundSubcategoriesOnPage.add(subcategoryOnPage.getText());
				}

				// checks if specified category name from test list is on the actual category page 
				for (String subcategoryTextFromTest : mapOfCategories.get(category)) {
					if (foundSubcategoriesOnPage.contains(subcategoryTextFromTest)) {
						System.out.println("INFO: [" + subcategoryTextFromTest + "] subcategory is in the category list on the [" + category + "] page");

					} else {
						System.out.println("ERROR: [" + subcategoryTextFromTest + "] is in NOT in the category list on the [" + category + "] page!");
						// intentionally not throwing anything, there may be more issues
					}

				}
				// check the amounts
				if (amountOfTestedSubcategories.equals(amountOfFoundSubcategories)) {
					System.out.println("INFO: amount of tested equals amount found [" + amountOfFoundSubcategories + "].");

				} else {
					throw new Exception("ERROR: amounts differ! Tested for [" + amountOfTestedSubcategories + "], but found: [" + amountOfFoundSubcategories + "]");

				}



			}
		} catch (Exception e) {
			System.out.println("ERROR: Horrible failure!");
			e.printStackTrace();
		}
    	  	
    }
    
    public static void shoppingSmoke() {
		driver.get("https://www.alza.cz/");
		wait.until(ExpectedConditions.titleIs("Alza.cz - nejvÏtöÌ obchod s poËÌtaËi a elektronikou | Alza.cz"));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("fmenu")));
		WebElement mainMenu = driver.findElement(By.className("fmenu"));
		List<WebElement> menuCategories = mainMenu.findElements(By.className("bx"));
		//System.out.println(menuCategories.size());
		Random randomIndex = new Random();
		//String randomMenuSelected = menuCategories.get(randomIndex.nextInt(menuCategories.size())).findElement(By.tagName("a")).getAttribute("title");
		//System.out.println(menuCategories.get(randomIndex.nextInt(menuCategories.size())).findElement(By.tagName("a")).getAttribute("title"));
		// category Premium Deals, T¯etinka, Pro ökoly a st·t does not work for this, they are not normal goods categories
		menuCategories.get(randomIndex.nextInt(menuCategories.size())).findElement(By.tagName("a")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(("h1"))));	
		System.out.println(driver.findElement(By.tagName("h1")).getText());
		
		
		
		
		for (WebElement menuCategory : menuCategories) {
			//System.out.println(menuCategory.findElement(By.tagName("a")).getAttribute("title"));
		}
		driver.close();
    }
    public static void testSorting() {
    	    	  	
    }
/*

*/


    
}