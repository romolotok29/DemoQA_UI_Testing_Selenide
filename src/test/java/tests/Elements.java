package tests;

import baseEntities.BaseTest;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

import static com.codeborne.selenide.Condition.*;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selectors.byText;

public class Elements extends BaseTest {

    @Test
    public void textBoxTest() {
        open("/text-box");

        $("#userName").setValue("James Williams");
        $("#userEmail").setValue("jw@example.com");
        $("#currentAddress").setValue("LT-1055");
        $("#permanentAddress").setValue("Some Permanent Address");
        $("#submit").click();

        $("#output").shouldBe(visible);
        $("#name").shouldHave(exactText("Name:James Williams"));
    }

    @Test
    public void checkBoxTest() {
        open("/checkbox");

        $(".rct-checkbox").click();
        $(By.xpath("//span[text()='You have selected :']")).shouldHave(text("You have selected :"));

    }

    @Test
    public void radioButtonTest() {
        open("/radio-button");

        $(".custom-control-label")
                .shouldBe(clickable)
                .click();

        $("#yesRadio").shouldBe(selected);

        $(".text-success")
                .should(appear)
                .shouldHave(exactText("Yes"));

        $(By.xpath("//label[@for='impressiveRadio']")).click();

        $("#impressiveRadio").shouldBe(checked);

        $(".text-success").shouldHave(text("Impressive"));

        $("#noRadio").shouldBe(disabled);
    }

    @Test
    public void webTablesTest() {
        open("/webtables");

        $$(".rt-tr-group").findBy(text("Alden")).shouldHave(text("Alden"));

        $("#delete-record-2").click();

        $$(".rt-tr-group").findBy(text("Alden")).should(disappear).shouldBe(hidden);


    }

    @Test
    public void buttonsTest() {
        open("/buttons");

        $("#doubleClickBtn").doubleClick();
        $("#doubleClickMessage")
                .should(appear)
                .shouldHave(text("You have done a double click"));

        $("#rightClickBtn").contextClick();
        $("#rightClickMessage")
                .should(appear)
                .shouldHave(text("You have done a right click"));

        $(By.xpath("//button[text()='Click Me']"))
                .shouldBe(visible)
                .click();

        $("#dynamicClickMessage")
                .should(appear)
                .shouldHave(text("You have done a dynamic click"));
    }

    @Test
    public void linksTest() {
        open("/links");

        $("#created").click();
        $("#linkResponse").should(appear).shouldHave(text("Link has responded with staus "));
    }

    @Test
    public void validAndBrokenLinksTest() {
        open("/broken");

        $("//a[contains(text(), 'Click Here for Valid Link')]").click();


    }

    @Test
    public void downloadFileTest() {
        open("/upload-download");

        String dataUrl = $("#downloadButton").attr("href");
        if (dataUrl.startsWith("data:image/jpeg;base64,")) {
            // Убираем префикс
            String base64Data = dataUrl.substring("data:image/jpeg;base64,".length());

            // Декодируем base64 строку
            byte[] fileData = Base64.getDecoder().decode(base64Data);

            // Сохраняем данные в файл
            try (FileOutputStream fos = new FileOutputStream("sampleFile.jpeg")) {
                fos.write(fileData);
                System.out.println("File saved as sampleFile.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Unexpected data URL format: " + dataUrl);
        }
    }

    @Test
    public void uploadFileTest() {
        open("/upload-download");

        String filePath = Elements.class.getClassLoader().getResource("sampleFile.jpeg").getPath();
        $("#uploadFile").uploadFile(new File(filePath));

        $("#uploadedFilePath")
                .should(appear)
                .shouldBe(visible)
                .shouldHave(text("C:\\fakepath\\sampleFile.jpeg"));
    }

    @Test
    public void dynamicPropertiesTest () {
        open("/dynamic-properties");

        SelenideElement colorChangeButton = $(By.id("colorChange"));

        // Проверяем, что кнопка не имеет класса "text-danger" до изменения цвета
        boolean initialClass = colorChangeButton.getAttribute("class").contains("text-danger");
        System.out.println("Initial class contains 'text-danger': " + initialClass);
        assert !initialClass;

        // Ждем 6 секунд, чтобы убедиться, что цвет изменился и класс "text-danger" добавлен
        sleep(6000);

        // Проверяем, что класс "text-danger" добавлен после изменения цвета
        boolean changedClass = colorChangeButton.getAttribute("class").contains("text-danger");
        System.out.println("Changed class contains 'text-danger': " + changedClass);

        // Убедитесь, что класс "text-danger" добавлен
        assert changedClass;
    }

}
