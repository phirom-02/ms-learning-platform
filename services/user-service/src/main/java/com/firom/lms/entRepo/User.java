package com.firom.lms.entRepo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document
public class User {

    @Id
    private String id;
    private String name;

    @Indexed(unique = true)
    private String email;

//    private String password;
//    private boolean enableed
}
