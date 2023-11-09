package plate.back.domain.image.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import plate.back.domain.record.entity.Record;

@Getter
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "record_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Record record;

    @Column(nullable = false, length = 10)
    private String imageType;

    @Column(nullable = false)
    private String imageTitle;

    @Column(nullable = false)
    private String imageUrl;

    @Builder
    public Image(Record record, String imageType, String imageTitle, String imageUrl) {
        this.record = record;
        this.imageType = imageType;
        this.imageTitle = imageTitle;
        this.imageUrl = imageUrl;
    }

    public void updateImage(String imageTitle, String imageUrl) {
        this.imageTitle = imageTitle;
        this.imageUrl = imageUrl;
    }
}