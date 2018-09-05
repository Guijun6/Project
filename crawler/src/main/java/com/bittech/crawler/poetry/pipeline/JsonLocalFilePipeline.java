package com.bittech.crawler.poetry.pipeline;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;

/**
 * Author: secondriver
 * Created: 2017/12/23 0023
 */
public class JsonLocalFilePipeline extends FilePersistentBase implements Pipeline {
    
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    public JsonLocalFilePipeline(String path) {
        setPath(path);
    }
    
    @Override
    public void process(ResultItems resultItems, Task task) {
        Object value = resultItems.get("poetry");
        if (value == null) {
            logger.error("Url {} not find value.", resultItems.getRequest().getUrl());
        } else {
            String path = this.path + PATH_SEPERATOR + task.getUUID() + PATH_SEPERATOR;
            try {
                // 存储的文件名
                File file = getFile(path + DigestUtils.md5Hex(resultItems.getRequest().getUrl()) + ".json");
                // 获取文件输出流
                PrintWriter printWriter = new PrintWriter(new FileWriter(file));
                // 输出打印流
                printWriter.write(JSON.toJSONString(value, SerializerFeature.PrettyFormat));

                printWriter.close();
            } catch (IOException e) {
                logger.warn("write file error", e);
            }
        }
    }
}
