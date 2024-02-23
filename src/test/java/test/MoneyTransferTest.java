package test;

import data.DataHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import page.LoginPageV1;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class MoneyTransferTest {

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
        var BalanceFirstCard = dashboardPage.getCardBalance(idCardFirst); // баланс первой карты
        var BalanceSecondCard = dashboardPage.getCardBalance(idCardSecond); // баланс второй карты
        var transferPage = dashboardPage.selectCardToTransfer(idCardFirst); // выбор первой карты для пополнения, нажатие кнопки "Пополнить"
        int addAmount = DataHelper.generateBalance(BalanceSecondCard); // случайная сумма пополнения с баланса второй карты
        transferPage.validTransfer(String.valueOf(addAmount), DataHelper.getSecondInfoCard()); // операция перевода/**/

        var expectedBalanceFirstCard = BalanceFirstCard + addAmount; // баланс первой карты после пополнения
        var expectedBalanceSecondCard = BalanceSecondCard - addAmount; // баланс второй карты после пополнения
        Assertions.assertEquals(expectedBalanceFirstCard, dashboardPage.getCardBalance(idCardFirst));
        Assertions.assertEquals(expectedBalanceSecondCard, dashboardPage.getCardBalance(idCardSecond));
        $("[data-test-id=dashboard]").shouldBe(visible); // проверка возврата на страницу со списком карт
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
        var BalanceFirstCard = dashboardPage.getCardBalance(idCardFirst); // баланс первой карты
        var BalanceSecondCard = dashboardPage.getCardBalance(idCardSecond); // баланс второй карты
        var transferPage = dashboardPage.selectCardToTransfer(idCardSecond); // выбор первой карты для пополнения, нажатие кнопки "Пополнить"
        int addAmount = DataHelper.generateBalance(BalanceFirstCard); // случайная сумма пополнения с баланса первой карты
        transferPage.validTransfer(String.valueOf(addAmount), DataHelper.getFirstInfoCard()); // операция перевода

        var expectedBalanceFirstCard = BalanceFirstCard - addAmount; // баланс первой карты после пополнения
        var expectedBalanceSecondCard = BalanceSecondCard + addAmount; // баланс второй карты после пополнения
        Assertions.assertEquals(expectedBalanceFirstCard, dashboardPage.getCardBalance(idCardFirst));
        Assertions.assertEquals(expectedBalanceSecondCard, dashboardPage.getCardBalance(idCardSecond));
        $("[data-test-id=dashboard]").shouldBe(visible); // проверка возврата на страницу со списком карт
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
}
