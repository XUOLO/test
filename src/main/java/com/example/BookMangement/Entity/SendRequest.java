package com.example.BookMangement.Entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Request
 *
 * @author benvo
 * @version 01-00
 * @since 5/31/2024
 */
@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "202403_send_request")
public class SendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Your description is required")
    @Column(name = "description",nullable = false)
    private String description;

    @Column(name = "CREATE_DATE",nullable = false)
    private LocalDate createDate;

    @Column(name = "CREATE_BY",nullable = false)
    private String createBy;

    @Column(name = "UPDATE_DATE",nullable = false)
    private LocalDate updateDate;

    @Column(name = "UPDATE_BY",nullable = false)
    private String updateBy;

    @Column(name = "IS_DELETE",nullable = false)
    private Boolean isDelete;

    @Column(name = "book_id",nullable = false)
    private Long bookId;

    @Column(name ="status",nullable = false)
    private String status;
    public String getStatusString() {
        switch (status) {
            case "1":
                return "PENDING";
            case "2":
                return "In progress";
            case "3":
                return "Done";
            case "4":
                return "APPROVED";
            case "5":
                return "REJECTED";

            default:
                return "Unknown";
        }
    }
}

