package org.jboss.as.quickstarts.datagrid.hotrod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class RestApiController {

    @Autowired
    private TeamManager teamManager;

    @GetMapping("/")
    public String healthCheck() {
        return "<p><b>Service is UP and RUNNING...<b>" +
                "<br>Please use proper GET and PUT method to enjoy the service. Options are : </br>" +
                "<br>1. Get all TeamName - /v1 </br>" +
                "<br>2. Add a TeamName - /v1/add/{teamName} </br>" +
                "<br>3. Delete a TeamName - /v1/del/{teamName}";
    }

    @GetMapping("/v1")
    public List<String> getTeams() {
            return teamManager.printTeams();
    }

    @PutMapping("/v1/add/{teamName}")
    public void addTeam(@PathVariable String teamName) {
        teamManager.addTeam(teamName);
    }

    @DeleteMapping("/v1/del/{teamName}")
    public void delTeam(@PathVariable String teamName) {
        teamManager.removeTeam(teamName);
    }
}
