package com.app.final_project.userInfor;
import com.app.final_project.enums.Gender;
import com.app.final_project.user.User;
import jakarta.persistence.*;
        import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "user_info")
public class UserInfor{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_info_id;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id") // This column is a foreign key.
    private User user;
    @Column(unique = true)
    private String fullname;
    private LocalDateTime dateOfBirth;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String address;
}
