package ru.yandex.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.yandex.practicum.client.CourierClient;
import ru.yandex.practicum.model.CourierCredentials;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.CoreMatchers.is;


public class LogInWithoutPasswordTest {
    private CourierClient courierClient;

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter(),
                new AllureRestAssured()
        );
    }

    @Before
    public void setUp() {courierClient = new CourierClient();}
    @Test
    @DisplayName("Авторизация без пароля")
    @Description("Курьер не может авторизоваться без пароля")
    public void courierCanNotLogInWithoutPassword() {

        CourierCredentials courierCredentials=new CourierCredentials("Testlogin1111", null);

        courierClient.login(courierCredentials)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", is("Недостаточно данных для входа"));

    }
}
