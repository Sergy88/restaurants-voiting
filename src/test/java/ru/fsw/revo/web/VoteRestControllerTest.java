package ru.fsw.revo.web;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import ru.fsw.revo.domain.model.Restaurant;
import ru.fsw.revo.domain.model.Vote;
import ru.fsw.revo.service.VoteService;
import ru.fsw.revo.utils.exception.NotFoundException;
import ru.fsw.revo.web.json.JsonUtil;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.fsw.revo.UserTestData.USER1_ID;
import static ru.fsw.revo.VoteTestData.*;

@SpringJUnitWebConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-db.xml"
})
@Transactional
public class VoteRestControllerTest {

    private static final String REST_URL = VoteRestController.REST_URL + "/";
    private static final CharacterEncodingFilter CHARACTER_ENCODING_FILTER = new CharacterEncodingFilter();

    @Autowired
    public Environment env;

    @Autowired
    protected VoteService service;

    static {
        CHARACTER_ENCODING_FILTER.setEncoding("UTF-8");
        CHARACTER_ENCODING_FILTER.setForceEncoding(true);
    }

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @PostConstruct
    private void postConstruct() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter(CHARACTER_ENCODING_FILTER)
                .apply(springSecurity())
                .build();
    }

    protected ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder);
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + VOTE1_ID)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("user", "password")))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(vote1));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + VOTE1_ID))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(VOTE1_ID, USER1_ID));
    }

    @Test
    void update() throws Exception {
        Vote updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + VOTE1_ID).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        Vote vote = service.get(VOTE1_ID, USER1_ID);
        vote.setRestaurant((Restaurant) Hibernate.unproxy(vote.getRestaurant()));
        VOTE_MATCHER.assertMatch(vote, updated);
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(
                        all_votes.stream()
                                .sorted((o1, o2) -> o2.getDate().compareTo(o1.getDate())).collect(Collectors.toList())
                ));
    }
}