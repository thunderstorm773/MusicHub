//package com.softuni.musichub.log.entity;
//
//import com.softuni.musichub.log.enums.OperationType;
//import javax.persistence.*;
//
//@Entity
//@Table(name = "logs")
//public class Log {
//
//    private Long id;
//
//    private String message;
//
//    private String tableName;
//
//    private OperationType operationType;
//
//    private String username;
//
//    public Log() {
//    }
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(nullable = false, updatable = false)
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    @Column(name = "message", nullable = false,
//            updatable = false, columnDefinition = "TEXT")
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    @Column(name = "table_name", nullable = false,
//            updatable = false)
//    public String getTableName() {
//        return tableName;
//    }
//
//    public void setTableName(String tableName) {
//        this.tableName = tableName;
//    }
//    @Column(name = "operation_type", nullable = false,
//            updatable = false)
//    @Enumerated(EnumType.STRING)
//    public OperationType getOperationType() {
//        return operationType;
//    }
//
//    public void setOperationType(OperationType operationType) {
//        this.operationType = operationType;
//    }
//
//    @Column(name = "username", nullable = false,
//            updatable = false)
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//}
