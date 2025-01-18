package edu.tcu.cs.hogwarts_artifacts_online.artifact;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.hogwarts_artifacts_online.artifact.converter.ArtfiactToArtifactDtoConverter;
import edu.tcu.cs.hogwarts_artifacts_online.artifact.converter.ArtifactDtoToArtifactConverter;
import edu.tcu.cs.hogwarts_artifacts_online.artifact.dto.ArtifactDto;
import edu.tcu.cs.hogwarts_artifacts_online.system.StatusCode;
import edu.tcu.cs.hogwarts_artifacts_online.wizard.converter.WizardToWizardDtoConverter;
import edu.tcu.cs.hogwarts_artifacts_online.wizard.dto.WizardDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class ArtifactControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    ArtifactService artifactService;
    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    WizardToWizardDtoConverter wizardToWizardDtoConverter;
    @MockitoBean
    private ArtfiactToArtifactDtoConverter artifactToArtifactDtoConverter;

    @Mock
    private ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter;

    @InjectMocks
    ArtifactController artifactController;

    List<Artifact> artifacts;


    @BeforeEach
    void setUp() {
        this.artifacts = new ArrayList<>();

        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles");
        a1.setImageUrl("ImageUrl");
        this.artifacts.add(a1);

        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        this.artifacts.add(a2);

        Artifact a5 = new Artifact();
        a5.setId("1250808601744904195");
        a5.setName("The Sword Of Gryffindor");
        a5.setDescription("A goblin-made sword adorned with large rubies on the pommel. It was o");
        a5.setImageUrl("ImageUrl");
        this.artifacts.add(a5);

        Artifact a6 = new Artifact();
        a6.setId("1250808601744904196");
        a6.setName("Resurrection Stone");
        a6.setDescription("The Resurrection Stone allows the holder to bring back deceased love ");
        a6.setImageUrl("ImageUrl");
        this.artifacts.add(a6);

        given(this.artifactToArtifactDtoConverter.convert(a1)).willReturn(
                new ArtifactDto("1250808601744904191", "Deluminator", a1.getDescription(), a1.getImageUrl(), null)
        );

        given(this.artifactToArtifactDtoConverter.convert(a2)).willReturn(
                new ArtifactDto("1250808601744904192", "Invisibility Cloak", a2.getDescription(), a2.getImageUrl(), null)
        );
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindArtifactByIdSuccess() throws Exception {
        // Given
        WizardDto owner = new WizardDto(1, "Albus Dumbledore",1);
        ArtifactDto artifactDto = new ArtifactDto("1250808601744904191", "Deluminator", "A Deluminator is a device invented by Albus Dumbledore that resembles", "ImageUrl",null);
        given(this.artifactService.findById("1250808601744904191")).willReturn(this.artifacts.get(0));
        given(this.artifactToArtifactDtoConverter.convert(this.artifacts.get(0))).willReturn(artifactDto);

        // When and then
        this.mockMvc.perform(get("/api/v1/artifacts/{artifactId}", "1250808601744904191")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find one Success"))
                .andExpect(jsonPath("$.data.id").value("1250808601744904191"))
                .andExpect(jsonPath("$.data.name").value("Deluminator"));
    }

    @Test
    void testFindArtifactByIdNotFound() throws Exception {
        // Given
        given(this.artifactService.findById("1250808601744904191")).willThrow(new ArtifactNotFoundException("1250808601744904191"));

        // When and then
        this.mockMvc.perform(get("/api/v1/artifacts/{artifactId}", "1250808601744904191")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("could not find artifact with Id: 1250808601744904191"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testFindAllArtifactsSuccess() throws Exception {
        //Given
        given(this.artifactService.findAll()).willReturn(this.artifacts);

        //When
        this.mockMvc.perform(get("/api/v1/artifacts").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.artifacts.size())))
                .andExpect(jsonPath("$.data[0].id").value("1250808601744904191"))
                .andExpect(jsonPath("$.data[0].name").value("Deluminator"))
                .andExpect(jsonPath("$.data[1].id").value("1250808601744904192"))
                .andExpect(jsonPath("$.data[1].name").value("Invisibility Cloak"));
    }

    @Test
    void testAddArtifactSuccess() throws Exception {
            //Given
            ArtifactDto artifactDto = new ArtifactDto(null,"Remembrall",
                                                      "A Remembrall was a magical large marble-sized glass ball",
                                                        "ImageUrl",null);
            String json = this.objectMapper.writeValueAsString(artifactDto);

            Artifact savedArtifact = new Artifact();
            savedArtifact.setId("1250808601744904197");
            savedArtifact.setName("Remembrall");
            savedArtifact.setDescription("A Remembrall was a magical large marble-sized glass ball");
            savedArtifact.setImageUrl("ImageUrl");

            ArtifactDto savedArtifactDto = new ArtifactDto(
                "1250808601744904197",
                "Remembrall",
                "A Remembrall was a magical large marble-sized glass ball",
                "ImageUrl",
                null
        );

        given(this.artifactDtoToArtifactConverter.convert(artifactDto)).willReturn(savedArtifact);
        given(this.artifactService.save(savedArtifact)).willReturn(savedArtifact);
        given(this.artifactToArtifactDtoConverter.convert(savedArtifact)).willReturn(savedArtifactDto);


        //When
        this.mockMvc.perform(post("/api/v1/artifacts").contentType(MediaType.APPLICATION_JSON)
                .content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(savedArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(savedArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(savedArtifact.getImageUrl()));

        //Then
    }

    @Test
    void testUpdateArtifactSuccess() throws Exception {
        //Given
        ArtifactDto artifactDto = new ArtifactDto("1250808601744904192","Invisibility Cloak",
                "A new description",
                "ImageUrl",null);
        String json = this.objectMapper.writeValueAsString(artifactDto);

        Artifact updatedArtifact = new Artifact();
        updatedArtifact.setId("1250808601744904192");
        updatedArtifact.setName("Invisibility Cloak");
        updatedArtifact.setDescription("A new description");
        updatedArtifact.setImageUrl("ImageUrl");

        ArtifactDto savedArtifactDto = new ArtifactDto(
                "1250808601744904192",
                "Invisibility Cloak",
                "A new description",
                "ImageUrl",
                null
        );

        given(this.artifactDtoToArtifactConverter.convert(artifactDto)).willReturn(updatedArtifact);
        given(this.artifactService.update(eq("1250808601744904192"),Mockito.any(Artifact.class))).willReturn(updatedArtifact);
        given(this.artifactToArtifactDtoConverter.convert(updatedArtifact)).willReturn(savedArtifactDto);


        //When
        this.mockMvc.perform(put("/api/v1/artifacts/1250808601744904192").contentType(MediaType.APPLICATION_JSON)
                        .content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update success"))
                .andExpect(jsonPath("$.data.id").value("1250808601744904192"))
                .andExpect(jsonPath("$.data.name").value(updatedArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(updatedArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(updatedArtifact.getImageUrl()));
    }

    @Test
    void testUpdateArtifactErrorWithNonExistentId() throws Exception {
        //Given
        ArtifactDto artifactDto = new ArtifactDto("1250808601744904192","Invisibility Cloak",
                "A new description",
                "ImageUrl",null);
        String json = this.objectMapper.writeValueAsString(artifactDto);


        //given(this.artifactDtoToArtifactConverter.convert(artifactDto)).willThrow(new ArtifactNotFoundException("1250808601744904192"));
        //Artifact artifact = new Artifact();
        //Mockito.when(artifactDtoToArtifactConverter.convert(Mockito.any(ArtifactDto.class))).thenReturn(artifact);
        given(this.artifactService.update(eq("1250808601744904192"),Mockito.any(Artifact.class)))
                .willThrow(new ArtifactNotFoundException("1250808601744904192"));
        //given(this.artifactToArtifactDtoConverter.convert(updatedArtifact)).willReturn(savedArtifactDto);


        //When
        this.mockMvc.perform(put("/api/v1/artifacts/1250808601744904192").contentType(MediaType.APPLICATION_JSON)
                        .content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("could not find artifact with Id: 1250808601744904192"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteArtifactSuccess() throws Exception {
        //Given
        doNothing().when(this.artifactService).delete("1250808601744904191");

        //When
        this.mockMvc.perform(delete("/api/v1/artifacts/1250808601744904191").contentType(MediaType.APPLICATION_JSON)
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
        doThrow(new ArtifactNotFoundException("1250808601744904191")).when(this.artifactService).delete("1250808601744904191");

        //When
        this.mockMvc.perform(delete("/api/v1/artifacts/1250808601744904191").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("could not find artifact with Id: 1250808601744904191"))
                .andExpect(jsonPath("$.data").isEmpty());

        //Then
    }
}
