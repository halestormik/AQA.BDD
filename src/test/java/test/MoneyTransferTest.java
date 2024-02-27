package test;

import com.codeborne.selenide.Configuration;
import data.DataHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import page.LoginPageV1;

import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class MoneyTransferTest {

    @BeforeEach
    public void setup() { // исправление высплывающих окон после обновления Google Chrome
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("credentials_enable_service", false);
        prefs.put("password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        Configuration.browserCapabilities = options;
    }

    @Test
    void shouldTestSuccessfulMoneyTransferFromSecondToFirst() { // успешное пополнение первой карты с баланса второй карты
        open("http://localhost:9999");
        var loginPage = new LoginPageV1();
        var authInfo = DataHelper.getAuthInfo(); // получить корректные данные для авторизации
        var verificationPage = loginPage.validLogin(authInfo); // ввести корректные данные для авторизации
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo); // получить корректный проверочный код
        var dashboardPage = verificationPage.validVerify(verificationCode); // ввести корректныйь проверочный код
        var idCardFirst = DataHelper.getFirstInfoCard().getCardId();  // получить id первой карты
        var idCardSecond = DataHelper.getSecondInfoCard().getCardId(); // получить id второй карты
        var balanceFirstCard = dashboardPage.getCardBalance(idCardFirst); // баланс первой карты
        var balanceSecondCard = dashboardPage.getCardBalance(idCardSecond); // баланс второй карты
        var transferPage = dashboardPage.selectCardToTransfer(idCardFirst); // выбор первой карты для пополнения, нажатие кнопки "Пополнить"
        int addAmount = DataHelper.generateBalance(balanceSecondCard); // случайная сумма пополнения с баланса второй карты
        transferPage.validTransfer(String.valueOf(addAmount), DataHelper.getSecondInfoCard()); // операция перевода

        var expectedBalanceFirstCard = balanceFirstCard + addAmount; // баланс первой карты после пополнения
        var expectedBalanceSecondCard = balanceSecondCard - addAmount; // баланс второй карты после пополнения
        Assertions.assertEquals(expectedBalanceFirstCard, dashboardPage.getCardBalance(idCardFirst));
        Assertions.assertEquals(expectedBalanceSecondCard, dashboardPage.getCardBalance(idCardSecond));
    }

    @Test
    void shouldTestSuccessfulMoneyTransferFromFirstToSecond() { // успешное пополнение второй карты с баланса первой карты
        open("http://localhost:9999");
        var loginPage = new LoginPageV1();
        var authInfo = DataHelper.getAuthInfo(); // получить корректные данные для авторизации
        var verificationPage = loginPage.validLogin(authInfo); // ввести корректные данные для авторизации
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo); // получить корректный проверочный код
        var dashboardPage = verificationPage.validVerify(verificationCode); // ввести корректныйь проверочный код
        var idCardFirst = DataHelper.getFirstInfoCard().getCardId();  // получить id первой карты
        var idCardSecond = DataHelper.getSecondInfoCard().getCardId(); // получить id второй карты
        var balanceFirstCard = dashboardPage.getCardBalance(idCardFirst); // баланс первой карты
        var balanceSecondCard = dashboardPage.getCardBalance(idCardSecond); // баланс второй карты
        var transferPage = dashboardPage.selectCardToTransfer(idCardSecond); // выбор первой карты для пополнения, нажатие кнопки "Пополнить"
        int addAmount = DataHelper.generateBalance(balanceFirstCard); // случайная сумма пополнения с баланса первой карты
        transferPage.validTransfer(String.valueOf(addAmount), DataHelper.getFirstInfoCard()); // операция перевода

        var expectedBalanceFirstCard = balanceFirstCard - addAmount; // баланс первой карты после пополнения
        var expectedBalanceSecondCard = balanceSecondCard + addAmount; // баланс второй карты после пополнения
        Assertions.assertEquals(expectedBalanceFirstCard, dashboardPage.getCardBalance(idCardFirst));
        Assertions.assertEquals(expectedBalanceSecondCard, dashboardPage.getCardBalance(idCardSecond));
    }

    @Test
    void shouldTestIncorrectVerificationCode() { // неправильный ввод проверочного кода
        open("http://localhost:9999");
        var loginPage = new LoginPageV1();
        var authInfo = DataHelper.getAuthInfo(); // получить корректные данные для авторизации
        var verificationPage = loginPage.validLogin(authInfo); // ввести корректные данные для авторизации
        var verificationCode = DataHelper.getWrongVerificationCodeFor(authInfo); // получить некорректный проверочный код
        verificationPage.invalidVerify(verificationCode); // ввести некорректный код
    }

    @Test
    void shouldTestIncorrectLogin() { // ввод неправильного логина
        open("http://localhost:9999");
        var loginPage = new LoginPageV1();
        var authInfo = DataHelper.getWrongAuthInfo(); // получить некорректные данные для авторизации
        var verificationPage = loginPage.validLogin(authInfo); // ввести некорректные данные для авторизации
        var verificationCode = DataHelper.getWrongVerificationCodeFor(authInfo); // получить некорректный проверочный код
        verificationPage.invalidVerify(verificationCode); // ввести некорректный код
    }

    @Test
    void shouldTestTransferPageWithEmptyFields() { // страница перевода с пустыми полями
        open("http://localhost:9999");
        var loginPage = new LoginPageV1();
        var authInfo = DataHelper.getAuthInfo(); // получить корректные данные для авторизации
        var verificationPage = loginPage.validLogin(authInfo); // ввести корректные данные для авторизации
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo); // получить корректный проверочный код
        var dashboardPage = verificationPage.validVerify(verificationCode); // ввести корректныйь проверочный код
        var idCardFirst = DataHelper.getFirstInfoCard().getCardId();  // получить id первой карты
        var transferPage = dashboardPage.selectCardToTransfer(idCardFirst); // выбор первой карты для пополнения, нажатие кнопки "Пополнить"
        transferPage.moneyTransfer("", DataHelper.getZeroInfoCard()); // операция перевода с пустыми полями
        transferPage.errorMessage("Ошибка! Произошла ошибка");
    }
}
