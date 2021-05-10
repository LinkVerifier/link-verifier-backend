package kjm.linkverifier.files.service;

import kjm.linkverifier.files.exception.FileStorageException;
import kjm.linkverifier.files.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Objects;
import java.util.stream.Stream;
import kjm.linkverifier.files.model.File;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

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

    public File store(File file) {
        return fileRepository.save(file);
    }

    public File store(java.io.File file) {
        try {
            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            byte[] fileContent = Files.readAllBytes(file.toPath());
            File newFile = new File(file.getName(), mimeType, fileContent);
            return fileRepository.save(newFile);
        } catch (IOException e) {
            throw new FileStorageException("Could not store file " + file.getName() + ". Please try again!", e);
        }
    }

    public File findFileById(String id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: File is not found"));
    }

    public void delete(File file) {
        fileRepository.delete(file);
    }

    public Stream<File> findAllFiles() {
        return fileRepository.findAll().stream();
    }

}
