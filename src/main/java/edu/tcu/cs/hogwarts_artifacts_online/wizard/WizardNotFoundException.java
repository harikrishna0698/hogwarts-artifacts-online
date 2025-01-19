package edu.tcu.cs.hogwarts_artifacts_online.wizard;

public class WizardNotFoundException extends RuntimeException{
    public WizardNotFoundException(Integer id){
        super("could not find wizard with Id: "+id);
    }
}
