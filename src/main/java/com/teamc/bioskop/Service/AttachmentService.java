package com.teamc.bioskop.Service;

import com.teamc.bioskop.Model.Attachment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachmentService {
    Attachment saveAttachment(MultipartFile file) throws Exception;

    Attachment getAttachment(String fileId) throws Exception;

    List<Attachment> findAllAttachment();
}