package com.example.awsTodo.repository;

import com.example.awsTodo.model.Todo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TodoRepository {

    private final DynamoDbEnhancedClient enhancedClient;
    private DynamoDbTable<Todo> todoTable;

    @PostConstruct
    void init() {
        todoTable = enhancedClient.table("TodoTable", TableSchema.fromBean(Todo.class));
    }

    public List<Todo> findAll() {
        return todoTable.scan().items().stream()
                .sorted(Comparator.comparing(Todo::getCreatedAt).reversed())
                .toList();
    }

    public void save(String task) {
        Todo todo = Todo.builder()
                .id(UUID.randomUUID().toString())
                .task(task)
                .createdAt(Instant.now())
                .build();
        todoTable.putItem(todo);
    }

    public void delete(String id) {
        todoTable.deleteItem(Key.builder().partitionValue(id).build());
    }

    public void update(String id, String task) {
        Todo existing = todoTable.getItem(Key.builder().partitionValue(id).build());
        if (existing != null) {
            existing.setTask(task);
            todoTable.putItem(existing);
        }
    }
}
