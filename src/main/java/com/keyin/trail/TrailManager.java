package com.keyin.trail;

import com.keyin.client.RemoteAPIClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrailManager {
    private List<Trail> trails;
    private RemoteAPIClient remoteAPIClient;

    public List<Trail> getTrails() {
        if (trails == null) {
            trails = new ArrayList<>();
        }

        return trails;
    }

    public void addTrail(Trail trail) {
        if (trails == null) return;

        if (!getTrails().contains(trail)) {
            getTrails().add(trail);

            try {
                getRemoteAPIClient().createTrail(trail);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public RemoteAPIClient getRemoteAPIClient() {
        if (remoteAPIClient == null) {
            remoteAPIClient = new RemoteAPIClient("https://localhost:8080/trail");
        }

        return remoteAPIClient;
    }

    public void setRemoteAPIClient(RemoteAPIClient remoteAPIClient) {
        this.remoteAPIClient = remoteAPIClient;
    }

    public void setTrails(List<Trail> trails) {
        this.trails = trails;
    }
}
