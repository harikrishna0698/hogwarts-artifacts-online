package edu.tcu.cs.hogwarts_artifacts_online.wizard;

import edu.tcu.cs.hogwarts_artifacts_online.artifact.ArtifactController;
import edu.tcu.cs.hogwarts_artifacts_online.system.Result;
import edu.tcu.cs.hogwarts_artifacts_online.system.StatusCode;
import edu.tcu.cs.hogwarts_artifacts_online.wizard.converter.WizardDtoToWizardConverter;
import edu.tcu.cs.hogwarts_artifacts_online.wizard.converter.WizardToWizardDtoConverter;
import edu.tcu.cs.hogwarts_artifacts_online.wizard.dto.WizardDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/wizards")
public class WizardController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WizardController.class);
    private WizardService wizardService;
    private WizardToWizardDtoConverter wizardToWizardDtoConverter;
    private WizardDtoToWizardConverter wizardDtoToWizardConverter;

    public WizardController(WizardService wizardService, WizardToWizardDtoConverter wizardToWizardDtoConverter, WizardDtoToWizardConverter wizardDtoToWizardConverter) {
        this.wizardService = wizardService;
        this.wizardToWizardDtoConverter = wizardToWizardDtoConverter;
        this.wizardDtoToWizardConverter = wizardDtoToWizardConverter;
    }

    @GetMapping
    public Result findAllWizards(){
        List<Wizard> foundWizards = this.wizardService.findAll();
        List<WizardDto> wizardDtos = foundWizards.stream().map(foundWizard -> this.wizardToWizardDtoConverter.convert(foundWizard))
                .collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS,"Find all success",wizardDtos);
    }

    @GetMapping("/{wizardId}")
    public Result findWizardById(@PathVariable Integer wizardId){
        Wizard foundWizard = this.wizardService.findById(wizardId);
        WizardDto wizardDto = this.wizardToWizardDtoConverter.convert(foundWizard);
        return new Result(true, StatusCode.SUCCESS,"Find one success",wizardDto);
    }

    @PostMapping
    public Result addWizard(@Valid @RequestBody WizardDto wizardDto){
        Wizard newWizard = this.wizardDtoToWizardConverter.convert(wizardDto);
        Wizard savedWizard = this.wizardService.save(newWizard);
        WizardDto savedWizardDto = this.wizardToWizardDtoConverter.convert(savedWizard);
        return new Result(true,StatusCode.SUCCESS,"Add success",savedWizardDto);
    }

    @PutMapping("/{wizardId}")
    public ResponseEntity<Result> updateArtifact(
            @PathVariable Integer wizardId,
            @Valid @RequestBody WizardDto wizardDto){
        try{
            Wizard wizard = this.wizardDtoToWizardConverter.convert(wizardDto);
            Wizard updatedWizard = this.wizardService.update(wizardId,wizard);
            WizardDto updatedWizardDto = this.wizardToWizardDtoConverter.convert(updatedWizard);
            return  ResponseEntity.ok(new Result(true, StatusCode.SUCCESS,"Update success",updatedWizardDto));
        }catch (WizardNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Result(false,StatusCode.NOT_FOUND, ex.getMessage()));
        }
    }

    @DeleteMapping("/{wizardId}")
    public Result deleteArtifact(@PathVariable Integer wizardId){
        this.wizardService.delete(wizardId);
        return new Result(true, StatusCode.SUCCESS, "Delete success");
    }

    @PutMapping("/{wizardId}/artifacts/{artifactId}")
    public Result assignArtifact(@PathVariable Integer wizardId, @PathVariable String artifactId){
        this.wizardService.assignArtifact(wizardId, artifactId);
        return new Result(true, StatusCode.SUCCESS, "Artifact assignment success");
    }
}
