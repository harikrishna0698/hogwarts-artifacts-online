package edu.tcu.cs.hogwarts_artifacts_online.wizard;

import edu.tcu.cs.hogwarts_artifacts_online.system.exception.ObjectNotFoundException;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WizardServiceTest {

    @Mock
    WizardRepository wizardRepository;
    @InjectMocks
    WizardService wizardService;

    List<Wizard> wizardList = new ArrayList<>();

    @BeforeEach
    void setUp(){
        Wizard w1 = new Wizard();
        w1.setId(101);
        w1.setName("Harry Potter");
        this.wizardList.add(w1);

        Wizard w2 = new Wizard();
        w2.setId(102);
        w2.setName("Albus dumbledore");
        this.wizardList.add(w2);

        Wizard w3 = new Wizard();
        w3.setId(103);
        w3.setName("Professor Snape");
        this.wizardList.add(w3);

    }

    @Test
    void testFindAllSuccess(){
        //Given
        given(this.wizardRepository.findAll()).willReturn(this.wizardList);

        //When
        List<Wizard> actualWizards = wizardService.findAll();

        //Then
        assertThat(actualWizards.size()).isEqualTo(this.wizardList.size());
        verify(wizardRepository,times(1)).findAll();
    }

    @Test
    void testFindByIdSuccess(){
        //Given
        Wizard w = new Wizard();
        w.setId(101);
        w.setName("Harry Potter");

        given(this.wizardRepository.findById(101)).willReturn(Optional.of(w));

        //When
        Wizard foundWizard = this.wizardService.findById(101);

        //Then
        assertThat(foundWizard.getId()).isEqualTo(w.getId());
        assertThat(foundWizard.getName()).isEqualTo(w.getName());

        verify(wizardRepository,times(1)).findById(101);
    }

    @Test
    void testFindByIdNotFound(){
        //Given
        given(wizardRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        //When
        Throwable thrown = catchThrowable(() -> {
            Wizard returnedWizard = wizardService.findById(101);
        });

        //Then
        assertThat(thrown).isInstanceOf(WizardNotFoundException.class)
                .hasMessage(("Could not find wizard with Id: 101"));
        verify(wizardRepository,times(1)).findById(101);
    }

    @Test
    void testSaveSuccess(){
        //Given
        Wizard newWizard = new Wizard();
        newWizard.setId(103);
        newWizard.setName("Harmoine Grenger");

        given(this.wizardRepository.save(newWizard)).willReturn(newWizard);

        //When
        Wizard savedWizard = this.wizardService.save(newWizard);

        //Then
        assertThat(savedWizard.getId()).isEqualTo(103);
        assertThat(savedWizard.getName()).isEqualTo(newWizard.getName());

        verify(wizardRepository, times(1)).save(newWizard);
    }

    @Test
    void testUpdateSuccess(){
        Wizard oldWizard = new Wizard();
        oldWizard.setId(101);
        oldWizard.setName("Harry Potter");

        Wizard update = new Wizard();
        update.setId(101);
        update.setName("Harry James Potter");

        given(wizardRepository.findById(101)).willReturn(Optional.of(oldWizard));
        given(wizardRepository.save(oldWizard)).willReturn(oldWizard);

        //When
        Wizard updatedWizard = this.wizardService.update(101,update);

        //Then
        assertThat(updatedWizard.getId()).isEqualTo(update.getId());
        assertThat(updatedWizard.getName()).isEqualTo(update.getName());
        verify(wizardRepository, times(1)).findById(101);
        verify(wizardRepository, times(1)).save(oldWizard);
    }

    @Test
    void testUdateNotFound(){
        //Given
        Wizard update = new Wizard();
        update.setId(101);
        update.setName("Harry Potter");

        given(wizardRepository.findById(101)).willReturn(Optional.empty());

        //When
        assertThrows(WizardNotFoundException.class, () -> {
            wizardService.update(101,update);
        });

        //Then
        verify(wizardRepository, times(1)).findById(101);
    }

    @Test
    void testDeleteSuccess(){
        //Given
        Wizard w1 = new Wizard();
        w1.setId(101);
        w1.setName("Harry Potter");
        given(this.wizardRepository.findById(101)).willReturn(Optional.of(w1));
        doNothing().when(wizardRepository).deleteById(101);

        //When
        wizardService.delete(101);

        //Then
        verify(wizardRepository, times(1)).findById(101);
    }

    @Test
    void testDeleteNotFound(){
        //Given
        given(wizardRepository.findById(101)).willReturn(Optional.empty());

        //When
        assertThrows(WizardNotFoundException.class, () -> {
            wizardService.delete(101);
        });

        //Then
        verify(wizardRepository, times(1)).findById(101);

    }


}