/*package isep.psoft.aisafe.aircraftmanagement.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AircraftRestController.class)
class AircraftRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean // Mocks dos teus serviços para não dependeres da BD aqui
    private ViewAircraftService viewAircraftService;
    // ... outros mocks

    @Test
    void ensureGetAircraftReturns404IfNotFound() throws Exception {
        mockMvc.perform(get("/api/aircrafts/NON-EXISTENT"))
               .andExpect(status().isNotFound());
    }
}*/