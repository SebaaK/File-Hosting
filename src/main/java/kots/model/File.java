package kots.model;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "files")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    @Lob
    private byte[] data;
}
