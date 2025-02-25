package com.rbnk.invenmanagement.service;

import com.rbnk.invenmanagement.entity.Project;
import com.rbnk.invenmanagement.entity.ProjectDocument;
import com.rbnk.invenmanagement.entity.User;
import com.rbnk.invenmanagement.repository.ProjectDocumentRepository;
import com.rbnk.invenmanagement.repository.ProjectRepository;
import com.rbnk.invenmanagement.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectDocumentService {

    private final ProjectDocumentRepository projectDocumentRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProjectDocumentService(ProjectDocumentRepository projectDocumentRepository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectDocumentRepository = projectDocumentRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    // Add a new document to a project
    public ProjectDocument addDocument(Long projectId, String filename, String filepath, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Ensure filepath is unique
        if (projectDocumentRepository.findByFilepath(filepath).isPresent()) {
            throw new IllegalArgumentException("A document with this filepath already exists.");
        }

        ProjectDocument document = new ProjectDocument(project, filename, filepath, user);
        document.setUploadedAt(LocalDateTime.now());

        return projectDocumentRepository.save(document);
    }

    // Retrieve all documents for a project
    public List<ProjectDocument> getDocumentsByProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        return projectDocumentRepository.findByProject(project);
    }

    // Retrieve a document by ID
    public ProjectDocument getDocumentById(Long documentId) {
        return projectDocumentRepository.findById(documentId)
                .orElseThrow(() -> new EntityNotFoundException("Document not found"));
    }

    // Update a document's filename
    public ProjectDocument updateDocument(Long documentId, String newFilename) {
        ProjectDocument document = projectDocumentRepository.findById(documentId)
                .orElseThrow(() -> new EntityNotFoundException("Document not found"));

        document.setFilename(newFilename);
        return projectDocumentRepository.save(document);
    }

    // Delete a document
    public void deleteDocument(Long documentId) {
        ProjectDocument document = projectDocumentRepository.findById(documentId)
                .orElseThrow(() -> new EntityNotFoundException("Document not found"));

        projectDocumentRepository.delete(document);
    }
}

