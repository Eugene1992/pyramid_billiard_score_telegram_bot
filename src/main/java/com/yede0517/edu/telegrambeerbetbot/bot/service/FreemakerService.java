package com.yede0517.edu.telegrambeerbetbot.bot.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.documents.*;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class FreemakerService {

    @Autowired
    private Configuration configuration;

    public String getFrameStats() {
        try {
            Template template = configuration.getTemplate("frame_stats.html");
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, new Object());
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }

        return "";
    }
}
