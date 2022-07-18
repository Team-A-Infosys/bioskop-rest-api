package com.teamc.bioskop.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentResponse {
    private String filename;

    private String downloadUrl;

    private String fileType;

    private long fileSize;
}