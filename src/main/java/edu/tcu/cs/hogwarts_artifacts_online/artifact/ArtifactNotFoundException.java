package edu.tcu.cs.hogwarts_artifacts_online.artifact;

public class ArtifactNotFoundException extends RuntimeException{
    public ArtifactNotFoundException(String id){
        super("could not find artifact with Id: "+id);
    }
}
