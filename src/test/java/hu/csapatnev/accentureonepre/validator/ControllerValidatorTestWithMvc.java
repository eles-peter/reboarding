package hu.csapatnev.accentureonepre.validator;

import hu.csapatnev.accentureonepre.service.ReboardingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerValidatorTestWithMvc {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    private void init() {
        ReboardingService reboardingService = applicationContext.getBean(ReboardingService.class);
        reboardingService.setReboardingService(
                LocalDate.now().minusDays(30),
                LocalDate.now().minusDays(15),
                LocalDate.now(),
                LocalDate.now().plusDays(15),
                LocalDate.now().plusDays(30));
    }

    @Test
    void testRegister_WithDayBeforeStep10() throws Exception {
        String testDayString = LocalDate.now().minusDays(31).toString();
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/reboarding/register").
                param("userId", "1").
                param("day", testDayString)).
                andDo(print()).
                andExpect(status().isBadRequest()).
                andReturn();
        String mvcResponse = mvcResult.getResponse().getContentAsString();
        String exceptedResponseMessage = "'register.day' " + testDayString + ": is before tracked days";
        Assertions.assertTrue(mvcResponse.contains(exceptedResponseMessage));
    }

    @Test
    void testRegister_WithDayAfterStep100() throws Exception {
        String testDayString = LocalDate.now().plusDays(31).toString();
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/reboarding/register").
                param("userId", "1").
                param("day", testDayString)).
                andDo(print()).
                andExpect(status().isBadRequest()).
                andReturn();
        String mvcResponse = mvcResult.getResponse().getContentAsString();
        String exceptedResponseMessage = "'register.day' " + testDayString + ": is after tracked days";
        Assertions.assertTrue(mvcResponse.contains(exceptedResponseMessage));
    }

    @Test
    void testRegister_WithDayBeforeToday() throws Exception {
        String testDayString = LocalDate.now().minusDays(1).toString();
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/reboarding/register").
                param("userId", "1").
                param("day", testDayString)).
                andDo(print()).
                andExpect(status().isBadRequest()).
                andReturn();
        String mvcResponse = mvcResult.getResponse().getContentAsString();
        String exceptedResponseMessage = "'register.day' " + testDayString + ": must be a date in the present or in the future";
        Assertions.assertTrue(mvcResponse.contains(exceptedResponseMessage));
    }

    @Test
    void testRegister_WithDayAccepted() throws Exception {
        String testDayString = LocalDate.now().plusDays(1).toString();
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/reboarding/register").
                param("userId", "1").
                param("day", testDayString)).
                andDo(print()).
                andExpect(status().isOk());
    }

    @Test
    void testGetStatus_WithDayBeforeStep10() throws Exception {
        String testDayString = LocalDate.now().minusDays(31).toString();
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/reboarding/status").
                param("userId", "1").
                param("day", testDayString)).
                andDo(print()).
                andExpect(status().isBadRequest()).
                andReturn();
        String mvcResponse = mvcResult.getResponse().getContentAsString();
        String exceptedResponseMessage = "'getStatus.day' " + testDayString + ": is before tracked days";
        Assertions.assertTrue(mvcResponse.contains(exceptedResponseMessage));
    }

    @Test
    void testGetStatus_WithDayAfterStep100() throws Exception {
        String testDayString = LocalDate.now().plusDays(31).toString();
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/reboarding/status").
                param("userId", "1").
                param("day", testDayString)).
                andDo(print()).
                andExpect(status().isBadRequest()).
                andReturn();
        String mvcResponse = mvcResult.getResponse().getContentAsString();
        String exceptedResponseMessage = "'getStatus.day' " + testDayString + ": is after tracked days";
        Assertions.assertTrue(mvcResponse.contains(exceptedResponseMessage));
    }

    @Test
    void testGetStatus_WithDayBeforeToday() throws Exception {
        String testDayString = LocalDate.now().minusDays(1).toString();
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/reboarding/status").
                param("userId", "1").
                param("day", testDayString)).
                andDo(print()).
                andExpect(status().isBadRequest()).
                andReturn();
        String mvcResponse = mvcResult.getResponse().getContentAsString();
        String exceptedResponseMessage = "'getStatus.day' " + testDayString + ": must be a date in the present or in the future";
        Assertions.assertTrue(mvcResponse.contains(exceptedResponseMessage));
    }

    @Test
    void testGetStatus_WithDayAccepted() throws Exception {
        String testDayString = LocalDate.now().plusDays(1).toString();
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/reboarding/status").
                param("userId", "1").
                param("day", testDayString)).
                andDo(print()).
                andExpect(status().isOk());
    }



}
