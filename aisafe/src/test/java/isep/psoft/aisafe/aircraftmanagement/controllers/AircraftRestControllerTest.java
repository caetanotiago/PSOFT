package isep.psoft.aisafe.aircraftmanagement.controllers;

import isep.psoft.aisafe.aircraftmanagement.assemblers.AircraftAssembler;
import isep.psoft.aisafe.aircraftmanagement.domain.AircraftNotFoundException;
import isep.psoft.aisafe.aircraftmanagement.services.CalculateOperationalHoursService;
import isep.psoft.aisafe.aircraftmanagement.services.CreateAircraftService;
import isep.psoft.aisafe.aircraftmanagement.services.SearchAircraftService;
import isep.psoft.aisafe.aircraftmanagement.services.UpdateAircraftStatusService;
import isep.psoft.aisafe.aircraftmanagement.services.ViewAircraftService;
import isep.psoft.aisafe.aircraftmanagement.services.ViewAircraftStatusService;
import isep.psoft.aisafe.aircraftmanagement.services.ViewCompatibleRoutesService;
import isep.psoft.aisafe.infrastructure.security.JwtTokenProvider;
import isep.psoft.aisafe.infrastructure.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AircraftRestController.class)
@Import(SecurityConfig.class)
class AircraftRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean private CreateAircraftService createAircraftService;
    @MockitoBean private ViewAircraftService viewAircraftService;
    @MockitoBean private SearchAircraftService searchAircraftService;
    @MockitoBean private UpdateAircraftStatusService updateAircraftStatusService;
    @MockitoBean private ViewAircraftStatusService viewAircraftStatusService;
    @MockitoBean private ViewCompatibleRoutesService viewCompatibleRoutesService;
    @MockitoBean private CalculateOperationalHoursService calculateOperationalHoursService;
    @MockitoBean private AircraftAssembler assembler;

    @MockitoBean private JwtTokenProvider jwtTokenProvider;
    @MockitoBean private UserDetailsService userDetailsService;

    @Test
    @WithMockUser(roles = "ATCC")
    void ensureGetAircraftReturns404IfNotFound() throws Exception {
        when(viewAircraftService.getAircraftByRegistrationNumber("NON-EXISTENT"))
                .thenThrow(new AircraftNotFoundException("NON-EXISTENT"));

        mockMvc.perform(get("/api/aircrafts/NON-EXISTENT"))
               .andExpect(status().isNotFound());
    }
}
