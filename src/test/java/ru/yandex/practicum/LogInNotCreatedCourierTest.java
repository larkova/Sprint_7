package ru.yandex.practicum;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.yandex.practicum.client.CourierClient;
import ru.yandex.practicum.model.CourierCredentials;
import ru.yandex.practicum.model.CourierGenerator;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.hamcrest.CoreMatchers.is;



public class LogInNotCreatedCourierTest {
    private CourierClient courierClient;
    private int courierId;

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter(),
                new AllureRestAssured()
        );
    }

    @Before
    public void setUp() {courierClient = new CourierClient();}

    @After
    public void clearData() {
        courierClient.delete(courierId);
    }
    @Test
    @DisplayName("Авторизация незарегистрированного курьера")
    @Description("Курьер не может авторизоваться, если учетная запись не создана")
    public void courierCanNotLogInWithoutAccount() {

        CourierCredentials courierCredentials = CourierCredentials.from(CourierGenerator.getRandom());

        courierClient.login(courierCredentials)
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", is("Учетная запись не найдена"));

    }
}
