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

import org.frameworkset.spi.assemble.PropertiesContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
	private static File appDir;
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	public static void main(String[] args){
		PropertiesContainer propertiesContainer = new PropertiesContainer();
		propertiesContainer.addConfigPropertiesFile("plugin.properties");
		GenConfig genConfig = buildGenConfig(propertiesContainer);
		PluginGenerator pluginGenerator = new PluginGenerator(genConfig,appDir);
		pluginGenerator.genPlugin();
	}
	private static GenConfig buildGenConfig(PropertiesContainer propertiesContainer) {
		GenConfig genConfig = new GenConfig();
		genConfig.setPluginName(propertiesContainer.getProperty("plugin.name"));
		genConfig.setServiceName(propertiesContainer.getProperty("plugin.serviceName"));
		genConfig.setPluginAuthor(propertiesContainer.getProperty("plugin.author"));
		genConfig.setPluginVersion(propertiesContainer.getProperty("plugin.version"));
		genConfig.setServicePackage(propertiesContainer.getProperty("plugin.package"));
		String originInterceptorType = propertiesContainer.getProperty("plugin.interceptor.type");
		if(originInterceptorType == null){
			originInterceptorType = GenConfig.pluginInterceptorSpanTraceType;
		}
		else{
			if(!originInterceptorType.equals(GenConfig.pluginInterceptorSpanEventType) && !originInterceptorType.equals(GenConfig.pluginInterceptorSpanTraceType))
			{
				throw new java.lang.IllegalArgumentException("plugin.interceptor.type = "+originInterceptorType+",must be "+GenConfig.pluginInterceptorSpanEventType + " or " + GenConfig.pluginInterceptorSpanTraceType);
			}
		}
		String serviceType = propertiesContainer.getProperty("plugin.serviceType");
		try {
			genConfig.setServiceType(Short.parseShort(serviceType));
		} catch (Exception e) {
			throw new java.lang.IllegalArgumentException("plugin.serviceType = " + serviceType + ",Must be a short scope number.");
		}


		String eventServiceType = propertiesContainer.getProperty("plugin.eventServiceType");
		try {
			if(originInterceptorType.equals(GenConfig.pluginInterceptorSpanTraceType ) && (eventServiceType == null || eventServiceType.equals(""))){
				throw new java.lang.IllegalArgumentException("plugin.interceptor.type = "+originInterceptorType+",must set  plugin.eventServiceType in plugin.properties");
			}
			genConfig.setEventServiceType(Short.parseShort(eventServiceType));
		} catch (Exception e) {
			throw new java.lang.IllegalArgumentException("plugin.eventServiceType = " + eventServiceType + ",Must be a short scope number.");
		}

		String argKeyCode = propertiesContainer.getProperty("plugin.argKeyCode");


		if (argKeyCode != null){
			try {
				genConfig.setArgKeyCode(Short.parseShort(argKeyCode));
			} catch (Exception e) {
				throw new java.lang.IllegalArgumentException("plugin.argKeyCode = " + argKeyCode + ",Must be a short scope number.");
			}
		}
		String argKeyName = propertiesContainer.getProperty("plugin.argKeyName");


		if (argKeyName != null){
			genConfig.setArgKeyName(argKeyName);
		}

		String executionPolicy = propertiesContainer.getProperty("plugin.executionPolicy");

		if (executionPolicy != null){
			genConfig.setExecutionPolicy(executionPolicy);
		}

		String enable = propertiesContainer.getProperty("profiler.enable");
		genConfig.setPluginEnabled(Boolean.parseBoolean(enable));

		String deleteFilesAfterGen = propertiesContainer.getProperty("plugin.deleteFilesAfterGen");
		if(deleteFilesAfterGen != null)
			genConfig.setCleanSource(Boolean.parseBoolean(deleteFilesAfterGen));
		String recordResult = propertiesContainer.getProperty("profiler.recordResult");
		genConfig.setRecordResult(Boolean.parseBoolean(recordResult));
		String recordArgs = propertiesContainer.getProperty("profiler.recordArgs");

		genConfig.setRecordArgs(Boolean.parseBoolean(recordArgs));
		String originInterceptorClasses = propertiesContainer.getProperty("plugin.interceptor.classes");
		genConfig.setOriginIntercepteClasses(originInterceptorClasses);
		genConfig.setIntercepteClasses(buildInterceptorClassInfos(originInterceptorClasses));

		genConfig.setPluginInterceptorType(originInterceptorType);
		return genConfig;
	}
	private static List<InterceptorClassInfo> buildInterceptorClassInfos(String originIntercepteClasses){
		String[] _originIntercepteClasses = originIntercepteClasses.split(" ");
		logger.info(originIntercepteClasses);
		List<InterceptorClassInfo> intercepteClassInfos = new ArrayList<InterceptorClassInfo>();
		int i = 0;
		for(String clazz:_originIntercepteClasses){
			clazz = clazz.trim();
			int pos = clazz.indexOf("|");
			InterceptorClassInfo interceptorClassInfo = null;
			if(pos > 0){
				String method = clazz.substring(pos + 1);
				clazz = clazz.substring(0,pos);
				interceptorClassInfo = new InterceptorClassInfo();
				interceptorClassInfo.setInterceptorClass(clazz.trim());
				interceptorClassInfo.setInterceptorMehtods(buildMethodInfo(interceptorClassInfo,method));
			}
			else
			{
				interceptorClassInfo = new InterceptorClassInfo();
				interceptorClassInfo.setInterceptorClass(clazz.trim());
			}
			intercepteClassInfos.add(interceptorClassInfo);
			i ++;
		}
		return intercepteClassInfos;
	}

	/**
	 * class|aaa~0,aaaa~1,bbbb
	 * @param methods
	 * @return
	 */
	private static List<MethodInfo> buildMethodInfo(InterceptorClassInfo intercepteClassInfo,String methods){
		String[] _methods = methods.split(",");
		List<MethodInfo> methodInfos = new ArrayList<MethodInfo>();
		MethodInfo allAccept = null;
		MethodInfo allReject = null;
		for (String m:_methods) {
			MethodInfo methodInfo = new MethodInfo();
			String[] infos = m.split("~");
			String name = infos[0].trim();
			if(name.equals("*")){
				if (infos.length > 1) {
					int filterType = Integer.parseInt(infos[1].trim());
					if(filterType == MethodInfo.FILTER_ACCEPT_TYPE){
						allAccept = methodInfo;
					}
					else{
						allReject = methodInfo;
					}
				}
				else{
					allAccept = methodInfo;
				}
			}
			if(name.endsWith("*")){
				name = name.substring(0,name.indexOf("*"));
				if(name.equals(""))
					name = "*";
				methodInfo.setPattern(true);
			}
			if (infos.length > 1) {
				methodInfo.setName(name);
				methodInfo.setFilterType(Integer.parseInt(infos[1].trim()));
			} else {
				methodInfo.setName(name);
				methodInfo.setFilterType(MethodInfo.FILTER_ACCEPT_TYPE);
			}
			methodInfos.add(methodInfo);
		}
		intercepteClassInfo.setAllAccept(allAccept);
		intercepteClassInfo.setAllReject(allReject);
		return methodInfos;


	}

	public static void setAppdir(File appDir){
		Main.appDir = appDir;
	}

}
