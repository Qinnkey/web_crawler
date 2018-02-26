package com.huisou.crawler;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByCssSelector;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;


/** 
* @author qinkai 
* @date 2017年8月31日
* @return 51cto网站数据
*/
// 数据抓取:method 2 
// selenium + phantomjs  WebElement是所有抓取数据的接口
public class clickedEvent { 
	
        public static void main(String[] args) throws Exception {
            //设置必要参数
            DesiredCapabilities dcaps = new DesiredCapabilities();
            //ssl证书支持
            dcaps.setCapability("acceptSslCerts", true);
            //截屏支持
            dcaps.setCapability("takesScreenshot", true);
            //css搜索支持
            dcaps.setCapability("cssSelectorsEnabled", true);
            //js支持
            dcaps.setJavascriptEnabled(true);
            //驱动支持
            dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,"e:/DownLoad/phantomjs/phantomjs-2.1.1-windows/phantomjs-2.1.1-windows/bin/phantomjs.exe");
            //创建无界面浏览器对象
//            PhantomJSDriver driver = new PhantomJSDriver(dcaps);
            PhantomJSDriver driver = new PhantomJSDriver(dcaps);

    try {
           	//让浏览器访问51cto主页
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//            driver.get("http://www.51cto.com/");
            driver.get("https://i.qq.com/");
            
            		
            Thread.sleep(1000L);
            // 主界面句柄,比如在三层子目录下返回主界面，当然也可以driver.get("http://.........")
            String mainHandle = driver.getWindowHandle();
            WebElement pwdLoginbutton = driver.findElement(By.xpath("//*[@id='lay']/div[2]/div[1]/div[2]/p[1]/a[2]"));
            System.out.println("99999999999999999   " + pwdLoginbutton.getText().toString() + "   999999999999999999999");
            // 点击登陆按钮
            pwdLoginbutton.click();
            Thread.sleep(1000L);
            
            //获取账号密码输入框的节点
            WebElement userNameElement = driver.findElement(ByCssSelector.cssSelector("#loginform-username"));
            WebElement pwdElement = driver.findElement(ByCssSelector.cssSelector("#loginform-password"));
            userNameElement.sendKeys("苏雨泽0516");
            pwdElement.sendKeys("qk02048539qinkai");
            //获取登录按钮
            WebElement loginButton = driver.findElement(ByCssSelector.cssSelector(".loginbtn"));
            loginButton.click();
            //设置线程休眠时间等待页面加载完成
            Thread.sleep(1000L);

            //获取新页面窗口句柄并跳转，模拟登陆完成
            String windowHandle = driver.getWindowHandle();
            driver.switchTo().window(windowHandle);
            //获取要抓取的元素,并设置等待时间,超出抛异常
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            //设置设置线程休眠时间等待页面加载完成
            Thread.sleep(1000L);
            WebElement firstTalk = driver.findElement(ByCssSelector.cssSelector(".login-suc > a:nth-child(2)"));
            String name = firstTalk.getText();
            System.out.println("userName = " + name + "=========");
            
            // 返回主界面,实际中可能不需要
            driver.switchTo().window(mainHandle);
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            Thread.sleep(1000L);
            WebElement mainTalk = driver.findElement(ByCssSelector.cssSelector(".rinfo1 > a:nth-child(1)"));
            System.out.println("main window element : " + mainTalk.getText() + "===========");

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }finally{
                //关闭并退出浏览器
                driver.close();
                driver.quit();
            }
        }   
}
