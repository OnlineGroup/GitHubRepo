import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Porsche {
    private static WebDriver driver;

    @BeforeTest
    public void setUp(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
    }

    /*@AfterTest
    public void tearDown(){
        driver.quit();
    }
   */

    @Test
    public void endToEnd() throws InterruptedException {
        driver.manage().window().fullscreen();
        driver.get("https://www.porsche.com/usa/modelstart/”");
        WebElement model718 = driver.findElement(By.xpath("//img[@alt='Porsche - 718']"));
        model718.click();
        WebElement startingPriceCayman=driver.findElement(By.xpath("//img[@alt='Porsche 718 Cayman']//parent::div//following-sibling::div//child::div[@class='m-14-model-price']"));
        double startingPricePorsche718Cayman=Double.parseDouble(startingPriceCayman.getText().substring(7).replace(",","").replace("*",""));
        WebElement cookiesButton = driver.findElement(By.xpath("//a[@class ='gui-btn-with-icon gui-btn-close']"));
        cookiesButton.click();
        WebElement cayman718 = driver.findElement(By.xpath("//img[@alt='Porsche 718 Cayman']"));
        cayman718.click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        System.out.println(driver.getTitle());

        //Get all window handles
        Set<String> allHandles = driver.getWindowHandles();
        //Get current handle or default handle
        String currentWindowHandle = allHandles.iterator().next();
        //Remove first/default Handle
        allHandles.remove(allHandles.iterator().next());
        //get the last Window Handle
        String lastHandle = allHandles.iterator().next();
        //switch to second/last window, because we know there are only two windows 1-parent window 2-other window(ad window)
        driver.switchTo().window(lastHandle);

        WebElement expandButton = driver.findElement(By.xpath("//*[.='Price for Equipment:']//child::span"));
        Actions actions = new Actions(driver);
        actions.click(expandButton).build().perform();
        driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
        WebElement basePriceCayman = driver.findElement(By.xpath("//*[.='Base price:']//following-sibling::div"));
        System.out.println(basePriceCayman.getText());
        double basePrice = Double.parseDouble(basePriceCayman.getText().substring(1).replace(",",""));
        Assert.assertEquals(basePrice,startingPricePorsche718Cayman);

        WebElement equipmentPriceText = driver.findElement(By.xpath("//*[.='Price for Equipment:']//following-sibling::div"));
        System.out.println(equipmentPriceText.getText());
        double priceEquipment = Double.parseDouble(equipmentPriceText.getText().substring(1));
        WebElement deliveryPrice = driver.findElement(By.xpath("//*[.='Delivery, Processing and Handling Fee']//following-sibling::div[@class='cca-price']"));
        System.out.println("delivery price: "+ deliveryPrice.getText());
        double deliveryPriceDouble = Double.parseDouble(deliveryPrice.getText().substring(1).replace(",",""));
        WebElement totalPrice = driver.findElement(By.xpath("//*[.='Total Price:*']//following-sibling::div[@class='cca-price']"));
        double totalPriceDouble = Double.parseDouble(totalPrice.getText().substring(1).replace(",",""));
        Assert.assertEquals((basePrice+deliveryPriceDouble),totalPriceDouble);
        
        //select miami blue
        WebElement miamiBlue = driver.findElement(By.xpath("//li[@id='s_exterieur_x_FJ5']"));//s_exterieur_x_FJ5
        miamiBlue.click();
        WebElement miamiBluePrice = driver.findElement(By.xpath("//*[.='Miami Blue']//following-sibling::div[@class='tt_price tt_cell']"));
        System.out.println("miami blue price: "+miamiBluePrice.getText());
        double miamiBluePriceDouble = Double.parseDouble(miamiBluePrice.getText().substring(1).replace(",",""));
        //renewing the equipment price
        equipmentPriceText = driver.findElement(By.xpath("//*[.='Price for Equipment:']//following-sibling::div"));
        priceEquipment = Double.parseDouble(equipmentPriceText.getText().substring(1).replace(",",""));
        Assert.assertEquals(priceEquipment,miamiBluePriceDouble);
        //renewing the total price
        totalPrice = driver.findElement(By.xpath("//*[.='Total Price:*']//following-sibling::div[@class='cca-price']"));
        totalPriceDouble = Double.parseDouble(totalPrice.getText().substring(1).replace(",",""));
        Assert.assertEquals(totalPriceDouble,(basePrice + priceEquipment+deliveryPriceDouble));
        
        //Add carrera sport wheels 20
        WebElement carreraSportWheels20 = driver.findElement(By.xpath("//li[@id='s_exterieur_x_MXRD']"));
        carreraSportWheels20.click();
        WebElement carreraSportWheels20Price = driver.findElement(By.xpath("//*[.='20\" Carrera Sport Wheels']//following-sibling::div[@class='tt_price tt_cell']"));
        double carreraWheelsPrice = Double.parseDouble(carreraSportWheels20Price.getText().substring(1).replace(",",""));
        System.out.println("sport wheels price: " + carreraWheelsPrice);
        //renewing the equipment price
        equipmentPriceText = driver.findElement(By.xpath("//*[.='Price for Equipment:']//following-sibling::div"));
        priceEquipment = Double.parseDouble(equipmentPriceText.getText().substring(1).replace(",",""));
        //renewing total price
        totalPrice = driver.findElement(By.xpath("//*[.='Total Price:*']//following-sibling::div[@class='cca-price']"));
        totalPriceDouble = Double.parseDouble(totalPrice.getText().substring(1).replace(",",""));
        //asserting new total price is equal to wheels' price added
        Assert.assertEquals(totalPriceDouble,(priceEquipment+deliveryPriceDouble+basePrice));
        
        //Add power sport seats
        WebElement powerSportSeats = driver.findElement(By.xpath("//span[@id='s_interieur_x_PP06']/.."));
        powerSportSeats.click();
        Thread.sleep(5000);
        WebElement powerSportSeatsPrice = driver.findElement(By.xpath("//*[.='Power Sport Seats (14-way) with Memory Package']/..//div[@class='pBox']//div"));
        double powerSportSeatsPriceDouble = Double.parseDouble(powerSportSeatsPrice.getText().substring(1).replace(",",""));
        System.out.println("power seats price: " + powerSportSeatsPriceDouble);//not adding the power seats price, why?
        //renewing the equipment price
        equipmentPriceText = driver.findElement(By.xpath("//*[.='Price for Equipment:']//following-sibling::div"));
        priceEquipment = Double.parseDouble(equipmentPriceText.getText().substring(1).replace(",",""));
        System.out.println("Equipment price after seats added: " + priceEquipment);
        //renewing total price
        totalPrice = driver.findElement(By.xpath("//*[.='Total Price:*']//following-sibling::div[@class='cca-price']"));
        totalPriceDouble = Double.parseDouble(totalPrice.getText().substring(1).replace(",",""));
        System.out.println("total price after seats are added: " + totalPriceDouble);
        //verify that equipment price is equal to sum of miami blue, sport wheels and power seats prices
        Assert.assertEquals(priceEquipment,(carreraWheelsPrice+miamiBluePriceDouble+powerSportSeatsPriceDouble));
        //asserting new total price is equal to wheels' price added
        Assert.assertEquals(totalPriceDouble,(priceEquipment+deliveryPriceDouble+basePrice));
        driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
        
        //Add interior carbon fiber standard
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("scroll(0, 2500);");//scroll down
        WebElement interiorCarbon = driver.findElement(By.xpath("//div[@id='IIC_subHdl']"));
        WebDriverWait wait2 = new WebDriverWait(driver, 10);
        wait2.until(ExpectedConditions.elementToBeClickable(interiorCarbon));
        jse.executeScript("arguments[0].click()", interiorCarbon);
        Thread.sleep(5000);
        WebElement interiorCarbonStandard = driver.findElement(By.xpath("//div[@id='vs_table_IIC_x_PEKH_x_c04_PEKH_x_shorttext']"));
        jse.executeScript("arguments[0].click()", interiorCarbonStandard);
        Thread.sleep(3000);
        WebElement interiorCarbonP = driver.findElement(By.xpath("//span[@id='vs_table_IIC_x_PEKH_x_c09_PEKH']/..//following-sibling::div//div"));
        double interiorCarbonPrice = Double.parseDouble(interiorCarbonP.getText().substring(1).replace(",",""));
        //get the equipment price and verify it equals to sum of miami blue, sport wheels,power sport seats,carbon fiber interior prices
        equipmentPriceText = driver.findElement(By.xpath("//*[.='Price for Equipment:']//following-sibling::div"));
        System.out.println(equipmentPriceText.getText());
        priceEquipment = Double.parseDouble(equipmentPriceText.getText().substring(1).replace(",",""));
        Assert.assertEquals(priceEquipment,(miamiBluePriceDouble+carreraWheelsPrice+powerSportSeatsPriceDouble+interiorCarbonPrice));

        //choose performance
        WebDriverWait wait = new WebDriverWait(driver,10);
        WebElement performance = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[@id='IMG_wrapper']"))));
        performance.click();//div[text()='Performance']//parent::div[@id='IMG_subHdl']
        WebElement speed7Doppelkupplung = driver.findElement(By.id("vs_table_IMG_x_M250_x_c11_M250"));
        speed7Doppelkupplung.click();
        WebElement speed7DoppelkupplungPriceText = driver.findElement(By.xpath("//span[@id='vs_table_IMG_x_M250_x_c19_M250']/..//following-sibling::div//div"));
        double speed7Price = Double.parseDouble(speed7DoppelkupplungPriceText.getText().substring(1).replace(",",""));
        System.out.println(speed7Price);

        //select Ceramic Composite Brakes
        jse.executeScript("scroll(0, 600);");//scroll down
        WebElement brakes = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//span[@id='vs_table_IMG_x_M450_x_c81_M450']/.."))));
        brakes.click(); //vs_table_IMG_x_M450_x_c81_M450
        WebElement brakesPriceText = driver.findElement(By.xpath("//span[@id='vs_table_IMG_x_M450_x_c89_M450']/..//following-sibling::div//div"));
        double brakesPrice = Double.parseDouble(brakesPriceText.getText().substring(1).replace(",",""));
        System.out.println(brakesPrice);
        Thread.sleep(5000);

        //verify that equipment price is equal to sum of prices chosen elements
        equipmentPriceText = driver.findElement(By.xpath("//*[.='Price for Equipment:']//following-sibling::div"));
        priceEquipment = Double.parseDouble(equipmentPriceText.getText().substring(1).replace(",",""));
        System.out.println(priceEquipment);
        Assert.assertEquals(priceEquipment,(miamiBluePriceDouble+carreraWheelsPrice+powerSportSeatsPriceDouble+interiorCarbonPrice
        +speed7Price+brakesPrice));

        //Verify the total price
        totalPrice = driver.findElement(By.xpath("//*[.='Total Price:*']//following-sibling::div[@class='cca-price']"));
        totalPriceDouble = Double.parseDouble(totalPrice.getText().substring(1).replace(",",""));
        Assert.assertEquals(totalPriceDouble,(priceEquipment+deliveryPriceDouble+basePrice));

    }

}
