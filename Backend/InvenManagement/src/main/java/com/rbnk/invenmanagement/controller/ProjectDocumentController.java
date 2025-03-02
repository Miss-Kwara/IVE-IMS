package com.rbnk.invenmanagement.controller;

import com.rbnk.invenmanagement.entity.ProjectDocument;
import com.rbnk.invenmanagement.entity.User;
import com.rbnk.invenmanagement.service.ProjectDocumentService;
import com.rbnk.invenmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project-documents")
public class ProjectDocumentController {

    private final ProjectDocumentService projectDocumentService;
    private final UserService userService;

    @Autowired
    public ProjectDocumentController(ProjectDocumentService projectDocumentService, UserService userService) {
        this.projectDocumentService = projectDocumentService;
        this.userService = userService;
    }

    private boolean lacksPermission(String username, int requiredTier) {
        Long userId = userService.returnId(username);
        User user = userService.getUserById(userId);
        Integer userTier = user.getRoleId();
        return userTier == null || userTier > requiredTier;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDocument> getDocumentById(@PathVariable Long id) {
        ProjectDocument document = projectDocumentService.getDocumentById(id);
        return ResponseEntity.ok(document);
    }

    @GetMapping("/project/{projectId}")
    public List<ProjectDocument> getDocumentsByProject(@PathVariable Long projectId) {
        return projectDocumentService.getDocumentsByProject(projectId);
    }

    @PostMapping
    public ResponseEntity<ProjectDocument> addDocument(@RequestParam Long projectId,
                                                       @RequestParam String filename,
                                                       @RequestParam String filepath,
                                                       @RequestParam String username) {
        if (lacksPermission(username, 2)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Long userId = userService.returnId(username);
        ProjectDocument document = projectDocumentService.addDocument(projectId, filename, filepath, userId);
        return ResponseEntity.ok(document);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDocument> updateDocument(@PathVariable Long id,
                                                          @RequestParam String username,
                                                          @RequestParam String newFilename) {
        if (lacksPermission(username, 2)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        ProjectDocument updatedDocument = projectDocumentService.updateDocument(id, newFilename);
        return ResponseEntity.ok(updatedDocument);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id, @RequestParam String username) {
        if (lacksPermission(username, 1)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        projectDocumentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}
