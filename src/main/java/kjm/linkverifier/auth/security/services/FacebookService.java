package kjm.linkverifier.auth.security.services;

import kjm.linkverifier.auth.client.FacebookClient;
import kjm.linkverifier.auth.exceptions.RoleNotFoundException;
import kjm.linkverifier.auth.models.Role;
import kjm.linkverifier.auth.models.RoleEnum;
import kjm.linkverifier.auth.models.User;
import kjm.linkverifier.auth.repository.RoleRepository;
import kjm.linkverifier.auth.repository.UserRepository;
import kjm.linkverifier.auth.security.oauth2.FacebookUser;
import kjm.linkverifier.files.model.File;
import kjm.linkverifier.files.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class FacebookService {

    private final FacebookClient facebookClient;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final FileService fileService;

    public FacebookService(FacebookClient facebookClient, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, FileService fileService) {
        this.facebookClient = facebookClient;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileService = fileService;
    }

    public User getUserFromFacebook(String fbAccessToken, Long creationDate) {
        var facebookUser = facebookClient.getUser(fbAccessToken);
        User user;
        if(!userRepository.existsById(facebookUser.getId())) {
            user = registerFacebookUser(convertUserToFacebookUser(facebookUser, creationDate));
        } else if(userRepository.existsByEmail(facebookUser.getEmail())) {
            user = userRepository.findByEmail(facebookUser.getEmail()).get();
        } else {
            user = userRepository.findById(facebookUser.getId()).get();
        }

        return user;
    }

    public User convertUserToFacebookUser(FacebookUser facebookUser, Long creationDate) {
        String url = facebookUser.getPicture().getData().getUrl();
        log.info("url {}", url);
        String extension = url.substring(url.lastIndexOf("."));
        byte[] fileContent = new byte[0];
        try {
            fileContent = IOUtils.toByteArray(new URL(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = fileService.store(new File(facebookUser.getPicture().toString(), "image/"+extension, fileContent));

        return User.builder()
                .id(facebookUser.getId())
                .email(facebookUser.getEmail())
                .username(facebookUser.getFirstName() + " " + facebookUser.getLastName())
                .password(passwordEncoder.encode(generatePassayPassword(8)))
                .creationDate(new Date(creationDate))
                .profilePicture(file)
                .isConfirmed(true)
                .build();
    }

    public User registerFacebookUser(User user) {
        log.info("registering facebook user {}", user.getEmail());

         if(userRepository.existsByEmail(user.getEmail())) {
            log.warn("email {} already exists", user.getEmail());
        }

        Set<Role> rolesToSet = new HashSet<>();
        Role userFacebookRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                .orElseThrow(() -> new RoleNotFoundException("Error: Role is not found"));

        rolesToSet.add(userFacebookRole);
        user.setRoles(rolesToSet);
        return userRepository.save(user);
    }

    public String generatePassayPassword(int size) {
        PasswordGenerator gen = new PasswordGenerator();
        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(2);

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(2);

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(2);

        CharacterData specialChars = new CharacterData() {
            @Override
            public String getErrorCode() {
                return "ERROR";
            }

            public String getCharacters() {
                return "!@#$%^&*()_+";
            }
        };
        CharacterRule splCharRule = new CharacterRule(specialChars);
        splCharRule.setNumberOfCharacters(2);

        String password = gen.generatePassword(size, splCharRule, lowerCaseRule,
                upperCaseRule, digitRule);
        return password;
    }
}
