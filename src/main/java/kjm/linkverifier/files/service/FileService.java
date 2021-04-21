package kjm.linkverifier.files.service;

import kjm.linkverifier.files.exception.FileStorageException;
import kjm.linkverifier.files.model.File;
import kjm.linkverifier.files.repository.FileRepository;
import kjm.linkverifier.files.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    public File store(MultipartFile multipartFile) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try {
            File file = new File(fileName, multipartFile.getContentType(), multipartFile.getBytes());
            return fileRepository.save(file);

        } catch (IOException e) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", e);
        }

    }

    public File findFileById(String id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: File is not found"));
    }

    public Stream<File> findAllFiles() {
        return fileRepository.findAll().stream();
    }

}
