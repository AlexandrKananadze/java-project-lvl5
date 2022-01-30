package hexlet.code.service.impl;

import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.repository.StatusRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private UserRepository userRepository;
    private StatusRepository statusRepository;
    private TaskRepository taskRepository;


    @Override
    public Task createTask(TaskDto taskDto) {
        Task task = new Task(
                taskDto.getName(),
                taskDto.getDescription(),
                statusRepository.findById(taskDto.getTaskStatusId())
                        .orElseThrow(() -> new NotFoundException("Status not found")),
                getCurrentUser(),
                userRepository.findById(taskDto.getExecutorId())
                        .orElseThrow(() -> new NotFoundException("User not found"))
        );

        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Long id, TaskDto taskDto) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setName(taskDto.getName());
                    task.setDescription(taskDto.getDescription());
                    task.setExecutor(userRepository.findById(taskDto.getExecutorId())
                            .orElseThrow(() -> new NotFoundException("User not found")));
                    task.setTaskStatus(statusRepository.findById(taskDto.getTaskStatusId())
                            .orElseThrow(() -> new NotFoundException("Status not found")));
                    return taskRepository.save(task);
                }).orElseThrow(() -> new NotFoundException("Task not found"));
    }

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private User getCurrentUser() {
        return userRepository.findUserByEmail(getCurrentUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
