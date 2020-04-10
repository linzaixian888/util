package com.linzaixian.util.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;


/**
 * Freemarker工具类
 * @author linzaixian
 * @since 2017-06-02 15:56:26
 */
public class FreemarkerUtil {
	private List<TemplateLoader> list=new ArrayList<TemplateLoader>();
	private Configuration cfg;
	private boolean isSetLoader=false;
	private String suffix;
	private boolean isCloseStream=true;

	/**
	 * 创建构造器对象
	 * @return
	 */
	public static FreemarkerUtilBuilder createBuilder(){
		return FreemarkerUtilBuilder.create();
	}
	private FreemarkerUtil(){
		this.cfg=new Configuration(Configuration.getVersion());
		this.cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.getVersion()));
	}

	private void setTemplateLoaderList(List<TemplateLoader> list) {
		this.list = list;
	}


	public FreemarkerUtil(Configuration cfg){
		TemplateLoader templateLoader=cfg.getTemplateLoader();
		if(templateLoader!=null){
			addTemplateLoader(templateLoader);
		}
		this.cfg=cfg;

		
	}
	
	/**
	 * 返回当前加载器个数
	 * @return
	 */
	public int loaderSize(){
		return list.size();
	}
	/**
	 * 替换指定位置的模版加载器
	 * @param index
	 * @param templateLoader
	 * @return
	 */
	public boolean setLoader(int index,TemplateLoader templateLoader){
		if(index<list.size()){
			list.set(index, templateLoader);
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 设置是否自动关闭流
	 * @param flag
	 */
	public void setIsCloseStream(boolean flag){
		this.isCloseStream=flag;
	}
	/**
	 * 设置模版后缀名
	 * @param suffix
	 */
	public void setSuffix(String suffix){
		if(suffix.startsWith(".")){
			this.suffix=suffix;
		}else{
			this.suffix="."+suffix;
		}
		
	}
	/**
	 * 消除当前所有模版加载器
	 */
	public synchronized void clearTemplateLoader(){
		isSetLoader=false;
		list.clear();
	}
	
	/**
	 * 返回当前所有模版加载器集合
	 * @return
	 */
	public List<TemplateLoader> getLoaderList(){
		return list;
	}
	/**
	 * 返回指定位置的模版加载器
	 * @param index
	 * @return
	 */
	public TemplateLoader getLoader(int index){
		if(index<list.size()){
			return list.get(index);
		}else{
			return null;
		}
	}
	/**
	 * 增加一个加载器
	 * @param templateLoader
	 * @return
	 */
	public synchronized int  addTemplateLoader(TemplateLoader templateLoader){
		isSetLoader=false;
		list.add(templateLoader);
		return list.size()-1;
	}
	/**
	 * 加入一个文件加载器
	 * @param folderPath
	 * @return
	 * @throws IOException 
	 */
	public int addFolderLoader(String folderPath) throws IOException{
		return addFolderLoader(new File(folderPath));
	}
	/**
	 * 加入一个文件夹加载器
	 * @param folder
	 * @return
	 * @throws IOException 
	 */
	public int addFolderLoader(File folder) throws IOException{
		FileTemplateLoader fileTemplateLoader=new FileTemplateLoader(folder);
		return addTemplateLoader(fileTemplateLoader);
		
	}
	/**
	 * 加入一个类位置加载器
	 * @param classType
	 * @param prefix
	 * @return
	 */
	public int addClassLoader(Class<?> classType,String prefix){
		ClassTemplateLoader classTemplateLoader=new ClassTemplateLoader(classType, prefix);
		return addTemplateLoader(classTemplateLoader);
	}
	/**
	 * 获得当前的多类型加载器
	 * @return
	 */
	private MultiTemplateLoader getMultiTemplateLoader(){
		TemplateLoader[] loaders=new TemplateLoader[list.size()];
		for(int i=0;i<list.size();i++){
			loaders[i]=list.get(i);
		}
		MultiTemplateLoader m=new MultiTemplateLoader(loaders);
		return m;
	}
	/**
	 * 返回某个名字的模版
	 * @param name
	 * @return
	 * @throws IOException 
	 * @throws ParseException 
	 * @throws MalformedTemplateNameException 
	 * @throws TemplateNotFoundException 
	 */
	public Template getTemplate(String name) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException{
		if(!isSetLoader){
			flushLoader();
		}
		if(suffix!=null){
			name=name+suffix;
		}
		return cfg.getTemplate(name);
	}
	/**
	 * 返回配置对象
	 * @return
	 */
	public Configuration getConfiguraton(){
		return cfg;
	}
	/**
	 * 刷新模版加载器
	 */
	public synchronized void flushLoader(){
		isSetLoader=true;
		cfg.setTemplateLoader(getMultiTemplateLoader());
	}
	/**
	 * 模版处理数据
	 * @param t
	 * @param rootMap
	 * @param out
	 * @return
	 * @throws IOException 
	 * @throws TemplateException 
	 */
	public void process(Template t,Object rootMap,Writer out) throws IOException, TemplateException{

		try{
			t.process(rootMap, out);
		}finally {
			if(isCloseStream&&out!=null) {
				out.close();
			}
		}
	}

	
	/**
	 * 模版处理数据
	 * @param templateName
	 * @param rootMap
	 * @param out
	 * @return
	 * @throws TemplateException 
	 * @throws IOException 
	 */
	public void process(String templateName,Object rootMap,Writer out) throws IOException, TemplateException{
		Template t=getTemplate(templateName);
		 process(t, rootMap, out);
	}
	/**
	 * 模版处理数据
	 * @param t
	 * @param rootMap
	 * @param os
	 * @return
	 * @throws TemplateException 
	 * @throws IOException 
	 */
	public void process(Template t,Object rootMap,OutputStream os) throws IOException, TemplateException{
		Writer out=new OutputStreamWriter(os);
		process(t,rootMap,out);
		
	}
	/**
	 * 模版处理数据
	 * @param templateName
	 * @param rootMap
	 * @param os
	 * @return
	 * @throws TemplateException 
	 * @throws IOException 
	 */
	public void process(String templateName,Object rootMap,OutputStream os) throws IOException, TemplateException{
		Writer out=new OutputStreamWriter(os);
		process(templateName, rootMap, out);
	}
	/**
	 * 模版处理数据
	 * @param templateName
	 * @param rootMap
	 * @param filePath
	 * @return
	 * @throws TemplateException 
	 * @throws IOException 
	 */
	public void process(String templateName,Object rootMap,String filePath) throws IOException, TemplateException{
		process(templateName, rootMap, new File(filePath));
	}
	/**
	 * 模版处理数据
	 * @param templateName
	 * @param rootMap
	 * @param file
	 * @return
	 * @throws TemplateException 
	 * @throws IOException 
	 */
	public void process(String templateName,Object rootMap,File file) throws IOException, TemplateException{
		File folder=file.getParentFile();
		if(!file.exists()){
			folder.mkdirs();
		}
		Writer out=new FileWriter(file);
		process(templateName, rootMap, out);
	}

	static class FreemarkerUtilBuilder{
		private List<TemplateLoader> list=new ArrayList<TemplateLoader>();
		private Configuration cfg;
		private String suffix;
		private boolean isCloseStream=true;
		private FreemarkerUtilBuilder() {
		}
		public static FreemarkerUtilBuilder create(){
			return new FreemarkerUtil.FreemarkerUtilBuilder();
		}

		public FreemarkerUtilBuilder setCfg(Configuration cfg) {
			this.cfg = cfg;
			return this;
		}
		public FreemarkerUtilBuilder addTemplateLoader(TemplateLoader templateLoader) {
			list.add(templateLoader);
			return this;
		}

		/**
		 * 加入一个文件加载器
		 * @param folderPath
		 * @return
		 * @throws IOException
		 */
		public FreemarkerUtilBuilder addFolderLoader(String folderPath) throws IOException{
			return addFolderLoader(new File(folderPath));
		}
		/**
		 * 加入一个文件夹加载器
		 * @param folder
		 * @return
		 * @throws IOException
		 */
		public FreemarkerUtilBuilder addFolderLoader(File folder) throws IOException{
			FileTemplateLoader fileTemplateLoader=new FileTemplateLoader(folder);
			return addTemplateLoader(fileTemplateLoader);

		}
		/**
		 * 加入一个类位置加载器
		 * @param classType
		 * @param prefix
		 * @return
		 */
		public FreemarkerUtilBuilder addClassLoader(Class<?> classType,String prefix){
			ClassTemplateLoader classTemplateLoader=new ClassTemplateLoader(classType, prefix);
			return addTemplateLoader(classTemplateLoader);
		}

		public FreemarkerUtilBuilder setSuffix(String suffix) {
			this.suffix = suffix;
			return this;
		}

		/**
		 * 设置是否自动关闭流
		 * @param isCloseStream
		 */
		public FreemarkerUtilBuilder setIsCloseStream(boolean isCloseStream){
			this.isCloseStream=isCloseStream;
			return this;
		}

		public  FreemarkerUtil build(){
			FreemarkerUtil util;
			if(cfg==null){
				util=new FreemarkerUtil();
			}else{
				util=new FreemarkerUtil(this.cfg);
			}
			util.setSuffix(this.suffix);
			util.setTemplateLoaderList(this.list);
			util.setIsCloseStream(this.isCloseStream);
			util.flushLoader();
			return util;
		}

	}
	
	
}
