package kjm.linkverifier.files.controller;

import kjm.linkverifier.auth.models.User;
import kjm.linkverifier.auth.repository.UserRepository;
import kjm.linkverifier.auth.service.CurrentUser;
import kjm.linkverifier.files.model.File;
import kjm.linkverifier.files.response.ResponseFile;
import kjm.linkverifier.files.response.ResponseMessage;
import kjm.linkverifier.files.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private UserRepository userRepository;

    @PutMapping("/upload")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file,
                                                      HttpServletRequest httpServletRequest) {
        String message = "";
        User user = CurrentUser.getCurrentUser(httpServletRequest);
        try {
            File savedFile = fileService.store(file);
            user.setProfilePicture(savedFile);
            userRepository.save(user);
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<ResponseFile>> getListFiles() {
        List<ResponseFile> files = fileService.findAllFiles().map(f -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/")
                    .path(f.getId())
                    .toUriString();

            return new ResponseFile(
                    f.getName(),
                    fileDownloadUri,
                    f.getType(),
                    f.getData().length);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) {
        File file = fileService.findFileById(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(file.getData());
    }

}
