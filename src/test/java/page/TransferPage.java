package page;

import com.codeborne.selenide.SelenideElement;
import data.DataHelper;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private SelenideElement transferHeader = $(".h1");
    private SelenideElement summField = $("[data-test-id=amount] .input__control"); // сумма перевода
    private SelenideElement fromField = $("[data-test-id=from] .input__control"); // откуда перевод
    private SelenideElement addSummButton = $("[data-test-id=action-transfer] .button__text"); // кнопка "Пополнить"
    private SelenideElement errorMessage = $("[data-test-id=error-notification]"); // окно об ошибке

    public TransferPage() {
    }

    public void validTransfer(String amountTransfer, DataHelper.InfoCard infoCard) { // корректный перевод денег
        summField.sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE); // очистка поля суммы
        fromField.sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE); // очистка поля откуда
        summField.setValue(amountTransfer); // ввод суммы пополнения
        fromField.setValue(infoCard.getCardNumber()); // ввод номера карты, с которой происходит пополнение
        addSummButton.click(); // нажатие кнопки "Пополнить"

        $("[data-test-id=dashboard]").shouldBe(visible); // проверка возврата на страницу со списком карт
    }

    public void inValidTransfer(String amountTransfer, DataHelper.InfoCard infoCard) { // ошибка на странице перевода
        summField.sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE); // очистка поля суммы
        fromField.sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE); // очистка поля откуда
        summField.setValue(amountTransfer); // ввод суммы пополнения
        fromField.setValue(infoCard.getCardNumber()); // ввод номера карты, с которой происходит пополнение
        addSummButton.click(); // нажатие кнопки "Пополнить"

        $("[data-test-id=error-notification]").shouldBe(visible);
        $("[data-test-id=error-notification]>.notification__title").shouldHave(text("Ошибка"));
        $("[data-test-id=error-notification]>.notification__content").shouldHave(text("Ошибка! Произошла ошибка"));
    }
}
