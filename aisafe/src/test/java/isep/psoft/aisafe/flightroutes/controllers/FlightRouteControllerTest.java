package isep.psoft.aisafe.flightroutes.controllers;

import isep.psoft.aisafe.flightroutes.assemblers.FlightRouteAssembler;
import isep.psoft.aisafe.flightroutes.assemblers.ItineraryAssembler;
import isep.psoft.aisafe.flightroutes.domain.FlightRoute;
import isep.psoft.aisafe.flightroutes.domain.Itinerary;
import isep.psoft.aisafe.flightroutes.domain.RouteUsage;
import isep.psoft.aisafe.flightroutes.dto.FlightRouteDTO;
import isep.psoft.aisafe.flightroutes.dto.ItineraryDTO;
import isep.psoft.aisafe.flightroutes.dto.NetworkDistanceDTO;
import isep.psoft.aisafe.flightroutes.dto.RouteHistoryDTO;
import isep.psoft.aisafe.flightroutes.services.CalculateNetworkDistanceService;
import isep.psoft.aisafe.flightroutes.services.CreateFlightRouteService;
import isep.psoft.aisafe.flightroutes.services.GetRouteHistoryService;
import isep.psoft.aisafe.flightroutes.services.ListActiveRoutesService;
import isep.psoft.aisafe.flightroutes.services.SearchAlternativeRoutesService;
import isep.psoft.aisafe.flightroutes.services.SearchFlightRoutesService;
import isep.psoft.aisafe.flightroutes.services.UpdateFlightRouteService;
import isep.psoft.aisafe.flightroutes.services.ViewRoutesByAirportService;
import isep.psoft.aisafe.infrastructure.security.JwtTokenProvider;
import isep.psoft.aisafe.infrastructure.security.SecurityConfig;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FlightRouteController.class)
@Import(SecurityConfig.class)
class FlightRouteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Colaboradores Phase 1 (testados)
    @MockitoBean private CreateFlightRouteService createService;
    @MockitoBean private UpdateFlightRouteService updateService;
    @MockitoBean private GetRouteHistoryService historyService;
    @MockitoBean private SearchFlightRoutesService searchService;
    @MockitoBean private FlightRouteAssembler assembler;

    // Colaboradores WP#3B — apenas mockados para o contexto arrancar (testados na Phase 2)
    @MockitoBean private ListActiveRoutesService listActiveRoutesService;
    @MockitoBean private CalculateNetworkDistanceService networkDistanceService;
    @MockitoBean private SearchAlternativeRoutesService searchAlternativeRoutesService;
    @MockitoBean private ItineraryAssembler itineraryAssembler;

    // WP#2B (US209)
    @MockitoBean private ViewRoutesByAirportService viewRoutesByAirportService;

    // Exigidos pelo JwtAuthenticationFilter que a slice MVC instancia
    @MockitoBean private JwtTokenProvider jwtTokenProvider;
    @MockitoBean private UserDetailsService userDetailsService;

    private static final String VALID_BODY = """
            { "originIATA":"LIS", "destIATA":"OPO", "minRange":500.0, "minCapacity":150, "estimatedFlightTime":50 }""";

    private FlightRouteDTO sampleDTO() {
        FlightRouteDTO dto = new FlightRouteDTO();
        dto.setId("route-123");
        dto.setOriginIATA("LIS");
        dto.setDestIATA("OPO");
        dto.setStatus("ACTIVE");
        return dto;
    }

    // ─── US110 — POST /api/routes ─────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "ATCC")
    void post_route_returns_201_with_body() throws Exception {
        when(createService.createRoute(any())).thenReturn(mock(FlightRoute.class));
        when(assembler.toDTO(any())).thenReturn(sampleDTO());

        mockMvc.perform(post("/api/routes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("route-123"))
                .andExpect(jsonPath("$.originIATA").value("LIS"));
    }

    @Test
    @WithMockUser(roles = "ATCC")
    void post_route_returns_400_on_invalid_body() throws Exception {
        // Falta o originIATA (e outros campos obrigatórios) → falha de validação @Valid
        mockMvc.perform(post("/api/routes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"destIATA\":\"OPO\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void post_route_returns_401_without_token() throws Exception {
        mockMvc.perform(post("/api/routes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "BACKOFFICE_OPERATOR")
    void post_route_returns_403_for_non_atcc_role() throws Exception {
        // POST /api/routes é exclusivo do ATCC
        mockMvc.perform(post("/api/routes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isForbidden());
    }

    // ─── US112 — PATCH /api/routes/{id} ───────────────────────────────────────

    @Test
    @WithMockUser(roles = "ATCC")
    void patch_route_returns_200() throws Exception {
        when(updateService.updateRoute(eq("route-123"), any())).thenReturn(mock(FlightRoute.class));
        when(assembler.toDTO(any())).thenReturn(sampleDTO());

        mockMvc.perform(patch("/api/routes/route-123")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"minCapacity\":180}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("route-123"));
    }

    @Test
    @WithMockUser(roles = "ATCC")
    void patch_route_returns_409_on_same_status() throws Exception {
        when(updateService.updateRoute(eq("route-123"), any()))
                .thenThrow(new IllegalStateException("Route is already ACTIVE."));

        mockMvc.perform(patch("/api/routes/route-123")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"ACTIVE\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(roles = "ATCC")
    void patch_route_returns_400_on_invalid_status() throws Exception {
        when(updateService.updateRoute(eq("route-123"), any()))
                .thenThrow(new IllegalArgumentException("Invalid status: FOO"));

        mockMvc.perform(patch("/api/routes/route-123")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"FOO\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ATCC")
    void patch_route_returns_404_when_not_found() throws Exception {
        when(updateService.updateRoute(eq("missing"), any()))
                .thenThrow(new EntityNotFoundException("Flight Route not found: missing"));

        mockMvc.perform(patch("/api/routes/missing")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"minCapacity\":180}"))
                .andExpect(status().isNotFound());
    }

    // ─── US113 — GET /api/routes/{id} ─────────────────────────────────────────

    @Test
    @WithMockUser(roles = "ATCC")
    void get_route_by_id_returns_200() throws Exception {
        when(searchService.getRouteById("route-123")).thenReturn(mock(FlightRoute.class));
        when(assembler.toDTO(any())).thenReturn(sampleDTO());

        mockMvc.perform(get("/api/routes/route-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("route-123"));
    }

    @Test
    void get_route_by_id_returns_401_without_token() throws Exception {
        mockMvc.perform(get("/api/routes/route-123"))
                .andExpect(status().isUnauthorized());
    }

    // ─── US111 — GET /api/routes/{id}/history ─────────────────────────────────

    @Test
    @WithMockUser(roles = "ATCC")
    void get_route_history_returns_200_with_list() throws Exception {
        RouteHistoryDTO created = new RouteHistoryDTO();
        created.setChangeDate("2026-06-16T10:00:00");
        created.setDescription("Route created.");
        RouteHistoryDTO updated = new RouteHistoryDTO();
        updated.setChangeDate("2026-06-16T10:05:00");
        updated.setDescription("Route details updated.");
        updated.setPreviousMinCapacity(150);
        updated.setNewMinCapacity(200);

        when(historyService.getRouteHistory("route-123")).thenReturn(List.of());
        when(assembler.toHistoryDTOList(any())).thenReturn(List.of(created, updated));

        mockMvc.perform(get("/api/routes/route-123/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Route created."))
                .andExpect(jsonPath("$[1].previousMinCapacity").value(150))
                .andExpect(jsonPath("$[1].newMinCapacity").value(200));
    }

    @Test
    @WithMockUser(roles = "ATCC")
    void get_route_history_returns_404_when_route_not_found() throws Exception {
        when(historyService.getRouteHistory("missing"))
                .thenThrow(new EntityNotFoundException("Flight Route not found: missing"));

        mockMvc.perform(get("/api/routes/missing/history"))
                .andExpect(status().isNotFound());
    }

    // ─── US114 — GET /api/routes/search ───────────────────────────────────────

    @Test
    @WithMockUser(roles = "ATCC")
    void search_routes_returns_200() throws Exception {
        when(searchService.searchRoutes(anyString(), anyString())).thenReturn(List.of(mock(FlightRoute.class)));
        when(assembler.toDTOList(any())).thenReturn(List.of(sampleDTO()));

        mockMvc.perform(get("/api/routes/search").param("origin", "LIS").param("dest", "OPO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("route-123"));
    }

    @Test
    @WithMockUser(roles = "MAINTENANCE_TECHNICIAN")
    void search_routes_returns_403_for_wrong_role() throws Exception {
        mockMvc.perform(get("/api/routes/search"))
                .andExpect(status().isForbidden());
    }

    // ─── US214 — GET /api/routes/active ───────────────────────────────────────

    @Test
    @WithMockUser(roles = "ATCC")
    void list_active_routes_returns_200() throws Exception {
        FlightRouteDTO dto = sampleDTO();
        dto.setUsageCount(3L);
        when(listActiveRoutesService.listActiveRoutes("popularity"))
                .thenReturn(List.of(mock(RouteUsage.class)));
        when(assembler.toDTOWithUsageList(any())).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/routes/active").param("sortBy", "popularity"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usageCount").value(3));
    }

    @Test
    @WithMockUser(roles = "ATCC")
    void list_active_routes_uses_default_sort_when_no_param() throws Exception {
        when(listActiveRoutesService.listActiveRoutes("popularity"))
                .thenReturn(List.of(mock(RouteUsage.class)));
        when(assembler.toDTOWithUsageList(any())).thenReturn(List.of(sampleDTO()));

        mockMvc.perform(get("/api/routes/active"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ATCC")
    void list_active_routes_returns_400_on_invalid_sortBy() throws Exception {
        when(listActiveRoutesService.listActiveRoutes("xpto"))
                .thenThrow(new IllegalArgumentException("Invalid sortBy value"));

        mockMvc.perform(get("/api/routes/active").param("sortBy", "xpto"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void list_active_routes_returns_401_without_token() throws Exception {
        mockMvc.perform(get("/api/routes/active"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "MAINTENANCE_TECHNICIAN")
    void list_active_routes_returns_403_for_wrong_role() throws Exception {
        mockMvc.perform(get("/api/routes/active"))
                .andExpect(status().isForbidden());
    }

    // ─── US215 — GET /api/routes/network/total-distance ───────────────────────

    @Test
    @WithMockUser(roles = "ATCC")
    void network_total_distance_returns_200() throws Exception {
        when(networkDistanceService.calculateTotalDistance())
                .thenReturn(new NetworkDistanceDTO(1012.0));

        mockMvc.perform(get("/api/routes/network/total-distance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalDistanceKm").value(1012.0));
    }

    // ─── US216 — GET /api/routes/alternatives ─────────────────────────────────

    @Test
    @WithMockUser(roles = "ATCC")
    void alternatives_returns_200_with_list() throws Exception {
        ItineraryDTO itinerary = new ItineraryDTO();
        itinerary.setNumberOfStops(1);
        itinerary.setTotalDistance(734.0);
        when(searchAlternativeRoutesService.searchAlternatives("LIS", "MAD"))
                .thenReturn(List.of(mock(Itinerary.class)));
        when(itineraryAssembler.toDTOList(any())).thenReturn(List.of(itinerary));

        mockMvc.perform(get("/api/routes/alternatives").param("origin", "LIS").param("dest", "MAD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numberOfStops").value(1))
                .andExpect(jsonPath("$[0].totalDistance").value(734.0));
    }

    @Test
    @WithMockUser(roles = "ATCC")
    void alternatives_returns_404_when_airport_missing() throws Exception {
        when(searchAlternativeRoutesService.searchAlternatives(anyString(), anyString()))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Airport not found"));

        mockMvc.perform(get("/api/routes/alternatives").param("origin", "XXX").param("dest", "MAD"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ATCC")
    void alternatives_returns_400_when_required_params_missing() throws Exception {
        mockMvc.perform(get("/api/routes/alternatives").param("origin", "LIS"))
                .andExpect(status().isBadRequest());
    }
}
