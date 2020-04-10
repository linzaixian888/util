package com.linzaixian.util.freemarker;

import freemarker.template.TemplateException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class FreemarkerUtilTest {
    @Test
    public void build() throws IOException, TemplateException {
        String str="测试";
        FreemarkerUtil util=FreemarkerUtil.createBuilder()
                .setSuffix("ftl")
                .setIsCloseStream(true)
                .addClassLoader(FreemarkerUtilTest.class,"/")
                .build();
        StringWriter sw=new StringWriter();
        Map<String,String> root=new HashMap<>();
        root.put("test",str);
        util.process("test",root,sw);
        Assert.assertEquals(str,sw.toString());

    }

}
