package com.keyin;

import java.util.ArrayList;
import java.util.List;

public class TrailManager {
    private List<Trail> trails;

    public List<Trail> getTrails() {
        if (trails == null) {
            trails = new ArrayList<>();
        }

        return trails;
    }

    public void addTrail(Trail trail) {
        getTrails().add(trail);
    }

}
