package com.example.BookMangement.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * HistoryChange
 *
 * @author xuanl
 * @version 01-00
 * @since 6/3/2024
 */
@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "202403_history_change")
public class HistoryChange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CREATE_DATE",nullable = false)
    private LocalDate createDate;

    @Column(name = "CREATE_BY",nullable = false)
    private String createBy;

    @Column(name = "UPDATE_Date",nullable = false)
    private LocalDate updateDate;

    @Column(name = "UPDATE_BY",nullable = false)
    private String updateBy;

    @Column(name = "entity_type",nullable = false)
    private String entityType;

    @Column(name = "field_name",nullable = false)
    private String fieldName;
    @Column(name = "old_value",nullable = false)
    private String oldValue;
    @Column(name = "new_value",nullable = false)
    private String newValue;

    @Column(name = "status",nullable = false)
    private String status;

    @Column(name = "user_id",nullable = false)
    private Long userId;
    @Column(name = "request_id",nullable = false)
    private Long requestId;

    public String getStatusString() {
        switch (status) {
            case "1":
                return "Pending";
            case "2":
                return "In progress";
            case "3":
                return "Done";
            case "4":
                return "Approve";
            case "5":
                return "Reject";
            default:
                return "Unknown";
        }
    }

}
