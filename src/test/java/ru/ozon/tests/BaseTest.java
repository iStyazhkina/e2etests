package ru.ozon.tests;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.junit5.AllureJunit5;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.ozon.rest.ApiService;
import ru.ozon.service.WebService;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.getWebDriverLogs;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.refresh;
import static com.codeborne.selenide.logevents.SelenideLogger.addListener;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.openqa.selenium.logging.LogType.BROWSER;
import static ru.ozon.service.WebService.getRemoteUrl;
import static ru.ozon.service.WebService.isRemote;
import static ru.ozon.utils.AttachmentsHelper.attachBrowserLogs;
import static ru.ozon.utils.AttachmentsHelper.attachPageSource;
import static ru.ozon.utils.AttachmentsHelper.attachScreenshot;
import static ru.ozon.utils.AttachmentsHelper.attachVideo;
import static ru.ozon.utils.TestUtils.prepareCookies;

@ExtendWith(AllureJunit5.class)
public class BaseTest {

    public static String remoteUrlPart = System.getProperty("remote.browser.url", "selenoid.autotests.cloud");

    protected ApiService apiService = new ApiService();

    @BeforeAll
    static void setUp() {
        addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true));

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", true);
        Configuration.browserCapabilities = capabilities;

        Configuration.baseUrl = "https://www.ozon.ru/";
        Configuration.startMaximized = true;

        if (isEmpty(System.getProperty("selenide.browser"))) {
            Configuration.browser = "chrome";
        }

        if (isRemote()) {
            Configuration.remote = String.valueOf(getRemoteUrl());
        }
    }

    @BeforeEach
    void changeLocalization() {
        open("");
        apiService.changeLocalization(prepareCookies());
        refresh();
    }

    @AfterEach
    public void closeBrowser() {
        attachBrowserLogs(String.join("\n", getWebDriverLogs(BROWSER)));
        attachScreenshot();
        attachPageSource();
        if (WebService.isVideoOn()) {
            attachVideo();
        }
        closeWebDriver();
    }
}
