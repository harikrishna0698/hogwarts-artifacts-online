package edu.tcu.cs.hogwarts_artifacts_online.wizard;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.hogwarts_artifacts_online.artifact.ArtifactController;
import edu.tcu.cs.hogwarts_artifacts_online.artifact.ArtifactNotFoundException;
import edu.tcu.cs.hogwarts_artifacts_online.system.Result;
import edu.tcu.cs.hogwarts_artifacts_online.system.StatusCode;
import edu.tcu.cs.hogwarts_artifacts_online.wizard.converter.WizardDtoToWizardConverter;
import edu.tcu.cs.hogwarts_artifacts_online.wizard.converter.WizardToWizardDtoConverter;
import edu.tcu.cs.hogwarts_artifacts_online.wizard.dto.WizardDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WizardControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    WizardService wizardService;
    @Autowired
    ObjectMapper objectMapper;
    @Mock
    WizardToWizardDtoConverter wizardToWizardDtoConverter;
    @Mock
    WizardDtoToWizardConverter wizardDtoToWizardConverter;
    @InjectMocks
    WizardController wizardController;

    List<Wizard> wizardList;

    @BeforeEach
    void setUp() {
        this.wizardList = new ArrayList<>();

        Wizard w1 = new Wizard();
        w1.setId(101);
        w1.setName("Harry Potter");
        this.wizardList.add(w1);

        Wizard w2 = new Wizard();
        w2.setId(102);
        w2.setName("Albus Dumbledore");
        this.wizardList.add(w2);

        Wizard w3 = new Wizard();
        w3.setId(103);
        w3.setName("Professor Snape");
        this.wizardList.add(w3);

        given(this.wizardToWizardDtoConverter.convert(w1)).willReturn(
                new WizardDto(101,"Harry Potter",2)
        );

        given(this.wizardToWizardDtoConverter.convert(w2)).willReturn(
                new WizardDto(102,"Albus Dumbledore",2)
        );
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindAllWizardsSuccesss() throws Exception{
        //Given
        given(this.wizardService.findAll()).willReturn(this.wizardList);

        //When
        this.mockMvc.perform(get("/api/v1/wizards").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find all success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.wizardList.size())))
                .andExpect(jsonPath("$.data[0].id").value(101))
                .andExpect(jsonPath("$.data[0].name").value("Harry Potter"))
                .andExpect(jsonPath("$.data[1].id").value(102))
                .andExpect(jsonPath("$.data[1].name").value("Albus Dumbledore"));
    }

    @Test
    void testFindWizardByIdSuccess() throws Exception{
        //Given
        WizardDto wizardDto = new WizardDto(101,"Harry Potter",2);
        given(this.wizardService.findById(101)).willReturn(this.wizardList.get(0));
        given(this.wizardToWizardDtoConverter.convert(this.wizardList.get(0))).willReturn(wizardDto);

        //When and then
        this.mockMvc.perform(get("/api/v1/wizards/{wizardId}",101)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find one success"))
                .andExpect(jsonPath("$.data.id").value(101))
                .andExpect(jsonPath("$.data.name").value("Harry Potter"));

    }

    @Test
    void testAddWizardSuccess() throws Exception{
        //Given
        WizardDto wizardDto = new WizardDto(101,"Harry Potter",2);
        String json = this.objectMapper.writeValueAsString(wizardDto);

        Wizard savedWizard = new Wizard();
        savedWizard.setId(101);
        savedWizard.setName("Harry Potter");

        WizardDto savedWizardDto = new WizardDto(101, "Harry Potter",0);

        given(this.wizardDtoToWizardConverter.convert(wizardDto)).willReturn(savedWizard);
        given(this.wizardService.save(savedWizard)).willReturn(savedWizard);
        given(this.wizardToWizardDtoConverter.convert(savedWizard)).willReturn(savedWizardDto);

        //When
        this.mockMvc.perform(post("/api/v1/wizards").contentType(MediaType.APPLICATION_JSON)
                .content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(savedWizard.getName()));
    }


    @Test
    void testUpdateWizardSuccess() throws Exception{
        //Given
        WizardDto wizardDto = new WizardDto(101,"Harry Potter",0);
        String json = this.objectMapper.writeValueAsString(wizardDto);

        Wizard updatedWizard = new Wizard();
        updatedWizard.setId(101);
        updatedWizard.setName("Harry James Potter");

        WizardDto savedWizardDto = new WizardDto(101,"Harry James Potter",0);

        given(this.wizardDtoToWizardConverter.convert(wizardDto)).willReturn(updatedWizard);
        given(this.wizardService.update(eq(101), Mockito.any(Wizard.class))).willReturn(updatedWizard);
        given(this.wizardToWizardDtoConverter.convert(updatedWizard)).willReturn(savedWizardDto);

        //When
        this.mockMvc.perform(put("/api/v1/wizards/101").contentType(MediaType.APPLICATION_JSON)
                        .content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update success"))
                .andExpect(jsonPath("$.data.id").value(101))
                .andExpect(jsonPath("$.data.name").value(updatedWizard.getName())) ;
    }

    @Test
    void testUpdateWizardErrorWithNonExistentId() throws Exception{
        //Given
        WizardDto wizardDto = new WizardDto(101,"Harry Potter",0);
        String json = this.objectMapper.writeValueAsString(wizardDto);

        given(this.wizardService.update(eq(101),Mockito.any(Wizard.class)))
                .willThrow(new WizardNotFoundException(101));

        this.mockMvc.perform(put("/api/v1/wizards/101").contentType(MediaType.APPLICATION_JSON)
                        .content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("could not find wizard with Id: 101"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteArtifactSuccess() throws Exception {
        //Given
        doNothing().when(this.wizardService).delete(101);

        //When
        this.mockMvc.perform(delete("/api/v1/wizards/101").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete success"))
                .andExpect(jsonPath("$.data").isEmpty());

        //Then
    }

    @Test
    void testDeleteArtifactErrorWithNonExistentId() throws Exception {
        //Given
        doThrow(new WizardNotFoundException(101)).when(this.wizardService).delete(101);

        //When
        this.mockMvc.perform(delete("/api/v1/wizards/101").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("could not find wizard with Id: 101"))
                .andExpect(jsonPath("$.data").isEmpty());

        //Then
    }
}