import geb.report.PageSourceReporter
import org.openqa.selenium.Dimension
import org.openqa.selenium.Platform
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.remote.CapabilityType
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.ie.InternetExplorerDriver
import org.openqa.selenium.remote.LocalFileDetector
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.safari.SafariDriver
import java.util.concurrent.TimeUnit


baseNavigatorWaiting = true /*Sometimes Firefox throws an error 'Root Element not loaded'*/
cacheDriverPerThread = true /*Caches all the resources per thread*/
atCheckWaiting = true

def browserDriver;
String sep = File.separator;
DesiredCapabilities capabilities;
String gridUrl = System.getProperty("gridUrl");
String parallel = System.getProperty("parallel");
String os = System.getProperty("op.sys.name");
String rootDir = new File(".").getCanonicalPath();

driver = {

    /************* Use this for Chrome ************/
    System.setProperty("geb.env", "chrome")
    String chromeDriverPath = os.equals("UNIX") ? "/usr/local/bin/chromedriver".replace("/", sep) : rootDir + "/src/main/groovy/Drivers/chromedriver.exe".replace('/', sep)
    System.setProperty("webdriver.chrome.driver", chromeDriverPath)
    capabilities = DesiredCapabilities.chrome();
    ChromeOptions options = new ChromeOptions();
    options.addArguments("test-type");
    capabilities.setCapability(ChromeOptions.CAPABILITY, options);
    browserDriver = new ChromeDriver(capabilities)
    browserDriver.manage().window().maximize()
    return browserDriver

    /************* Use this for Firefox*****************/
    /*browserDriver = new FirefoxDriver()
    browserDriver.manage().window().maximize()
    browserDriver.manage().timeouts().pageLoadTimeout(20,TimeUnit.MINUTES)
    return browserDriver*/

    /************* Use this for Safari******************/
    /* System.setProperty("webdriver.safari.noinstall", "true");
    System.setProperty("geb.env","safari")
    capabilities = DesiredCapabilities.safari();
    capabilities.setPlatform(Platform.WINDOWS);
    browserDriver = (parallel.equalsIgnoreCase("no")) ? new SafariDriver(capabilities) : new RemoteWebDriver(new URL(gridUrl), capabilities);
    Thread.sleep(5000);
    return browserDriver*/

    /************** Use this for Remote ****************/
    /*capabilities = DesiredCapabilities.firefox();
    browserDriver = new RemoteWebDriver(new URL(gridUrl), capabilities);
    return browserDriver;*/

    /************* Use this for IE *****************/
    /*System.setProperty("webdriver.ie.driver", "src/main/groovy/Drivers/IEDriverServer.exe")
    System.setProperty("geb.env", "ie")
    DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
    def browserDriver=new InternetExplorerDriver(capabilities)
    browserDriver.manage().window().maximize()
    return browserDriver*/
}

waiting {
    timeout = Integer.parseInt(System.getProperty("test.timeout", "30"))

    presets {
        longwait {
            timeout = 90
        }
        mediumwait {
            timeout = 45
        }
        shortwait{
            timeout = 10
        }
        averagewait {
            timeout = 15
        }
        fast {
            timeout = 5
        }
        quick {
            timeout = 1
        }
        waitWithRetry {
            timeout = 45
            retryInterval = 3
        }
    }
}

environments {

    ie {
        driver = {
            capabilities = DesiredCapabilities.internetExplorer();
            capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
            capabilities.internetExplorer().setCapability("ignoreProtectedModeSettings", true);
            capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            capabilities.setJavascriptEnabled(true);
            capabilities.setCapability("requireWindowFocus", true);
            capabilities.setCapability("enablePersistentHover", false);
            File file = new File(rootDir + "/src/main/groovy/Drivers/IEDriverServer.exe".replace("/", sep));
            System.setProperty("webdriver.ie.driver", file.path);

            browserDriver = (parallel.equalsIgnoreCase("no")) ? new InternetExplorerDriver(capabilities) : new RemoteWebDriver(new URL(gridUrl), capabilities);
            browserDriver.setFileDetector(new LocalFileDetector())
            browserDriver.manage().timeouts().pageLoadTimeout(30, TimeUnit.MINUTES)
            browserDriver.manage().window().setSize(new Dimension(1280,1024))
            browserDriver.manage().window().maximize()
            return browserDriver;
        }
    }

    firefox {
        driver = {
            capabilities = DesiredCapabilities.firefox()
            browserDriver = (parallel.equalsIgnoreCase("no")) ? new FirefoxDriver(capabilities) : (new RemoteWebDriver(new URL(gridUrl), capabilities));
            if(parallel.equalsIgnoreCase("yes")){
                browserDriver.setFileDetector(new LocalFileDetector())
            }
            browserDriver.manage().timeouts().pageLoadTimeout(30, TimeUnit.MINUTES)
            browserDriver.manage().window().setSize(new Dimension(1280,1024))
            browserDriver.manage().window().maximize()
            return browserDriver;
        }
    }

    chrome {
        driver = {
            capabilities = DesiredCapabilities.chrome();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("test-type");

            capabilities.setCapability(ChromeOptions.CAPABILITY, options);
            String chromeDriverPath = os.equals("UNIX") ? "/usr/local/bin/chromedriver".replace("/", sep) : rootDir + "/src/main/groovy/Drivers/chromedriver.exe".replace('/', sep)
            System.setProperty("webdriver.chrome.driver", chromeDriverPath)

            browserDriver = (parallel.equalsIgnoreCase("no")) ? new ChromeDriver(capabilities) : new RemoteWebDriver(new URL(gridUrl), capabilities);
            if(parallel.equalsIgnoreCase("yes")){
                browserDriver.setFileDetector(new LocalFileDetector());
            }
            browserDriver.manage().timeouts().pageLoadTimeout(30, TimeUnit.MINUTES);
            browserDriver.manage().window().setSize(new Dimension(1280,1024))
            browserDriver.manage().window().maximize();
            return browserDriver;
        }
    }

    safari {
        driver = {
            System.setProperty("webdriver.safari.noinstall", "true");
            capabilities = DesiredCapabilities.safari();
            Platform platform = os.equals("UNIX") ? Platform.UNIX : Platform.WINDOWS
            capabilities.setPlatform(platform);
            capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

            browserDriver = (parallel.equalsIgnoreCase("no")) ? new SafariDriver(capabilities) : new RemoteWebDriver(new URL(gridUrl), capabilities);
            browserDriver.manage().window().setSize(new Dimension(1280,1024))
            Thread.sleep(20000);
            return browserDriver
        }
    }
}