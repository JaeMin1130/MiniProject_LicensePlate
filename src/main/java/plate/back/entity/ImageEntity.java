package plate.back.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "image")
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "log_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private LogEntity logEntity;

    @Column(nullable = false, length = 10)
    private String imageType;

    @Column(nullable = false)
    private String imageTitle;

    @Column(nullable = false)
    private String imageUrl;

    public void setImageUrl(String url) {
        this.imageUrl = url;
    }

    public void setImageTitle(String title) {
        this.imageTitle = title;
    }
}