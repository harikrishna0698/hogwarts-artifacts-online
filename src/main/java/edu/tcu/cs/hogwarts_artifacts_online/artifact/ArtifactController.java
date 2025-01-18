package edu.tcu.cs.hogwarts_artifacts_online.artifact;

import edu.tcu.cs.hogwarts_artifacts_online.artifact.converter.ArtfiactToArtifactDtoConverter;
import edu.tcu.cs.hogwarts_artifacts_online.artifact.converter.ArtifactDtoToArtifactConverter;
import edu.tcu.cs.hogwarts_artifacts_online.artifact.dto.ArtifactDto;
import edu.tcu.cs.hogwarts_artifacts_online.system.Result;
import edu.tcu.cs.hogwarts_artifacts_online.system.StatusCode;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/artifacts")
public class ArtifactController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArtifactController.class);
    private final ArtifactService artifactService;
    private final ArtfiactToArtifactDtoConverter artfiactToArtifactDtoConverter;
    private final ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter;

    public ArtifactController(ArtifactService artifactService, ArtfiactToArtifactDtoConverter artfiactToArtifactDtoConverter, ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter) {
        this.artifactService = artifactService;
        this.artfiactToArtifactDtoConverter = artfiactToArtifactDtoConverter;
        this.artifactDtoToArtifactConverter = artifactDtoToArtifactConverter;
    }

    @GetMapping("/{artifactId}")
    public Result findArtifactById(@PathVariable String artifactId){
        Artifact foundArtifact = this.artifactService.findById(artifactId);
        ArtifactDto artifactDto = this. artfiactToArtifactDtoConverter.convert(foundArtifact);
        return new Result(true, StatusCode.SUCCESS,"Find one Success",artifactDto);
    }

    @GetMapping()
    public Result findAllArtifacts(){
        List<Artifact> foundArtifacts = this.artifactService.findAll();
        //convert foundArtifacts to a list of artifactDto
        List<ArtifactDto> artifactDtos = foundArtifacts.stream().map(foundArtifact -> this.artfiactToArtifactDtoConverter.convert(foundArtifact))
                .collect(Collectors.toList());
        return new Result(true,StatusCode.SUCCESS, "Find All Success",artifactDtos);
    }

    @PostMapping()
    public Result addArtifact(@Valid @RequestBody ArtifactDto artifactDto){
        //convert artifactDto to artifact
        Artifact newArtifact = this.artifactDtoToArtifactConverter.convert(artifactDto);
        Artifact savedArtifact = this.artifactService.save(newArtifact);
        ArtifactDto savedArtifactDto = this.artfiactToArtifactDtoConverter.convert(savedArtifact);
        return new Result(true,StatusCode.SUCCESS,"Add Success",savedArtifactDto);
    }

    @PutMapping("/{artifactId}")
    public ResponseEntity<Result> updateArtifact(
            @PathVariable String artifactId,
            @Valid @RequestBody ArtifactDto artifactDto) {
        try {
            LOGGER.info("Received ArtifactDto: {}", artifactDto);
            Artifact artifact = this.artifactDtoToArtifactConverter.convert(artifactDto);
            LOGGER.info("Updated : {}",artifact);
            Artifact updatedArtifact = this.artifactService.update(artifactId, artifact);
            ArtifactDto updatedArtifactDto = this.artfiactToArtifactDtoConverter.convert(updatedArtifact);
            return ResponseEntity.ok(new Result(true, StatusCode.SUCCESS, "Update success", updatedArtifactDto));
        } catch (ArtifactNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Result(false, StatusCode.NOT_FOUND, ex.getMessage()));
        }
    }

    @DeleteMapping("/{artifactId}")
    public Result deleteArtifact(@PathVariable String artifactId){
         this.artifactService.delete(artifactId);
        return new Result(true,StatusCode.SUCCESS,"Delete success");
    }

}
