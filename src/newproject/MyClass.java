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

	// TODO following lines would be better to be somehow general for whole class / test set
	//final static WebDriver driver = new FirefoxDriver();
	//final static int waitTimeValueInSeconds = 5; // general timeout in seconds
	//final static WebDriverWait wait = new WebDriverWait(driver, waitTimeValueInSeconds);
    
	public static void main(String[] args) {
    	System.out.println("INFO: lets roll out this MyClass test set");
    	
    	testVerifySubcategoryNames();
    	testVerifyMenuNames();
    	testVerifyCorrectItemWasInBasket();
    	
    	System.out.println("INFO: MyClass test set has ended");
    }
    


	public static void testVerifySubcategoryNames() {
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
		WebDriver driver = new FirefoxDriver();
		int waitTimeValueInSeconds = 5; // general timeout in seconds
		WebDriverWait wait = new WebDriverWait(driver, waitTimeValueInSeconds);
		Map <String, List<String>> mapOfCategories = new HashMap<>();
		mapOfCategories.put("Komponenty", new ArrayList<>(Arrays.asList(
				"Intel Optane", "Procesory", "GrafickÈ karty", "Disky", "PamÏti", "Z·kladnÌ desky",
				"Sk¯ÌnÏ a zdroje", "ChlazenÌ", "Overclocking", "OptickÈ mechaniky", "TeleviznÌ karty",
				"ZvukovÈ karty", "SÌùovÈ karty", "ÿadiËe a adaptÈry", "ProgramovatelnÈ stavebnice",
				"Tuning PC", "Z·znamov· za¯ÌzenÌ", "Kabely a konektory", "PC na mÌru", "Mining", 
				"Jak sestavit PC", "ProË nakupovat u n·s", "HW novinky a recenze")));
		//mapOfCategories.put("Notebooky", new ArrayList<>(Arrays.asList("Notebooky za vysvÏdËenÌ", "BÏûnÈ uûitÌ", "HernÌ", "this is not there")));
		// works for any Category (except last three, they are not regular goods categories)
		int errorState = 0;
		
		try {
			System.out.println("INFO: Test testVerifySubcategoryNames started!");
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

				// check the amounts
				if (amountOfTestedSubcategories.equals(amountOfFoundSubcategories)) {
					System.out.println("INFO: amount of tested equals amount found [" + amountOfFoundSubcategories + "].");

				} else {
					driver.close();
					throw new Exception("ERROR: amounts differ! Tested for [" + amountOfTestedSubcategories + "], but found: [" + amountOfFoundSubcategories + "]");
					// could be a screenshot taken here
				}
				
				// gather the text value from "subcategory" web elements on the category page 
				for ( WebElement subcategoryOnPage : listOfSubcategoriesOnPage)  {
					foundSubcategoriesOnPage.add(subcategoryOnPage.getText());
				}

				// checks if specified category name from test list is on the actual category page 
				for (String subcategoryTextFromTest : mapOfCategories.get(category)) {
					if (foundSubcategoriesOnPage.contains(subcategoryTextFromTest)) {
						System.out.println("INFO: [" + subcategoryTextFromTest + "] subcategory is in the subcategory list on the [" + category + "] page");

					} else {
						System.out.println("ERROR: [" + subcategoryTextFromTest + "] is in NOT in the subcategory list on the [" + category + "] page!");
						errorState = 1;
					}

				}

				
				driver.close();
				
				if (errorState != 0) {
					throw new Exception("ERROR: Test testVerifySubcategoryNames failed!");
				} else {
					System.out.println("INFO: Test testVerifySubcategoryNames passed!");
				}

			}
		} catch (Exception e) {
			System.out.println("ERROR: Horrible failure in testVerifySubcategoryNames!");
			e.printStackTrace();
		}
    	  	
    }
    
    public static void testVerifyMenuNames() {
    	
		/* testMenuNames
		this test is supposed to check main menu for Categories, 
		verify names as well as their count and order
		for manual verification: 
		1. open browser and navigate to https://www.alza.cz/ - page should load correctly
		2. verify left menu appears - should be present on the page
		3. verify the amount of elements - should be 21
		4. verify the list of presented categories 
			4a. there should be list of the following, in order:
			"Masakr cen", "Smart", "MobilnÌ telefony", "ChytrÈ hodinky",
			"Notebooky", "PoËÌtaËe a Software", "P¯ÌsluöenstvÌ", "Komponenty", "HernÌ zÛna", "Televize", "Foto Audio Video", 
			"VelkÈ spot¯ebiËe", "Dom·cnost", "Kr·sa a zdravÌ", "Tisk·rny a kancel·¯", "Elektromobilita", "Hobby, sport a dalöÌ",
			"Slevy, bazar", "T¯etinka", "Premium Deals", "Pro ökoly a st·t"
	*/
    	
		WebDriver driver = new FirefoxDriver();
		int waitTimeValueInSeconds = 5; // general timeout in seconds
		WebDriverWait wait = new WebDriverWait(driver, waitTimeValueInSeconds);
    	List<String> menuCategoryTestNames = new ArrayList<>(Arrays.asList("Masakr cen", "Smart", "MobilnÌ telefony", "ChytrÈ hodinky",
				"Notebooky", "PoËÌtaËe a Software", "P¯ÌsluöenstvÌ", "Komponenty", "HernÌ zÛna", "Televize", "Foto Audio Video", 
				"VelkÈ spot¯ebiËe", "Dom·cnost", "Kr·sa a zdravÌ", "Tisk·rny a kancel·¯", "Elektromobilita", "Hobby, sport a dalöÌ",
				"Slevy, bazar", "T¯etinka", "Premium Deals", "Pro ökoly a st·t"));
    	int errorState = 0;
    	
		try {
			System.out.println("INFO: Test testVerifyMenuNames started!");
			driver.get("https://www.alza.cz/");
			wait.until(ExpectedConditions.titleIs("Alza.cz - nejvÏtöÌ obchod s poËÌtaËi a elektronikou | Alza.cz"));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("fmenu")));
			WebElement mainMenu = driver.findElement(By.className("fmenu"));
			List<WebElement> menuCategoriesFromWeb = mainMenu.findElements(By.className("bx"));
			
			if (menuCategoryTestNames.size() == menuCategoriesFromWeb.size()) {
				System.out.println("INFO: number of menu-elements on the web matches the number of tested (" + menuCategoryTestNames.size() + ")");
			} else {
				driver.close();
				throw new Exception("ERROR: amounts differ, tested for (" + menuCategoryTestNames.size() + ") but found (" + menuCategoriesFromWeb.size() + ")");
			}
			
			int index = 0;
			for (WebElement menuCategoryFromWeb : menuCategoriesFromWeb) {
				
				if (menuCategoryFromWeb.findElement(By.tagName("a")).getAttribute("title").equals(menuCategoryTestNames.get(index))) {
					System.out.println("INFO: this menu is the same - index [" + index + "], " + menuCategoryFromWeb.findElement(By.tagName("a")).getAttribute("title") + " " + menuCategoryTestNames.get(index));
				} else {
					System.out.println("ERROR: this menu differs - index [" + index + "], expected [" + menuCategoryFromWeb.findElement(By.tagName("a")).getAttribute("title") + "] but found [" + menuCategoryTestNames.get(index) + "]");
					errorState = 1;
				}
				index++;
			}
			
			driver.close();
			
			if (errorState != 0) {
				throw new Exception("ERROR: Test testVerifyMenuNames failed!");
			} else {
				System.out.println("INFO: Test testVerifyMenuNames passed!");
			}
			
		} catch (Exception e) {
			System.out.println("ERROR: Horrible failure in testVerifyMenuNames!");
			e.printStackTrace();
		}
    }
    
    
    public static void testVerifyCorrectItemWasInBasket() {
		WebDriver driver = new FirefoxDriver();
		int waitTimeValueInSeconds = 5; // general timeout in seconds
		WebDriverWait wait = new WebDriverWait(driver, waitTimeValueInSeconds);
		
		try {
			System.out.println("INFO: Test testVerifyCorrectItemWasInBasket started!");
			driver.get("https://www.alza.cz/");
			wait.until(ExpectedConditions.titleIs("Alza.cz - nejvÏtöÌ obchod s poËÌtaËi a elektronikou | Alza.cz"));
			//wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("fmenu")));
			
			List<WebElement> items = driver.findElements(By.className("item"));
			// was about to pick random item, but then decided to rather pick first visible item (further below), its easier
			// due to many horizontally-scrolled panels there is about 45 products, but not all "on screen"
			//System.out.println("amount of items" + items.size());
			//int randomItemIndex = new Random().nextInt(items.size());
			//WebElement randomItem = items.get(randomItemIndex);
			//System.out.println(randomItem.findElement(By.className("name")).getAttribute("innerHTML"));
			//randomItem.click();
			String itemTitle = null;
			
			// following just picks first 
			for (WebElement item : items) {
				if (item.isDisplayed()) {
					//itemTitle = item.findElement(By.className("name")).getAttribute("innerHTML");
					itemTitle = item.findElement(By.className("name")).getAttribute("innerHTML");
					System.out.println("INFO: The title of a chosen item is [" + itemTitle + "]");
					item.click();
					break;
				}
			}
			wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//a[contains(@class,'btnx normal green buy')]"))));
			driver.findElement(By.xpath("//a[contains(@class,'btnx normal green buy')]")).click();
			
			wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.linkText("ZboûÌ bylo p¯id·no do koöÌku"))));
			driver.findElement(By.linkText("ZboûÌ bylo p¯id·no do koöÌku")).click();
			
			wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("orderpage"))));
			driver.findElement(By.className("mainItem")).click();
			
			wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//a[contains(@class,'btnx normal green buy')]"))));
			
			String itemTitleFromBasket = driver.findElement(By.tagName("h1")).getText();
			
			if (itemTitle.equals(itemTitleFromBasket)) {
				System.out.println("INFO: Item in the basket was the same as the one ordered.");
				System.out.println("INFO: testVerifyCorrectItemWasInBasket passed!");
			} else {
				System.out.println("ERROR: incorrect item was in the basket, expected [" + itemTitle + "], but found [" + itemTitleFromBasket + "]!");
				throw new Exception("ERROR: testVerifyCorrectItemWasInBasket failed!");
			}

			driver.close();
			
		} catch (Exception e) {
			System.out.println("ERROR: Horrible failure in testVerifyCorrectItemWasInBasket!");
			e.printStackTrace();
		}
    }
    
}