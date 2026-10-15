package com.raph_furniture.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

//Add your annotations here
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "home_photo")

@NamedQuery(name = "Badge.GetBadges", query = "SELECT new com.raph_furniture.wrapper.BadgeWrapper(b.id, b.title, b.description, b.badgeName, b.image) FROM Badge b")
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "badge_name")
    private String badgeName;

    @Lob
    @Column(name = "badge_data", columnDefinition = "LONGBLOB")
    private byte[] image;
}
