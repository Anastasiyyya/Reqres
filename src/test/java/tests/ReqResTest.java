package tests;

import com.google.gson.Gson;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;
import reqres_objects.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ReqResTest {

    public static final String BASE_URL = "https://reqres.in/api/";
    public static final String AVATAR_URL = "https://reqres.in/img/faces/7-image.jpg";

    /**
     * This test checks status code and all exist fields of the first user
     */
    @Test(description = "Gets list of users and checks status code and all exist fields of the first user")
    public void getListOfUsersTest() {
        String body =
        given()
        .when()
                .get(BASE_URL + "users?page=2")
        .then()
                .log().all()
                .statusCode(200)
                .extract().body().asString();
        ListOfUsers listOfUsers = new Gson().fromJson(body, ListOfUsers.class);
        Assert.assertEquals(listOfUsers.getData().get(0).getId(), 7);
        Assert.assertEquals(listOfUsers.getData().get(0).getEmail(), "michael.lawson@reqres.in");
        Assert.assertEquals(listOfUsers.getData().get(0).getFirstName(), "Michael");
        Assert.assertEquals(listOfUsers.getData().get(0).getLastName(), "Lawson");
        Assert.assertEquals(listOfUsers.getData().get(0).getAvatar(), AVATAR_URL);
    }

    /**
     * This test checks status code and all exist fields of the user
     */
    @Test(description = "Gets user, then checks status code and all exist fields of the user")
    public void getSingleUserTest() {
        String body =
        given()
        .when()
                .get(BASE_URL + "users/2")
        .then()
                .log().all()
                .statusCode(200)
                .extract().body().asString();
        SingleUser singleUser = new Gson().fromJson(body, SingleUser.class);
        Assert.assertEquals(singleUser.getData().getId(), 2);
        Assert.assertEquals(singleUser.getData().getEmail(), "janet.weaver@reqres.in");
        Assert.assertEquals(singleUser.getData().getFirstName(), "Janet");
        Assert.assertEquals(singleUser.getData().getLastName(), "Weaver");
        Assert.assertEquals(singleUser.getData().getAvatar(), "https://reqres.in/img/faces/2-image.jpg");
    }

    /**
     * This test checks status code
     */
    @Test(description = "checks status code")
    public void singleUserNotFoundTest() {
        given()
        .when()
                .get(BASE_URL + "users/23")
        .then()
                .log().all()
                .body(equalTo("{}"))
                .statusCode(404);
    }

    /**
     * This test checks status code and all fields of the first user
     */
    @Test(description = "checks status code and all fields of the first user")
    public void getListResourceTest() {
        String body =
        given()
        .when()
                .get(BASE_URL + "unknown")
        .then()
                .log().all()
                .statusCode(200)
                .extract().body().asString();
        ListOfResource listOfResource = new Gson().fromJson(body, ListOfResource.class);
        Assert.assertEquals(listOfResource.getData().get(0).getId(), 1);
        Assert.assertEquals(listOfResource.getData().get(0).getName(), "cerulean");
        Assert.assertEquals(listOfResource.getData().get(0).getYear(), 2000);
        Assert.assertEquals(listOfResource.getData().get(0).getColor(),"#98B2D1" );
        Assert.assertEquals(listOfResource.getData().get(0).getPantoneValue(), "15-4020");
    }

    /**
     * This test checks status code and all other fields of the user
     */
    @Test(description = "checks status code and all other fields of the user")
    public void getSingleResourceTest() {
        String body =
                given()
                        .when()
                        .get(BASE_URL + "unknown/2")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().body().asString();
        SingleResource singleResource = new Gson().fromJson(body, SingleResource.class);
        Assert.assertEquals(singleResource.getData().getId(), 2);
        Assert.assertEquals(singleResource.getData().getName(), "fuchsia rose");
        Assert.assertEquals(singleResource.getData().getYear(), 2001);
        Assert.assertEquals(singleResource.getData().getColor(),"#C74375" );
        Assert.assertEquals(singleResource.getData().getPantoneValue(), "17-2031");
    }

    /**
     * This test checks status code
     */
    @Test(description = "Verify status code")
    public void singleResourceNotFoundTest() {
        given()
        .when()
                .get( BASE_URL + "unknown/23")
        .then()
                .log().all()
                .body(equalTo("{}"))
                .statusCode(404);
    }

    /**
     * This method create user,  checks status code, 'name' field, 'job' field
     */
    @Test(description = "create user,  checks status code, 'name' field, 'job' field")
    public void postUserTest() {
        User user = User.builder()
                .name("morpheus")
                .job("leader")
                .build();
        given()
                .body(user)
                .header("Content-Type", "application/json")
                .when()
                .post(BASE_URL + "users")
                .then()
                .log().all()
                .body("name", equalTo(user.getName()),
                        "job", equalTo(user.getJob()))
                .statusCode(201);
    }

    /**
     * This test create user with empty data, then put data, checks status code and new data
     */
    @Test(description = "Create user, then put data, checks status code and new data")
    public void postUpdateUserTest() {
        User user1 = User.builder()
                .name("")
                .job("")
                .build();
        given()
                .contentType(ContentType.JSON)
                .body(user1)
        .when()
                .post(BASE_URL + "users/2")
        .then()
                .log().all();
        user1 = User.builder()
                .name("morpheus")
                .job("zion resident")
                .build();
        given()
                .contentType(ContentType.JSON)
                .body(user1)
        .when()
                .put(BASE_URL + "users/2")
        .then()
                .log().all()
                .body("name", equalTo(user1.getName()), "job", equalTo(user1.getJob()))
                .statusCode(200);
    }

    /**
     * This test create user with empty data, then patch data, checks status code and new data
     */
    @Test(description = "Create user, then patch data, checks status code and new data")
    public void patchUpdateUserTest() {
        User user = User.builder()
                .name("")
                .job("")
                .build();
        given()
                .contentType(ContentType.JSON)
                .body(user)
        .when()
                .post(BASE_URL + "users/2")
        .then()
                .log().all();
        user = User.builder()
                .name(user.getName())
                .job("zion resident")
                .build();
        given()
                .contentType(ContentType.JSON)
                .body(user)
        .when()
                .patch(BASE_URL + "users/2")
        .then()
                .log().all()
                .body("name", equalTo(user.getName()), "job", equalTo(user.getJob()))
                .statusCode(200);
    }

    /**
     * This test checks status that user has been deleted
     */
    @Test(description = "checks status that user has been deleted")
    public void deleteUserTest() {
        given()
        .when()
                .delete(BASE_URL + "users/2")
        .then()
                .log().all()
                .body(equalTo(""))
                .statusCode(204);
    }

    /**
     * This test register user and checks status code and value of 'id'
     */
    @Test(description = "checks status code and value of'id'")
    public void registerUnsuccessfulTest() {
        UsersForUsersList usersForUsersList = UsersForUsersList.builder()
                .email("eve.holt@reqres.in")
                .password("pistol")
                .build();
        given()
        .when()
                .contentType(ContentType.JSON)
                .body(usersForUsersList)
                .post(BASE_URL + "register")
        .then()
                .log().all()
                .body("$", hasValue(4))
                .statusCode(200);
    }

    /**
     * This test register user and checks status code and error message
     */
    @Test(description = "checks status code and error message")
    public void registerSuccessfulTest() {
        UsersForUsersList usersForUsersList = UsersForUsersList.builder()
                .email("sydney@fife")
                .build();
        given()
        .when()
                .contentType(ContentType.JSON)
                .body(usersForUsersList)
                .post(BASE_URL + "register")
        .then()
                .log().all()
                .body("$", hasValue("Missing password"))
                .statusCode(400);
    }

    /**
     * Login and checks status code and existence of a 'token'
     */
    @Test(description = "checks status code and existence of a 'token'")
    public void loginSuccessfulTest() {
        UsersForUsersList usersForUsersList = UsersForUsersList.builder()
                .email("sydney@fife")
                .build();
        given()
        .when()
                .contentType(ContentType.JSON)
                .body(usersForUsersList)
                .post(BASE_URL + "register")
        .then()
                .log().all()
                .body("$", hasKey("token"))
                .statusCode(200);
    }

    /**
     * Login and checks status code and existence of an 'error'
     */
    @Test(description = "checks status code and existence of an 'error'")
    public void loginUnsuccessfulTest() {
        UsersForUsersList usersForUsersList = UsersForUsersList.builder()
                .email("sydney@fife")
                .build();
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(usersForUsersList)
                .post(BASE_URL + "register")
                .then()
                .log().all()
                .body("$", hasKey("error"))
                .statusCode(400);
    }

    /**
     * This test at first gets users list with delay and then verify status code
     * also checks keys values of first user
     */
    @Test(description = "Get users list with delay, then verify status code and third user 'email' field")
    public void getDelayedResponseTest() {
        String body =
        given()
        .when()
                .get(BASE_URL + "users?delay=3")
        .then()
                .log().all()
                .statusCode(200)
                .extract().body().asString();
        ListOfUsers listOfUsers = new Gson().fromJson(body, ListOfUsers.class);
        Assert.assertEquals(listOfUsers.getData().get(0).getId(), 1);
        Assert.assertEquals(listOfUsers.getData().get(0).getEmail(), "george.bluth@reqres.in");
        Assert.assertEquals(listOfUsers.getData().get(0).getFirstName(), "George");
        Assert.assertEquals(listOfUsers.getData().get(0).getLastName(), "Bluth");
        Assert.assertEquals(listOfUsers.getData().get(0).getAvatar(), "https://reqres.in/img/faces/1-image.jpg");
    }
}
