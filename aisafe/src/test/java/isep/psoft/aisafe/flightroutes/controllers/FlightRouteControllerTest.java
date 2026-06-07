/*package isep.psoft.aisafe.flightroutes.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import isep.psoft.aisafe.flightroutes.assemblers.FlightRouteAssembler;
import isep.psoft.aisafe.flightroutes.domain.FlightRoute;
import isep.psoft.aisafe.flightroutes.dto.CreateRouteDTO;
import isep.psoft.aisafe.flightroutes.dto.FlightRouteDTO;
import isep.psoft.aisafe.flightroutes.services.*;
import isep.psoft.aisafe.infrastructure.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean; 
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    controllers = FlightRouteController.class,
    excludeAutoConfiguration = {SecurityAutoConfiguration.class},
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
)
class FlightRouteControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    // NOVO PADRÃO NO SPRING BOOT 4: @MockitoBean
    @MockitoBean private CreateFlightRouteService createService;
    @MockitoBean private UpdateFlightRouteService updateService;
    @MockitoBean private GetRouteHistoryService historyService;
    @MockitoBean private SearchFlightRoutesService searchService;
    @MockitoBean private FlightRouteAssembler assembler;

    @Test
    void ensureCreateRouteReturns201Created() throws Exception {
        // Arrange
        CreateRouteDTO requestDto = new CreateRouteDTO();
        requestDto.setOriginIATA("LIS");
        requestDto.setDestIATA("OPO");
        requestDto.setMinRange(300.0);
        requestDto.setMinCapacity(100);
        requestDto.setEstimatedFlightTime(50);

        FlightRouteDTO responseDto = new FlightRouteDTO();
        responseDto.setId("route-123");
        responseDto.setOriginIATA("LIS");
        responseDto.setDestIATA("OPO");

        when(createService.createRoute(any(CreateRouteDTO.class))).thenReturn(null);
        when(assembler.toDTO(any())).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/api/routes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated()) 
                .andExpect(jsonPath("$.id").value("route-123"))
                .andExpect(jsonPath("$.originIATA").value("LIS"));
    }

    @Test
    void ensureGetRouteByIdReturns200Ok() throws Exception {
        // Arrange
        FlightRouteDTO responseDto = new FlightRouteDTO();
        responseDto.setId("route-123");
        responseDto.setOriginIATA("LIS");

        when(searchService.getRouteById("route-123")).thenReturn(null);
        when(assembler.toDTO(any())).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(get("/api/routes/route-123")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$.id").value("route-123"));
    }
}*/