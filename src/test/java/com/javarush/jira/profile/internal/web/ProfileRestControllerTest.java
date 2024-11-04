package com.javarush.jira.profile.internal.web;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.profile.ProfileTo;
import com.javarush.jira.profile.internal.ProfileRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.javarush.jira.common.util.JsonUtil.writeValue;
import static com.javarush.jira.profile.internal.web.ProfileTestData.*;
import static com.javarush.jira.login.internal.web.UserTestData.USER_MAIL;
import static com.javarush.jira.login.internal.web.UserTestData.USER_ID;
import static com.javarush.jira.profile.internal.web.ProfileRestController.REST_URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileRestControllerTest extends AbstractControllerTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    @WithUserDetails(value = "admin@gmail.com")
    void getProfile_Success() throws Exception {
        perform( MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(PROFILE_TO_MATCHER.contentJson(USER_PROFILE_TO));
    }


    @Test
    @WithUserDetails(value = USER_MAIL)
    void getProfile_Unauthorized() throws Exception {
        perform(get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateProfile_Success() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(getUpdatedTo())))
                .andExpect(status().isNoContent());

        PROFILE_MATCHER.assertMatch(
                profileRepository.getExisted(USER_ID),
                ProfileTestData.getUpdated(USER_ID)
        );

    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateProfile_InvalidData() throws Exception {
        String invalidJson = "{\"id\":null,\"mailNotifications\":[\"\"],\"contacts\":[{\"code\":\"skype\",\"value\":\"\"}]}";
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isUnprocessableEntity()); // 422 Unprocessable Entity
    }


    @Test
    @WithUserDetails(value = "admin@gmail.com")
    void updateProfile_Forbidden() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"email\":\"user@gmail.com\",\"first_name\":\"userFirstName\",\"last_name\":\"userLastName\"}"))
                        .andExpect(status().isForbidden());

    }
}
