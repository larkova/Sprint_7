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
import ru.yandex.practicum.model.Courier;
import ru.yandex.practicum.model.CourierCredentials;

import static org.apache.http.HttpStatus.SC_CONFLICT;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreatingCourierWithSameLoginTest {
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
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    public void clearData() {
        courierClient.delete(courierId);
    }

    @Test
    @DisplayName("Создание двух курьеров с одинаковым логином")
    @Description("Нельзя создать двух курьеров с одинаковым логином")
    public void twoSameCouriersCanNotBeCreated() {
        Courier courier = new Courier("Testlogin12", "Testpassword", "Elena");
        Courier secondCourier = new Courier("Testlogin", "Testpassword1234", "Alex");

        courierClient.create(courier)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .assertThat()
                .body("ok", is(true));

        courierId = courierClient.login(CourierCredentials.from(courier))
                .assertThat()
                .body("id", notNullValue())
                .extract().path("id");

        courierClient.create(secondCourier)
                .assertThat()
                .statusCode(SC_CONFLICT)
                .and()
                .assertThat()
                .body("message", is("Этот логин уже используется. Попробуйте другой."));

    }

}
