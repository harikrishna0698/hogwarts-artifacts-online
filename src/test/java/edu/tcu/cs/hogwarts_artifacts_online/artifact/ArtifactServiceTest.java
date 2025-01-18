package edu.tcu.cs.hogwarts_artifacts_online.artifact;

import edu.tcu.cs.hogwarts_artifacts_online.artifact.utils.IdWorker;
import edu.tcu.cs.hogwarts_artifacts_online.wizard.Wizard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ArtifactServiceTest {
    @Mock
    ArtifactRepository artifactRepository;

    @Mock
    IdWorker idWorker;

    @InjectMocks
    ArtifactService artifactService;

    List<Artifact> artifactList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles");
        a1.setImageUrl("ImageUrl");
        this.artifactList.add(a1);

        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("ImageUrl");
        this.artifactList.add(a2);
    }


    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindByIdSuccess() {
        //given (ARrange inputs and targets. Define the behaviour of Mock object artifactRepository
        Artifact a = new Artifact();
        a.setId("123");
        a.setName("Invisibility cloack");
        a.setDescription("An invisilibity cloack is used to make the wearer invisible");
        a.setImageUrl("imageURL");
        Wizard w = new Wizard();
        w.setId(2);
        w.setName("harry potter");

        a.setOwner(w);

        given(artifactRepository.findById("123")).willReturn(Optional.of(a));

        //when (Act on the target behaviour when steps should cover the method to be treated)
        Artifact returnedArtifact = artifactService.findById("123");

        //then  (Assert expectations outcomes)
        assertThat(returnedArtifact.getId()).isEqualTo(a.getId());
        assertThat(returnedArtifact.getName()).isEqualTo(a.getName());
        assertThat(returnedArtifact.getDescription()).isEqualTo(a.getDescription());
        assertThat(returnedArtifact.getImageUrl()).isEqualTo(a.getImageUrl());

        verify(artifactRepository, times(1)).findById("123");
    }

    @Test
    void testFindByIdNotFound(){
        //Given
        given(artifactRepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());

        //When
        Throwable thrown = catchThrowable(()->{
            Artifact returnedArtifact = artifactService.findById("123");
        });

        //Then
        assertThat(thrown).isInstanceOf(ArtifactNotFoundException.class)
                .hasMessage("could not find artifact with Id: 123");
        verify(artifactRepository,times(1)).findById("123");
    }

    @Test
    void testFindAllSuccess(){
        //Given
        given(artifactRepository.findAll()).willReturn(this.artifactList);

        //When
        List<Artifact> actualArtifacts = artifactService.findAll();

        //Then
        assertThat(actualArtifacts.size()).isEqualTo(this.artifactList.size());
        verify(artifactRepository,times(1)).findAll();
    }

    @Test
    void testSaveSuccess(){
        //Given
        Artifact newArtifact = new Artifact();
        newArtifact.setName("Artifact 3");
        newArtifact.setDescription("Description....");
        newArtifact.setImageUrl("ImageUrl...");

        given(idWorker.nextId()).willReturn(123456L);
        given(artifactRepository.save(newArtifact)).willReturn(newArtifact);

        //When
        Artifact savedArtifact = artifactService.save(newArtifact);

        //Then
        assertThat(savedArtifact.getId()).isEqualTo("123456");
        assertThat(savedArtifact.getName()).isEqualTo(newArtifact.getName());
        assertThat(savedArtifact.getDescription()).isEqualTo(newArtifact.getDescription());
        assertThat(savedArtifact.getImageUrl()).isEqualTo(newArtifact.getImageUrl());
        verify(artifactRepository, times(1)).save(newArtifact);
    }

    @Test
    void testUpdateSuccess(){
        //Given
        Artifact oldArtifact = new Artifact();
        oldArtifact.setId("1250808601744904192");
        oldArtifact.setName("Invisibility Cloak");
        oldArtifact.setDescription("An invisibility cloak is used to make the wearer invisible.");
        oldArtifact.setImageUrl("ImageUrl");

        Artifact update = new Artifact();
        update.setId("1250808601744904192");
        update.setName("Invisibility Cloak");
        update.setDescription("A new Description");
        update.setImageUrl("ImageUrl");

        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(oldArtifact));
        given(artifactRepository.save(oldArtifact)).willReturn(oldArtifact);

        //When
        Artifact updatedArtifact = artifactService.update("1250808601744904192",update);

        //Then
        assertThat(updatedArtifact.getId()).isEqualTo(update.getId());
        assertThat(updatedArtifact.getDescription()).isEqualTo(updatedArtifact.getDescription());
        verify(artifactRepository,times(1)).findById("1250808601744904192");
        verify(artifactRepository,times(1)).save(oldArtifact);
    }

    @Test
    void testUpdateNotFound(){
        //Given
        Artifact update = new Artifact();
        update.setName("Invisibility cloak");
        update.setDescription("A new description");
        update.setImageUrl("ImageUrl");

        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.empty());

        //When
        assertThrows(ArtifactNotFoundException.class,() ->{
            artifactService.update("1250808601744904192",update);
        });

        //Then
        verify(artifactRepository,times(1)).findById("1250808601744904192");
    }

    @Test
    void testDeleteSuccess(){
        //Given
        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles");
        a1.setImageUrl("ImageUrl");

        given(artifactRepository.findById("1250808601744904191")).willReturn(Optional.of(a1));
        doNothing().when(artifactRepository).deleteById("1250808601744904191");

        //When
        artifactService.delete("1250808601744904191");

        //Then
        verify(artifactRepository,times(1)).findById("1250808601744904191");
    }

    @Test
    void testDeleteNotFound(){
        //Given

        given(artifactRepository.findById("1250808601744904191")).willReturn(Optional.empty());


        //When
        assertThrows(ArtifactNotFoundException.class, () ->{
            artifactService.delete("1250808601744904191");
        });

        //Then
        verify(artifactRepository,times(1)).findById("1250808601744904191");
    }
}