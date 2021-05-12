package kjm.linkverifier.auth.service;

import kjm.linkverifier.auth.exceptions.EmailNotFoundException;
import kjm.linkverifier.auth.exceptions.UserNotFoundException;
import kjm.linkverifier.auth.models.Role;
import kjm.linkverifier.auth.models.RoleEnum;
import kjm.linkverifier.auth.models.User;
import kjm.linkverifier.auth.repository.RoleRepository;
import kjm.linkverifier.auth.repository.UserRepository;
import kjm.linkverifier.files.repository.FileRepository;
import kjm.linkverifier.files.service.FileService;
import kjm.linkverifier.link.model.Comment;
import kjm.linkverifier.link.service.CommentService;
import kjm.linkverifier.link.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    CommentService commentService;

    @Autowired
    LinkService linkService;

    @Autowired
    private FileService fileService;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException(email));
    }

    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByCommentsContaining(Comment comment) {
        return userRepository.findByCommentsContaining(comment).orElseThrow(UserNotFoundException::new);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
        commentService.deleteSome(user.getComments());
        fileService.delete(user.getProfilePicture());
    }

    @Bean
    public CommandLineRunner saveAdmin() {
        return(args -> {

            if (roleRepository.findAll().size() == 0) {
            roleRepository.save(new Role(RoleEnum.ROLE_USER));
            roleRepository.save(new Role(RoleEnum.ROLE_ADMIN));
            }
            if (!userRepository.existsByEmail("admin@admin.pl")) {
                java.io.File file = new File("profilepic.jpg");
                kjm.linkverifier.files.model.File savedFile = fileService.store(file);
                User user = new User("admin",
                        "admin@admin.pl",
                        encoder.encode("admin123"),
                        new Date(),
                        true,
                        savedFile);
                Set<Role> roleToSet = new HashSet<>();
                roleToSet.add(roleRepository.findByName(RoleEnum.ROLE_ADMIN).get());

                user.setRoles(roleToSet);
                userRepository.save(user);
            }

        });
    }

}
