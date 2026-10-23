package com.keyin.cli;

import com.keyin.client.RemoteAPIClient;
import com.keyin.trail.Trail;
import com.keyin.trail.TrailManager;

import java.util.List;

public class TrailManagementSystemCLI {

    public static void main(String[] args) {
        TrailManager trailManager = new TrailManager();

        RemoteAPIClient remoteAPIClient = new RemoteAPIClient("http://localhost:8080");

        trailManager.setRemoteAPIClient(remoteAPIClient);

        List<Trail> trails = trailManager.getTrails();

        for (Trail trail : trails) {
            System.out.println(trail);
        }
    }
}
