package org.frameworkset.pinpoint.plugin.generator;
/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import bboss.org.apache.velocity.Template;
import bboss.org.apache.velocity.VelocityContext;
import com.frameworkset.util.FileUtil;
import com.frameworkset.util.VelocityUtil;
import org.frameworkset.runtime.CommonLauncher;
import org.frameworkset.runtime.StreamGobbler;
import org.frameworkset.util.encoder.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class PluginGenerator {
	private static Logger log = LoggerFactory.getLogger(PluginGenerator.class);
	private GenConfig genConfig;
	private  File appDir;
	private File pluginProjectGradleBuildFile;
	private File pluginProjectGradleBuildSHFile;
	private File pluginProjectGradleBuildBatFile;
	private File pluginProjectGradleBuildPropertiesFile;
	private File pluginProjectMetaInfoPluginFile;
	private File pluginProjectMetaInfoProviderFile;
	private File pluginProjectJavaProviderFile;

	private File pluginProjectJavaInterceptorFile;
	private File pluginProjectJavaSpanTraceInterceptorFile;
	private File pluginProjectJavaDetectFile;
	private File pluginProjectJavaPluginFile;
	private File pluginProjectJavaPluginConstantsFile;
	private File pluginProjectJavaPluginConfigFile;
	private File pluginProjectJavaMethodInfoFile;
	private File pluginProjectJavaMethodFilterFile;
	private File pluginProjectJavaInterceptorClassInfoFile;
	private File pluginProjectReadMeFile;
	private File pluginProjectImageIconFile;
	private File pluginProjectImageServerMapFile;
	private File pluginProjectDir;

	private File pluginArchiveDir;
	private File pluginLibDir;
	private File toolLibDir;
	private String packageDir;
	private VelocityContext velocityContext;
	public PluginGenerator(GenConfig genConfig,File appDir){
		this.genConfig = genConfig;
		this.appDir = appDir;
	}
	private void init(){
		this.initFiles();
		this.initVelocityContext();
	}
	private void initFiles(){
		this.pluginProjectDir = new File(appDir,genConfig.getPluginName());
		if(this.pluginProjectDir.exists())
			FileUtil.deleteFile(this.pluginProjectDir);
		this.pluginArchiveDir = new File(appDir,"dist");
		this.toolLibDir = new File(appDir,"lib");
		if(this.pluginArchiveDir.exists())
			FileUtil.deleteFile(this.pluginArchiveDir);
		this.packageDir = genConfig.getServicePackage().replace('.','/');
		pluginProjectGradleBuildFile = new File(this.pluginProjectDir,"build.gradle");
		pluginLibDir = new File(this.pluginProjectDir,"lib");
		String src = "src/main/java";
//		String resources = "src/main/resources";
		pluginProjectGradleBuildPropertiesFile = new File(this.pluginProjectDir,"gradle.properties");
		pluginProjectGradleBuildSHFile = new File(this.pluginProjectDir,"build.sh");
		pluginProjectGradleBuildBatFile = new File(this.pluginProjectDir,"build.bat");
		pluginProjectMetaInfoPluginFile = new File(this.pluginProjectDir,genConfig.getMetaInfServicesProfilerPluginFileDir()+"/"+genConfig.getMetaInfServicesProfilerPluginFileName());
		if(!pluginProjectMetaInfoPluginFile.getParentFile().exists()){
			pluginProjectMetaInfoPluginFile.getParentFile().mkdirs();
		}

		pluginProjectMetaInfoProviderFile  = new File(this.pluginProjectDir,genConfig.getMetaInfServicesProfilerPluginFileDir()+"/"+genConfig.getMetaInfServicesTraceMetadataProviderFileName());
		pluginProjectJavaProviderFile = new File(this.pluginProjectDir,
											src+"/"+this.packageDir+"/"+genConfig.getServiceName()+"MetadataProvider.java");
		pluginProjectJavaInterceptorFile = new File(this.pluginProjectDir,
				src+"/"+this.packageDir+"/interceptor/"+genConfig.getServiceName()+"OperationInterceptor.java");
		pluginProjectJavaSpanTraceInterceptorFile = new File(this.pluginProjectDir,
				src+"/"+this.packageDir+"/interceptor/"+genConfig.getServiceName()+"BaseOperationInterceptor.java");
		pluginProjectJavaDetectFile = new File(this.pluginProjectDir,
				src+"/"+this.packageDir+"/"+genConfig.getServiceName()+"ProviderDetector.java");
		if(!pluginProjectJavaInterceptorFile.getParentFile().exists()){
			pluginProjectJavaInterceptorFile.getParentFile().mkdirs();
		}
		 pluginProjectJavaPluginFile = new File(this.pluginProjectDir,
				 src+"/"+this.packageDir+"/"+genConfig.getServiceName()+"Plugin.java");
		pluginProjectJavaPluginConstantsFile = new File(this.pluginProjectDir,
				src+"/"+this.packageDir+"/"+genConfig.getServiceName()+"Constants.java");
		 pluginProjectJavaPluginConfigFile = new File(this.pluginProjectDir,
				 src+"/"+this.packageDir+"/"+genConfig.getServiceName()+"PluginConfig.java");

		pluginProjectJavaMethodInfoFile  = new File(this.pluginProjectDir,
				src+"/"+this.packageDir+"/"+genConfig.getServiceName()+"MethodInfo.java");
		pluginProjectJavaMethodFilterFile = new File(this.pluginProjectDir,
				src+"/"+this.packageDir+"/"+genConfig.getServiceName()+"CustomMethodFilter.java");
		pluginProjectJavaInterceptorClassInfoFile = new File(this.pluginProjectDir,
				src+"/"+this.packageDir+"/"+genConfig.getServiceName()+"InterceptorClassInfo.java");
		if(genConfig.getPluginInterceptorType().equals(GenConfig.pluginInterceptorSpanTraceType)) {
			this.pluginProjectImageIconFile = new File(this.pluginArchiveDir,
					"images/icons/"+genConfig.getServiceName()+".png");
			this.pluginProjectImageServerMapFile = new File(this.pluginArchiveDir,
					"images/servermap/"+genConfig.getServiceName()+".png");
		}
		 pluginProjectReadMeFile = new File(this.pluginArchiveDir,
				 "plugin.config");
		if(!pluginArchiveDir.exists()){
			pluginArchiveDir.mkdirs();
		}
	}
	private void copyPinpointLibs()  {
		File[] pinpointLibs = this.toolLibDir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isFile() && pathname.getName().startsWith("pinpoint-") && !pathname.getName().startsWith("pinpoint-plugin-generate");
			}
		});
		if(!pluginLibDir.exists()){
			pluginLibDir.mkdirs();
		}
		for(int i = 0; i < pinpointLibs.length; i ++){
			try {
				FileUtil.copy(pinpointLibs[i], this.pluginLibDir.getCanonicalPath());
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	private void initVelocityContext(){
		velocityContext = new VelocityContext();
		velocityContext.put("serviceName",genConfig.getServiceName());
		velocityContext.put("interceptorClasses",genConfig.getIntercepteClasses());
		velocityContext.put("metaInfServicesProfilerPluginFileDir",genConfig.getMetaInfServicesProfilerPluginFileDir());
		velocityContext.put("metaInfServicesTraceMetadataProviderFileName",genConfig.getMetaInfServicesTraceMetadataProviderFileName());
		velocityContext.put("metaInfServicesProfilerPluginFileName",genConfig.getMetaInfServicesProfilerPluginFileName());
		velocityContext.put("pluginAuthor",genConfig.getPluginAuthor());
		velocityContext.put("pluginName",genConfig.getPluginName());
		velocityContext.put("servicePackage",genConfig.getServicePackage());
		velocityContext.put("serviceType",genConfig.getServiceType());

		velocityContext.put("eventServiceType",genConfig.getEventServiceType());

		velocityContext.put("pluginEnabled",genConfig.isPluginEnabled());

		velocityContext.put("recordArgs",genConfig.isRecordArgs());
		velocityContext.put("pluginVersion",genConfig.getPluginVersion());
		velocityContext.put("recordResult",genConfig.isRecordResult());
		velocityContext.put("pluginSrcFileDir",genConfig.getPluginSrcFileDir());
		velocityContext.put("pluginInterceptorType",genConfig.getPluginInterceptorType());

		velocityContext.put("argKeyCode",genConfig.getArgKeyCode());
		if(genConfig.getArgKeyName() == null || genConfig.getArgKeyName().equals(""))
			velocityContext.put("argKeyName","ext.args");
		else
			velocityContext.put("argKeyName",genConfig.getArgKeyName());
		if(genConfig.getExecutionPolicy() == null || genConfig.getExecutionPolicy().equals(""))
			velocityContext.put("executionPolicy","BOUNDARY");
		else
			velocityContext.put("executionPolicy",genConfig.getExecutionPolicy());


	}
	public void genPlugin(){
		this.init();
		this.genJavaCodeFiles();
		this.genMetaInfFiles();
		this.genBuildFile();
		genImages();
		this.genReadMeFile();
		this.copyPinpointLibs();
		this.archivePlugin();
		this.releaseFiles();

		this.cleanResource();
	}

	private void releaseFiles(){
		try {
			log.info("从目录"+new File(this.pluginProjectDir,"/build/dist").getAbsolutePath()+"拷贝文件到"+pluginArchiveDir.getAbsolutePath()+"开始！");
			FileUtil.copy(new File(this.pluginProjectDir,"/build/dist"),pluginArchiveDir.getAbsolutePath());
			log.info("从目录"+new File(this.pluginProjectDir,"/build/dist").getAbsolutePath()+"拷贝文件到"+pluginArchiveDir.getAbsolutePath()+"完毕！");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void cleanResource(){
		if(genConfig.isCleanSource())
			FileUtil.deleteFile(this.pluginProjectDir);
	}

	private void genJavaCodeFiles(){
		_genFile("plugin/src/PersistentMetadataProvider.vm",this.pluginProjectJavaProviderFile);

		if(genConfig.getPluginInterceptorType().equals(GenConfig.pluginInterceptorSpanEventType)) {
			_genFile("plugin/src/PersistentOperationInterceptor.vm",this.pluginProjectJavaInterceptorFile);
		}
		else{
			_genFile("plugin/src/SpanTracePersistentOperationInterceptor.vm",this.pluginProjectJavaInterceptorFile);
		}

		_genFile("plugin/src/PersistentPlugin.vm",this.pluginProjectJavaPluginFile);
		_genFile("plugin/src/Constants.vm",this.pluginProjectJavaPluginConstantsFile);


		_genFile("plugin/src/PersistentPluginConfig.vm",this.pluginProjectJavaPluginConfigFile);

		_genFile("plugin/src/BaseOperationInterceptor.vm",this.pluginProjectJavaSpanTraceInterceptorFile);

		_genFile("plugin/src/PersistentProviderDetector.vm",this.pluginProjectJavaDetectFile);

		_genFile("plugin/src/CustomMethodFilter.vm",this.pluginProjectJavaMethodFilterFile);
		_genFile("plugin/src/InterceptorClassInfo.vm",this.pluginProjectJavaInterceptorClassInfoFile);
		_genFile("plugin/src/MethodInfo.vm",this.pluginProjectJavaMethodInfoFile);

	}


	private void genMetaInfFiles(){
		_genFile("plugin/resources/metainf/profilerPlugin.vm",this.pluginProjectMetaInfoPluginFile);
		_genFile("plugin/resources/metainf/traceMetadataProvider.vm",this.pluginProjectMetaInfoProviderFile);

	}
	private void genImages(){
		try {
			if (genConfig.getPluginInterceptorType().equals(GenConfig.pluginInterceptorSpanTraceType)) {
				if(!pluginProjectImageIconFile.getParentFile().exists()){
					pluginProjectImageIconFile.getParentFile().mkdirs();
				}
				if(!pluginProjectImageServerMapFile.getParentFile().exists()){
					pluginProjectImageServerMapFile.getParentFile().mkdirs();
				}
				FileUtil.fileCopy(new File(this.appDir, "resources/templates/plugin/images/icons/BBoss.png"), pluginProjectImageIconFile );
//			this.pluginProjectImageIconFile = new File(this.pluginArchiveDir,
//					"images/icons/"+genConfig.getServiceName()+".png");
				FileUtil.fileCopy(new File(this.appDir, "resources/templates/plugin/images/servermap/BBoss.png"), pluginProjectImageServerMapFile);
//			this.pluginProjectImageIconFile = new File(this.pluginArchiveDir,
//					"images/servermap/"+genConfig.getServiceName()+".png");
			}
		}
		catch (Exception e){
			log.error("",e);
		}
	}
	private void genReadMeFile(){
		_genFile("plugin/readme.vm",this.pluginProjectReadMeFile);

	}

	private void genBuildFile(){
		_genFile("plugin/build/build.gradle",this.pluginProjectGradleBuildFile);
		_genFile("plugin/build/gradle.properties",this.pluginProjectGradleBuildPropertiesFile);
		_genFile("plugin/build/build.sh",this.pluginProjectGradleBuildSHFile);
		_genFile("plugin/build/build.bat",this.pluginProjectGradleBuildBatFile);


	}
	protected void chmodx() throws IOException
	{
		Process proc = !CommonLauncher.isOSX()?
				Runtime.getRuntime().exec("chmod +x -R "+
						this.pluginProjectDir
								.getCanonicalPath()):

				Runtime.getRuntime().exec("chmod -R +x "+
						this.pluginProjectDir
								.getCanonicalPath())			;
		StreamGobbler error = new StreamGobbler( proc.getErrorStream(),"ERROR",null);

		StreamGobbler normal = new StreamGobbler( proc.getInputStream(),"NORMAL",null);
		error.start();
		normal.start();

		try {
			int exitVal = proc.waitFor();
		} catch (InterruptedException e) {
			log.error("授予执行权限失败：",e);
		}
	}
	private void archivePlugin(){
		try {
			Process proc = null;
			if (CommonLauncher.isWindows()) {

				proc = Runtime.getRuntime().exec(
						new File(this.pluginProjectDir, "/build.bat")
								.getCanonicalPath());
//					${dbinitpath}

			}
			else
			{
				chmodx();

				proc = Runtime.getRuntime().exec("sh "+
						new File(this.pluginProjectDir, "/build.sh")
								.getCanonicalPath());
			}
			StreamGobbler error = new StreamGobbler( proc.getErrorStream(),"INFO",null);

			StreamGobbler normal = new StreamGobbler( proc.getInputStream(),"NORMAL",null);
			error.start();
			normal.start();

			int exitVal = proc.waitFor();
			log.info("构建插件完毕! " );


		} catch (Throwable t) {
			log.error("构建插件失败：",t);
		}
		finally
		{

		}
	}
	private void _genFile(String vm,File File){
		Writer writer = null;
		OutputStream out = null;
		try {
			Template template = VelocityUtil.getTemplate(vm);
			out = new FileOutputStream(File);
			writer = new OutputStreamWriter(out, Charsets.UTF_8);
			template.merge(this.velocityContext, writer);
			writer.flush();
		}
		catch (Exception e) {
			log.error("生成文件失败："+File.getAbsolutePath(),e);
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

}
