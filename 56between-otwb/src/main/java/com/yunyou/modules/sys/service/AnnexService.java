package com.yunyou.modules.sys.service;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.utils.FileUtils;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.entity.Annex;
import com.yunyou.modules.sys.mapper.AnnexMapper;
import com.yunyou.modules.sys.utils.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AnnexService extends CrudService<AnnexMapper, Annex> {
    private final String UPLOAD_PATH = ".." + File.separator + "annex";

    @Transactional
    public void upload(Integer type, String pkId, MultipartFile file) {
        Annex annex = new Annex();
        annex.setType(type);
        annex.setPkId(pkId);
        annex.setFileName(file.getOriginalFilename());
        annex.setFileSize(file.getSize());
        annex.setUploadBy(UserUtils.getUser());
        annex.setUploadDate(new Date());
        annex.setPath(UPLOAD_PATH);
        super.save(annex);

        try {
            // 将ID拼入文件名(防止文件名重复)
            File file1 = new File(UPLOAD_PATH + File.separator + annex.getId() + annex.getFileName());
            if (!file1.getParentFile().exists()) {
                FileUtils.createDirectory(file1.getParentFile().getPath());
            }
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file1));
            outputStream.write(file.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new GlobalException("文件上传失败");
        }
    }

    @Transactional
    public void delete(String id) {
        Annex annex = super.get(id);
        if (annex == null) {
            return;
        }

        // 1.删除服务器附件文件
        String path = annex.getPath();
        String fileName = annex.getFileName();
        FileUtils.deleteFile(path + File.separator + id + fileName);

        // 2.删除附件记录
        super.delete(annex);
    }

    @Transactional
    public void deleteByTypeAndPk(Integer type, String pkId) {
        Annex annex = new Annex();
        annex.setType(type);
        annex.setPkId(pkId);
        List<Annex> list = findList(annex);
        list.forEach(o -> delete(o.getId()));
    }
}
