package com.tartarus.petriflowbackend.model;

import java.io.Serializable;
import java.util.ArrayList;

public class PetriNet implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<PetriNetObject> petriNetObjects = new ArrayList<>();

    public PetriNet() {
    }

    public PetriNet(ArrayList<PetriNetObject> petriNetObjects) {
        this.petriNetObjects = petriNetObjects;
    }

    public ArrayList<PetriNetObject> getPetriNetObjects() {
        return petriNetObjects;
    }

    public void setPetriNetObjects(ArrayList<PetriNetObject> petriNetObjects) {
        this.petriNetObjects = petriNetObjects;
    }
}
