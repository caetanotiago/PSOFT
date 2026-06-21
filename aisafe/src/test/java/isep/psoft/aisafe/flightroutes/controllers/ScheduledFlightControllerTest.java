package isep.psoft.aisafe.flightroutes.controllers;

import isep.psoft.aisafe.flightroutes.assemblers.ScheduledFlightAssembler;
import isep.psoft.aisafe.flightroutes.domain.RouteRequirementsNotMetException;
import isep.psoft.aisafe.flightroutes.domain.ScheduledFlight;
import isep.psoft.aisafe.flightroutes.dto.ScheduledFlightDTO;
import isep.psoft.aisafe.flightroutes.services.CreateScheduledFlightService;
import isep.psoft.aisafe.flightroutes.services.ViewScheduledFlightsByAircraftService;
import isep.psoft.aisafe.infrastructure.security.JwtTokenProvider;
import isep.psoft.aisafe.infrastructure.security.SecurityConfig;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScheduledFlightController.class)
@Import(SecurityConfig.class)
class ScheduledFlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean private CreateScheduledFlightService createService;
    @MockitoBean private ViewScheduledFlightsByAircraftService viewService;
    @MockitoBean private ScheduledFlightAssembler assembler;
    @MockitoBean private JwtTokenProvider jwtTokenProvider;
    @MockitoBean private UserDetailsService userDetailsService;

    private static final String VALID_BODY = """
            { "aircraftRegistration":"CS-TVA", "routeID":"route-1", "date":"2026-07-01", "time":"10:00:00" }""";

    private ScheduledFlightDTO sampleDTO() {
        ScheduledFlightDTO dto = new ScheduledFlightDTO();
        dto.setId("sf-1");
        dto.setAircraftRegistration("CS-TVA");
        dto.setRouteID("route-1");
        dto.setStatus("SCHEDULED");
        return dto;
    }

    // ─── US212 — POST /api/scheduled-flights ──────────────────────────────────

    @Test
    @WithMockUser(roles = "ATCC")
    void post_returns_201_with_location_and_body() throws Exception {
        ScheduledFlight flight = mock(ScheduledFlight.class);
        when(flight.getId()).thenReturn("sf-1");
        when(createService.create(any())).thenReturn(flight);
        when(assembler.toModel(any())).thenReturn(sampleDTO());

        mockMvc.perform(post("/api/scheduled-flights")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value("sf-1"))
                .andExpect(jsonPath("$.aircraftRegistration").value("CS-TVA"));
    }

    @Test
    @WithMockUser(roles = "ATCC")
    void post_returns_404_when_aircraft_or_route_missing() throws Exception {
        when(createService.create(any())).thenThrow(new EntityNotFoundException("Aircraft not found"));

        mockMvc.perform(post("/api/scheduled-flights")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ATCC")
    void post_returns_422_when_requirements_not_met() throws Exception {
        when(createService.create(any()))
                .thenThrow(new RouteRequirementsNotMetException("range/capacity"));

        mockMvc.perform(post("/api/scheduled-flights")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isUnprocessableContent());
    }

    @Test
    @WithMockUser(roles = "ATCC")
    void post_returns_409_on_conflict() throws Exception {
        when(createService.create(any()))
                .thenThrow(new IllegalStateException("Aircraft already has a flight at that time"));

        mockMvc.perform(post("/api/scheduled-flights")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(roles = "ATCC")
    void post_returns_400_on_invalid_body() throws Exception {
        mockMvc.perform(post("/api/scheduled-flights")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void post_returns_401_without_token() throws Exception {
        mockMvc.perform(post("/api/scheduled-flights")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "BACKOFFICE_OPERATOR")
    void post_returns_403_for_non_atcc_role() throws Exception {
        mockMvc.perform(post("/api/scheduled-flights")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isForbidden());
    }

    // ─── US213 — GET /api/scheduled-flights?aircraft= ─────────────────────────

    @Test
    @WithMockUser(roles = "ATCC")
    void get_by_aircraft_returns_200_with_page() throws Exception {
        Page<ScheduledFlight> page = new PageImpl<>(
                List.of(mock(ScheduledFlight.class)), PageRequest.of(0, 20), 1);
        when(viewService.viewByAircraft(eq("CS-TVA"), any())).thenReturn(page);
        when(assembler.toModel(any())).thenReturn(sampleDTO());

        mockMvc.perform(get("/api/scheduled-flights").param("aircraft", "CS-TVA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements").value(1));
    }

    @Test
    @WithMockUser(roles = "ATCC")
    void get_by_aircraft_returns_200_with_empty_page() throws Exception {
        Page<ScheduledFlight> page = new PageImpl<>(List.of(), PageRequest.of(0, 20), 0);
        when(viewService.viewByAircraft(eq("CS-TVA"), any())).thenReturn(page);

        mockMvc.perform(get("/api/scheduled-flights").param("aircraft", "CS-TVA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements").value(0));
    }

    @Test
    @WithMockUser(roles = "ATCC")
    void get_by_aircraft_returns_404_when_aircraft_missing() throws Exception {
        when(viewService.viewByAircraft(eq("XX-XXX"), any()))
                .thenThrow(new EntityNotFoundException("Aircraft not found"));

        mockMvc.perform(get("/api/scheduled-flights").param("aircraft", "XX-XXX"))
                .andExpect(status().isNotFound());
    }

    @Test
    void get_by_aircraft_returns_401_without_token() throws Exception {
        mockMvc.perform(get("/api/scheduled-flights").param("aircraft", "CS-TVA"))
                .andExpect(status().isUnauthorized());
    }

    // ─── GET /api/scheduled-flights/{id} ──────────────────────────────────────

    @Test
    @WithMockUser(roles = "ATCC")
    void get_by_id_returns_200() throws Exception {
        when(viewService.getById("sf-1")).thenReturn(mock(ScheduledFlight.class));
        when(assembler.toModel(any())).thenReturn(sampleDTO());

        mockMvc.perform(get("/api/scheduled-flights/sf-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("sf-1"));
    }

    @Test
    @WithMockUser(roles = "ATCC")
    void get_by_id_returns_404_when_missing() throws Exception {
        when(viewService.getById("missing"))
                .thenThrow(new EntityNotFoundException("Scheduled flight not found"));

        mockMvc.perform(get("/api/scheduled-flights/missing"))
                .andExpect(status().isNotFound());
    }
}
