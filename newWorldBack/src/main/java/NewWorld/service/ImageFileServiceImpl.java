package NewWorld.service;

import NewWorld.domain.ImageFile;
import NewWorld.domain.User;
import NewWorld.dto.ImageFileDto;
import NewWorld.repository.ImageFileRepository;
import NewWorld.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageFileServiceImpl implements ImageFileService {

    private final ImageFileRepository imageFileRepository;
    private final UserRepository userRepository;



    /**
     * Saves the uploaded image file.
     *
     * @param uploadFile The uploaded image file.
     * @param realpath The path to save the image file.
     * @return The status of the save operation. Possible values are "s" for success and "f" for failure.
     */
    @Override
    public String saveImageFile(MultipartFile uploadFile, String realpath, String userName, String userNickname){

        // 이미지 파일만 업로드
        if (!Objects.requireNonNull(uploadFile.getContentType()).startsWith("image")) {
            log.warn("this file is not image type");
            return "f";
        }

        String originalFilename = uploadFile.getOriginalFilename();
        String path = realpath + "/" + originalFilename;

        ImageFileDto imageFileDto = ImageFileDto.builder()
                .path(path)
                .originalPath(originalFilename)
                .fileName(originalFilename)
                .build();

        ImageFile imageFile = ImageFile.of(imageFileDto);
        ImageFile save = imageFileRepository.save(imageFile);

        File file = new File(path);

        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(uploadFile.getBytes());
            bos.close();

        } catch (Exception e) {
            return "f";
        }

        User user = userRepository.findUserByNameAndNickname(userName, userNickname);

        if(user != null){
            user.saveImage(save);
        }

        if(save == null) return "f";
        else return  "s";
    }

}