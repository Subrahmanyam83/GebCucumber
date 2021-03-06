package Specs

import cucumber.api.CucumberOptions
import cucumber.api.testng.AbstractTestNGCucumberTests
import org.testng.annotations.BeforeSuite
import org.testng.annotations.Test

/**
 * Created by E002183 on 9/9/2016.
 */

@CucumberOptions(
features = ["src/test/resources/features/wiki.feature"],
        format = ["html:target/site/cucumber-pretty", "pretty","rerun:target/rerun.txt","json:target/cucumber.json"])
class Runner extends AbstractTestNGCucumberTests {

    @BeforeSuite
    public void convertPropertiestoSystemProperties(){
        String rootDir = new File(".").getCanonicalPath() /*Absolute Path*/
        String projectPropertiesPath = rootDir + "/src/test/resources/project.properties".replace('/', File.separator)
        Properties properties = new Properties()
        properties.load(new FileInputStream(projectPropertiesPath))
        properties.each { key, value ->
            if (System.getProperty(key) == null) {
                System.setProperty(key, value);
            }
        }
    }


}
