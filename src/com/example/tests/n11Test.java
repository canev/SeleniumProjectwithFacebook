package com.example.tests;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class n11Test {


	private static WebDriver driver;
	private static String baseUrl;


	@BeforeClass
	public static void testN11() throws Exception {
		homePage HomePage = new homePage();
		driver = HomePage.setUp();
		baseUrl = HomePage.baseUrl();
	}
	@Test	
	public void testSequence(){
		// 3. Ekranın sağ üstünde yer alan Giriş Yap linkine tıklanılıp login sayfası açılacak. 
		// 4. Bir kullanici ile login olacak ( daha önce siteye üyeliğin varsa o olabilir )
		driver.get(baseUrl);
		driver.findElement(By.linkText("Giriş Yap")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.cssSelector("div.button:nth-child(4)")).click();
		facebookLoginPopup();

		// 5. Ekranın üstündeki Search alanına 'samsung' yazıp Ara butonuna tiklayacak. 
		driver.findElement(By.id("searchData")).click();
		driver.findElement(By.id("searchData")).clear();
		driver.findElement(By.id("searchData")).sendKeys("samsung");
		driver.findElement(By.cssSelector("a.searchBtn")).click();

		// 6. Gelen sayfada samsung için sonuç bulunduğunu onaylayacak.
		verifyTitle("Samsung - n11.com");

		// 7. Arama sonuclarindan 2. sayfaya tıklayacak ve açılan sayfada 2. sayfanın şu an gösterimde olduğunu onaylayacak.
		driver.findElement(By.linkText("2")).click();
		verifyTitle("Samsung - n11.com - 2");

		// üstten 3. ürün seçilir
		driver.findElement(By.cssSelector("li.column:nth-child(4) > div:nth-child(1) > div:nth-child(1) > a:nth-child(1) > img:nth-child(1)")).click();
		String itemText = getItemText();

		// 8. Üstten 3. ürünün içindeki ‘Favorilere ekle' butonuna tıklayacak.
		driver.findElement(By.id("addToFavourites")).click();

		// 9. Ekranin sağ üstündeki ‘Hesabım’ linkine tıklayacak ve açılan sayfanın hesabım ekranı olduğunu onaylayacak.
		driver.findElement(By.linkText("Hesabım")).click();
		verifyTitle("Hesabım - n11.com");

		// 10. Ekranın sol tarafındaki ‘Favorilerim’e tıklayacak. 
		// 11. Açılan sayfada bir önceki sayfada favorilere eklenmiş ürünün bulunduğunu onaylayacak.
		driver.findElement(By.linkText("Favorilerim")).click();
		verifyItem(itemText);

		// 12. Favorilere eklenen bu ürünün yanındaki 'Kaldır' butonuna basarak, favorilerden çıkaracak. 
		// 13. Sayfada bu ürünün artık favorilerde olmadığını onaylayacak.

		driver.findElement(By.linkText("Kaldır")).click();
		boolean empty = (driver.findElements(By.cssSelector(".emptyWatchList"))).isEmpty();
		if(empty)
		{
			verifyItemRemoved(itemText);
		}
	}

	//sayfa doğru kontrol
	private void verifyTitle(String expectedTitle) {
		String actualTitle = driver.getTitle();
		Assert.assertTrue(actualTitle.contains(expectedTitle));
	}

	//ürünün favorilerime eklendiğini kontrol
	private void verifyItem(String expectedHeaderMessage) {
		WebElement element = driver.findElement(By.cssSelector("#watchList > tbody > tr > td.productTitle > p > a"));
		String actualHeaderMessage = element.getText();
		assertThat(actualHeaderMessage, equalTo(expectedHeaderMessage));
	}

	//ürün ismini al
	private String getItemText() {
		String itemText;
		itemText = driver.findElement(By.cssSelector("#contentProDetail > div > div.proDetailArea > div.proDetail > div.proNameHolder > div > h1")).getText();
		return itemText;
	}

	//listeden ürün silinmiş mi kontrol
	private void verifyItemRemoved(String itemText) {
		// Favori listesinde ki elemanları kontrol et
		List<WebElement> elementRemove = driver.findElements(By.cssSelector("#watchList > tbody > tr > td.productTitle > p > a"));

		for (WebElement element : elementRemove)
		{
			String itemtoberemovedText=element.getText();
			Assert.assertNotEquals(itemText, itemtoberemovedText);
		}	
	}

	//facebook kullanıcı adı ve şifre ile login ol
	private void facebookLoginPopup() {
		String parentWindowHandler = driver.getWindowHandle(); // n11 ana sayfası store
		String subWindowHandler = null;

		Set<String> handles = driver.getWindowHandles(); // facebook login pop up window handle
		Iterator<String> iterator = handles.iterator();
		while (iterator.hasNext())
		{
			subWindowHandler = iterator.next();
		}
		driver.switchTo().window(subWindowHandler); // facebook login pop upında iş yap

		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys("seleniumtesti@hotmail.com");
		driver.findElement(By.id("pass")).clear();
		driver.findElement(By.id("pass")).sendKeys("SeleniumTest1!");
		driver.findElement(By.id("u_0_1")).click();

		driver.switchTo().window(parentWindowHandler);  // n11 ana sayfaya dön
	}


}
