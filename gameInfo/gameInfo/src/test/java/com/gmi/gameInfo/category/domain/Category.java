package com.gmi.gameInfo.category.domain;


import com.gmi.gameInfo.category.domain.dto.CategoryDto;
import com.gmi.gameInfo.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10)
    private String name;

    private Long parentId;

    @OneToMany(mappedBy = "category",fetch = FetchType.LAZY)
    List<Post> posts = new ArrayList<>();


    public Category(String name, Long parentId){
        this.name = name;
        this.parentId = parentId;
    }

    public Category(CategoryDto dto) {
        this.name = dto.getName();
        this.parentId = dto.getParentId();
    }
}
