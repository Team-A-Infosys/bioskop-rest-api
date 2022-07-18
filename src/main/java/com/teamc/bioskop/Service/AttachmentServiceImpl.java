package com.teamc.bioskop.Service;

import com.teamc.bioskop.Model.Attachment;
import com.teamc.bioskop.Repository.AttachmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@AllArgsConstructor
public class AttachmentServiceImpl implements AttachmentService{

    private AttachmentRepository attachmentRepository;
    @Override
    public Attachment saveAttachment(MultipartFile file) throws Exception {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try{
            if(fileName.contains("..")){
                throw new Exception("Filename contains invalid path sequence: " + fileName);
            }
            Attachment attachment = new Attachment(fileName, file.getContentType(),file.getBytes());
            return this.attachmentRepository.save(attachment);

        } catch (Exception e){
            throw new Exception("Could not save File: " + fileName);
        }
    }

    @Override
    public Attachment getAttachment(String fileId) throws Exception {
        return this.attachmentRepository.findById(fileId).orElseThrow(()->new Exception("Could not find file id"));
    }

    @Override
    public List<Attachment> findAllAttachment() {
        return this.attachmentRepository.findAll();
    }
}