package isep.psoft.aisafe.airports.controllers;

import isep.psoft.aisafe.airports.domain.*;
import isep.psoft.aisafe.airports.services.AddCertificationUseCase;
import isep.psoft.aisafe.airports.services.AddFacilityUseCase;
import isep.psoft.aisafe.airports.services.AddPhotoUseCase;
import isep.psoft.aisafe.airports.services.GroupAirportsUseCase;
import isep.psoft.aisafe.airports.services.ListBusiestAirportsUseCase;
import isep.psoft.aisafe.airports.services.RegisterAirportUseCase;
import isep.psoft.aisafe.airports.services.SearchAirportsUseCase;
import isep.psoft.aisafe.airports.services.UpdateAirportDetailsUseCase;
import isep.psoft.aisafe.airports.services.UpdateAirportStatusUseCase;
import isep.psoft.aisafe.airports.services.ViewAirportDetailsUseCase;
import isep.psoft.aisafe.domain.shared.ModelDesignation;
import isep.psoft.aisafe.infrastructure.security.SecurityConfig;
import isep.psoft.aisafe.infrastructure.security.JwtTokenProvider;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AirportController.class)
@Import(SecurityConfig.class)
class AirportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean private RegisterAirportUseCase registerAirportUseCase;
    @MockitoBean private ViewAirportDetailsUseCase viewAirportDetailsUseCase;
    @MockitoBean private SearchAirportsUseCase searchAirportsUseCase;
    @MockitoBean private AddCertificationUseCase addCertificationUseCase;
    @MockitoBean private UpdateAirportStatusUseCase updateAirportStatusUseCase;
    @MockitoBean private AddFacilityUseCase addFacilityUseCase;
    @MockitoBean private AddPhotoUseCase addPhotoUseCase;
    @MockitoBean private UpdateAirportDetailsUseCase updateAirportDetailsUseCase;
    @MockitoBean private ListBusiestAirportsUseCase listBusiestAirportsUseCase;
    @MockitoBean private GroupAirportsUseCase groupAirportsUseCase;
    @MockitoBean private JwtTokenProvider jwtTokenProvider;
    @MockitoBean private UserDetailsService userDetailsService;

    private Airport lisbon;

    @BeforeEach
    void setUp() {
        lisbon = new Airport(
                new IATACode("LIS"),
                new AirportDetails("Humberto Delgado", "Lisbon", "Portugal",
                        "Europe", "Europe/Lisbon", new Coordinates(38.77, -9.13)),
                AirportState.OPERATIONAL,
                List.of(new Runway("03/21", 3805.0, "030/210"))
        );
    }

    // ─── US106 — POST /airports ───────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "BACKOFFICE_OPERATOR")
    void post_airport_returns_201_with_location() throws Exception {
        when(registerAirportUseCase.registerAirport(
                anyString(), anyString(), anyString(), anyString(),
                any(), anyString(), anyDouble(), anyDouble(), anyList(), any(), any()))
                .thenReturn(lisbon);

        mockMvc.perform(post("/airports")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "iataCode": "LIS",
                              "name": "Humberto Delgado",
                              "city": "Lisbon",
                              "country": "Portugal",
                              "timezone": "Europe/Lisbon",
                              "latitude": 38.77,
                              "longitude": -9.13,
                              "runways": [{"name":"03/21","length":3805.0,"orientation":"030/210"}]
                            }"""))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.iataCode").value("LIS"))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @WithMockUser(roles = "BACKOFFICE_OPERATOR")
    void post_airport_returns_409_on_duplicate_iata() throws Exception {
        when(registerAirportUseCase.registerAirport(
                anyString(), anyString(), anyString(), anyString(),
                any(), anyString(), anyDouble(), anyDouble(), anyList(), any(), any()))
                .thenThrow(new DuplicateIATACodeException("LIS"));

        mockMvc.perform(post("/airports")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "iataCode": "LIS",
                              "name": "Humberto Delgado",
                              "city": "Lisbon",
                              "country": "Portugal",
                              "timezone": "Europe/Lisbon",
                              "latitude": 38.77,
                              "longitude": -9.13,
                              "runways": [{"name":"03/21","length":3805.0,"orientation":"030/210"}]
                            }"""))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(roles = "BACKOFFICE_OPERATOR")
    void post_airport_returns_400_on_missing_required_field() throws Exception {
        mockMvc.perform(post("/airports")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"iataCode\":\"LIS\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void post_airport_returns_401_without_token() throws Exception {
        mockMvc.perform(post("/airports")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ATCC")
    void post_airport_returns_403_for_atcc_role() throws Exception {
        mockMvc.perform(post("/airports")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "iataCode": "LIS",
                              "name": "Humberto Delgado",
                              "city": "Lisbon",
                              "country": "Portugal",
                              "timezone": "Europe/Lisbon",
                              "latitude": 38.77,
                              "longitude": -9.13,
                              "runways": [{"name":"03/21","length":3805.0,"orientation":"030/210"}]
                            }"""))
                .andExpect(status().isForbidden());
    }

    // ─── US107 — GET /airports/{iataCode} ────────────────────────────────────

    @Test
    @WithMockUser(roles = "BACKOFFICE_OPERATOR")
    void get_airport_returns_200_with_hateoas_links() throws Exception {
        when(viewAirportDetailsUseCase.getAirportByIataCode("LIS")).thenReturn(lisbon);

        mockMvc.perform(get("/airports/LIS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.iataCode").value("LIS"))
                .andExpect(jsonPath("$.status").value("OPERATIONAL"))
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.update-status").exists())
                .andExpect(jsonPath("$._links.add-certification").exists());
    }

    @Test
    @WithMockUser(roles = "ATCC")
    void get_airport_atcc_role_is_authorized() throws Exception {
        when(viewAirportDetailsUseCase.getAirportByIataCode("LIS")).thenReturn(lisbon);
        mockMvc.perform(get("/airports/LIS")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "BACKOFFICE_OPERATOR")
    void get_airport_returns_404_when_not_found() throws Exception {
        when(viewAirportDetailsUseCase.getAirportByIataCode("FAO"))
                .thenThrow(new AirportNotFoundException("FAO"));

        mockMvc.perform(get("/airports/FAO")).andExpect(status().isNotFound());
    }

    @Test
    void get_airport_returns_401_without_token() throws Exception {
        mockMvc.perform(get("/airports/LIS")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "MAINTENANCE_TECHNICIAN")
    void get_airport_returns_403_for_wrong_role() throws Exception {
        mockMvc.perform(get("/airports/LIS")).andExpect(status().isForbidden());
    }

    // ─── US108 — GET /airports ────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "BACKOFFICE_OPERATOR")
    void search_returns_200_with_empty_list() throws Exception {
        when(searchAirportsUseCase.searchAirports(any(), any(), any())).thenReturn(List.of());

        mockMvc.perform(get("/airports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").doesNotExist());
    }

    @Test
    @WithMockUser(roles = "BACKOFFICE_OPERATOR")
    void search_returns_matching_airports() throws Exception {
        when(searchAirportsUseCase.searchAirports(any(), eq("Lisbon"), any()))
                .thenReturn(List.of(lisbon));

        mockMvc.perform(get("/airports").param("city", "Lisbon"))
                .andExpect(status().isOk());
    }

    // ─── US106a — POST /airports/{iataCode}/certifications ───────────────────

    @Test
    @WithMockUser(roles = "BACKOFFICE_OPERATOR")
    void post_certification_returns_200_with_updated_airport() throws Exception {
        Airport withCert = new Airport(
                new IATACode("LIS"),
                new AirportDetails("Humberto Delgado", "Lisbon", "Portugal",
                        "Europe", "Europe/Lisbon", new Coordinates(38.77, -9.13)),
                AirportState.OPERATIONAL,
                List.of(new Runway("03/21", 3805.0, "030/210"))
        );
        withCert.addCertification(new ModelDesignation("Boeing", "737-800"));

        when(addCertificationUseCase.addCertification(eq("LIS"), eq("Boeing"), eq("737-800")))
                .thenReturn(withCert);

        mockMvc.perform(post("/airports/LIS/certifications")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"manufacturer\":\"Boeing\",\"modelName\":\"737-800\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.iataCode").value("LIS"))
                .andExpect(jsonPath("$.certifiedModels[0].manufacturer").value("Boeing"));
    }

    @Test
    @WithMockUser(roles = "BACKOFFICE_OPERATOR")
    void post_certification_returns_404_when_airport_not_found() throws Exception {
        when(addCertificationUseCase.addCertification(eq("XXX"), anyString(), anyString()))
                .thenThrow(new AirportNotFoundException("XXX"));

        mockMvc.perform(post("/airports/XXX/certifications")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"manufacturer\":\"Boeing\",\"modelName\":\"737-800\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "BACKOFFICE_OPERATOR")
    void post_certification_returns_409_on_duplicate() throws Exception {
        when(addCertificationUseCase.addCertification(eq("LIS"), anyString(), anyString()))
                .thenThrow(new ModelAlreadyCertifiedException("Boeing", "737-800"));

        mockMvc.perform(post("/airports/LIS/certifications")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"manufacturer\":\"Boeing\",\"modelName\":\"737-800\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void post_certification_returns_401_without_token() throws Exception {
        mockMvc.perform(post("/airports/LIS/certifications")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"manufacturer\":\"Boeing\",\"modelName\":\"737-800\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ATCC")
    void post_certification_returns_403_for_atcc_role() throws Exception {
        mockMvc.perform(post("/airports/LIS/certifications")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"manufacturer\":\"Boeing\",\"modelName\":\"737-800\"}"))
                .andExpect(status().isForbidden());
    }

    // ─── US109 — PATCH /airports/{iataCode}/status ───────────────────────────

    @Test
    @WithMockUser(roles = "BACKOFFICE_OPERATOR")
    void patch_status_returns_200() throws Exception {
        when(updateAirportStatusUseCase.updateStatus(eq("LIS"), eq("CLOSED"))).thenReturn(lisbon);

        mockMvc.perform(patch("/airports/LIS/status")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"state\":\"CLOSED\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ATCC")
    void patch_status_returns_403_for_atcc_role() throws Exception {
        mockMvc.perform(patch("/airports/LIS/status")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"state\":\"CLOSED\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "BACKOFFICE_OPERATOR")
    void patch_status_returns_400_for_invalid_state_value() throws Exception {
        when(updateAirportStatusUseCase.updateStatus(eq("LIS"), eq("FLYING")))
                .thenThrow(new IllegalArgumentException("No enum constant isep.psoft.aisafe.airports.domain.AirportState.FLYING"));

        mockMvc.perform(patch("/airports/LIS/status")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"state\":\"FLYING\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "BACKOFFICE_OPERATOR")
    void patch_status_returns_404_when_airport_not_found() throws Exception {
        when(updateAirportStatusUseCase.updateStatus(eq("XXX"), anyString()))
                .thenThrow(new AirportNotFoundException("XXX"));

        mockMvc.perform(patch("/airports/XXX/status")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"state\":\"CLOSED\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "BACKOFFICE_OPERATOR")
    void patch_status_returns_409_on_concurrent_modification() throws Exception {
        when(updateAirportStatusUseCase.updateStatus(eq("LIS"), anyString()))
                .thenThrow(new ObjectOptimisticLockingFailureException(Object.class, "LIS"));

        mockMvc.perform(patch("/airports/LIS/status")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"state\":\"CLOSED\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(roles = "ATCC")
    void search_with_atcc_role_returns_200() throws Exception {
        when(searchAirportsUseCase.searchAirports(any(), any(), any())).thenReturn(List.of(lisbon));

        mockMvc.perform(get("/airports").param("city", "Lisbon"))
                .andExpect(status().isOk());
    }
}
