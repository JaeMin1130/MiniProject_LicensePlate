package plate.back.entity;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "history")
public class HistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer logId;

    // @ManyToOne(optional = false, cascade = CascadeType.REMOVE)
    // @JoinColumn(name = "log_id")
    // @OnDelete(action = OnDeleteAction.CASCADE)
    // // @JoinColumn(name = "logId", referencedColumnName = "logId", nullable =
    // false)
    // private LogEntity logEntity;

    @Column(nullable = false, length = 20)
    private String userId;

    @Column(nullable = false, length = 10)
    private String workType;

    @Column(nullable = false, length = 10)
    private String previousText;

    @Column(nullable = false, length = 10)
    private String currentText;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date date;
}
